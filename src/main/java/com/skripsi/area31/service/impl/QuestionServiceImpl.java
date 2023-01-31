package com.skripsi.area31.service.impl;

import com.skripsi.area31.model.course.*;
import com.skripsi.area31.model.response.SimpleCustomResponse;
import com.skripsi.area31.model.tesaurus.Tesaurus;
import com.skripsi.area31.model.user.User;
import com.skripsi.area31.repository.*;
import com.skripsi.area31.service.QuestionService;
import com.skripsi.area31.util.FieldName;
import com.skripsi.area31.util.Stemmer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.Principal;
import java.util.*;

@Service(value = "questionService") public class QuestionServiceImpl implements QuestionService {
    @Autowired TesaurusRepository tesaurusRepository;
    @Autowired QuestionBankRepository questionBankRepository;
    @Autowired UserRepository userRepository;
    @Autowired QuestionRepository questionRepository;
    @Autowired QuizRepository quizRepository;
    private Stemmer stemmer = new Stemmer();

    @Override public ResponseEntity createQuestionBank(QuestionBankRequest questionBankRequest,
        Principal principal) throws IOException {
        if (questionBankRequest.getDescription() != null && questionBankRequest.getTitle() != null
            && !questionBankRequest.getTitle().equals("")) {
            QuestionBank newQuestionBank = new QuestionBank();
            User user = userRepository.findByEmail(principal.getName());
            newQuestionBank.setIdInstructor(user.getIdUser());
            newQuestionBank.setTitle(questionBankRequest.getTitle());
            newQuestionBank.setDescription(questionBankRequest.getDescription());
            saveQuestion(newQuestionBank, questionBankRequest.getListQuestion());
            return new ResponseEntity<>(
                new SimpleCustomResponse(200, "Success create question bank"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new SimpleCustomResponse(400, "Value can't be null"),
                HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity updateQuestionBank(UpdateQuestionBankRequest questionBankRequest,
        String idQuestionBank, Principal principal) throws IOException {
        String message = "";
        QuestionBank questionBankExist =
            questionBankRepository.findQuestionBankByIdQuestionBank(idQuestionBank);
        if (questionBankExist != null) {
            ResponseEntity quiz = checkOngoingQuiz(idQuestionBank);
            if (quiz != null)
                return quiz;
            if (questionBankRequest.getDescription() != null && !questionBankRequest
                .getDescription().equals("")) {
                questionBankExist.setDescription(questionBankRequest.getDescription());
                message += "Updated Description, ";
            }
            if (questionBankRequest.getTitle() != null && !questionBankRequest.getTitle()
                .equals("")) {
                questionBankExist.setTitle(questionBankRequest.getTitle());
                message += "Updated Title, ";
            }
            if (questionBankRequest.getListDeletedIdQuestion() != null) {
                for (String deletedQuestion : questionBankRequest.getListDeletedIdQuestion()) {
                    Question question =
                        questionRepository.findQuestionByIdQuestion(deletedQuestion);
                    if (question != null) {
                        Set<String> listIdQuestion = questionBankExist.getListIdQuestion();
                        listIdQuestion.remove(deletedQuestion);
                        questionBankExist.setListIdQuestion(listIdQuestion);
                        message += "Deleted " + question.getIdQuestion() + " ";
                        questionRepository.delete(question);
                    }
                }
            }
            saveQuestion(questionBankExist, questionBankRequest.getListQuestion());
            return new ResponseEntity<>(
                new SimpleCustomResponse(200, "Success update question bank " + message),
                HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new SimpleCustomResponse(400, "Question bank not found"),
                HttpStatus.BAD_REQUEST);
        }
    }

    @Override public ResponseEntity deleteQuestionBank(String idQuestionBank) {
        ResponseEntity quiz = checkOngoingQuiz(idQuestionBank);
        if (quiz != null)
            return quiz;
        QuestionBank questionBank =
            questionBankRepository.findQuestionBankByIdQuestionBank(idQuestionBank);
        for (String idQuestion : questionBank.getListIdQuestion()) {
            questionRepository.delete(questionRepository.findQuestionByIdQuestion(idQuestion));
        }
        questionBankRepository.delete(questionBank);
        return new ResponseEntity<>(new SimpleCustomResponse(200, "Success delete question bank"),
            HttpStatus.OK);
    }

    private ResponseEntity checkOngoingQuiz(String idQuestionBank) {
        List<Quiz> quizList = quizRepository.findAllByIdQuestionBank(idQuestionBank);
        if (quizList != null) {
            for (Quiz quiz : quizList) {
                if (quiz.getStartDate() < Calendar.getInstance().getTimeInMillis()
                    && quiz.getStartDate() + quiz.getDuration() > Calendar.getInstance()
                    .getTimeInMillis()) {
                    return new ResponseEntity<>(new SimpleCustomResponse(400,
                        "Failed update question bank, quiz " + quiz.getTitle() + " still ongoing"),
                        HttpStatus.BAD_REQUEST);
                }
            }
        }
        return null;
    }

    @Override public ResponseEntity getQuestionBankList(Integer page, Principal principal) {
        User user = userRepository.findByEmail(principal.getName());
        PageRequest request = PageRequest.of(page, 10, Sort.by(Sort.Direction.ASC, "title"));
        return ResponseEntity
            .ok(questionBankRepository.findAllByIdInstructor(user.getIdUser(), request));
    }

    @Override public ResponseEntity getQuestionDetails(String idQuestionBank) {
        QuestionBank questionBank =
            questionBankRepository.findQuestionBankByIdQuestionBank(idQuestionBank);
        List<Question> questionList = new ArrayList<>();
        for (String idQuestion : questionBank.getListIdQuestion()) {
            questionList.add(questionRepository.findQuestionByIdQuestion(idQuestion));
        }
        return ResponseEntity
            .ok(new QuestionBankDetails(questionBank.getIdQuestionBank(), questionBank.getTitle(),
                questionBank.getDescription(), questionList));
    }

    private void saveQuestion(QuestionBank questionBankExist, List<Question> listQuestion)
        throws IOException {
        if (listQuestion != null) {
            for (Question question : listQuestion) {
                if (!question.getQuestionType().equals("MULTIPLECHOICE")) {
                    String[] steamedAnswerKey =
                        question.getAnswerKey().toLowerCase().split("\\P{Alpha}+");
                    List<String> formattedAnswerKey = new ArrayList<>();
                    for (String s : steamedAnswerKey) {
                        if (!FieldName.Constants.STOPWORD
                            .matches(".*\\b" + s + "\\b.*")) { //check if word is stopword or not
                            formattedAnswerKey.add(stemmer.steam(s));
                        }
                    }

                    question.setSteamedAnswerKey(formattedAnswerKey.toArray(new String[0]));
                    List<Tesaurus> tesaurusList = new ArrayList<>();
                    for (String answerKeyWord : question.getSteamedAnswerKey()) {
                        tesaurusList.add(tesaurusRepository.findTop1ByMainWord(answerKeyWord));
                    }
                    question.setSynonymAnswerKey(tesaurusList);
                }
                questionRepository.save(question);
                if (questionBankExist.getListIdQuestion() == null) {
                    questionBankExist.setListIdQuestion(new HashSet<>());
                }
                Set<String> listIdQuestion = questionBankExist.getListIdQuestion();
                listIdQuestion.add(question.getIdQuestion());
                questionBankExist.setListIdQuestion(listIdQuestion);
            }
        }
        questionBankRepository.save(questionBankExist);
    }
}

   /* @Override public ResponseEntity checkQuestionAnswer(Answer answer) {
        int vectorD = 0;
        int vectorQ = 0;
        int sumVector = 0;
        String[] splittedAnswerKey = answer.getAnswerKey().toLowerCase().split(" ");
        String[] splittedAnswer = answer.getAnswer().toLowerCase().split(" ");
        HashMap<String, Integer> splittedAnswerKeyMap = new HashMap<>();
        HashMap<String, Integer> splittedAnswerMap = new HashMap<>();

        for (String s : splittedAnswerKey) {
            incrementValue(splittedAnswerKeyMap, s);
            setValueZero(splittedAnswerMap, s);
        }

        for (String s : splittedAnswer) {
            incrementValue(splittedAnswerMap, s);
            setValueZero(splittedAnswerKeyMap, s);
        }

        for (Map.Entry<String, Integer> entry : splittedAnswerMap.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            int valueAnswerKey = splittedAnswerKeyMap.get(key);
            vectorD += value * value;
            vectorQ += valueAnswerKey * valueAnswerKey;
            sumVector += value * valueAnswerKey;
        }

        System.out.println(sumVector / (Math.sqrt(vectorD) * Math.sqrt(vectorQ)) * 100);


      *//*  Tesaurus tesaurus = new Tesaurus();
        tesaurus.setMainWord(splittedAnswerKey[1]);
        Set<String> synonyms = new TreeSet<>();
        synonyms.add(splittedAnswerKey[2]);
        synonyms.add(splittedAnswerKey[3]);
        synonyms.add(splittedAnswerKey[1]);
        tesaurus.setSynonyms(synonyms);
        tesaurusRepository.save(tesaurus);*//*

        System.out.println(tesaurusRepository.findByMainWord(splittedAnswerKey[1]));

        //        synonymChecker(splittedAnswerKey);

        return new ResponseEntity<>("Ok", HttpStatus.OK);*/

    /*private void synonymChecker(String[] splittedAnswerKey) {
        HashMap<Integer, Integer> synonymIndex = new HashMap<>();
        for (int i = 0; i < splittedAnswerKey.length; i++) {
            for (int j = 0; j < Dictionary.TESAURUS.length; j++) {
                if (Dictionary.TESAURUS[j].contains("," + splittedAnswerKey[i] + ",")) {
                    synonymIndex.put(i, j);
                    System.out.println(synonymIndex);
                    j = Dictionary.TESAURUS.length;
                }
            }
        }
    }
*/

