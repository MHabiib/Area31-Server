package com.skripsi.area31.service.impl;

import com.skripsi.area31.FcmClient;
import com.skripsi.area31.model.course.*;
import com.skripsi.area31.model.response.SimpleCustomResponse;
import com.skripsi.area31.model.user.User;
import com.skripsi.area31.repository.*;
import com.skripsi.area31.service.QuizService;
import com.skripsi.area31.util.FieldName;
import com.skripsi.area31.util.Stemmer;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;


@Service(value = "quizService") public class QuizServiceImpl implements QuizService {
    @Autowired QuizRepository quizRepository;
    @Autowired UserRepository userRepository;
    @Autowired CourseRepository courseRepository;
    @Autowired ReportRepository reportRepository;
    @Autowired QuestionBankRepository questionBankRepository;
    @Autowired QuestionRepository questionRepository;
    @Autowired QuestionFulfilledRepository questionFulfilledRepository;
    @Autowired ComplaintRepository complaintRepository;
    private Stemmer stemmer = new Stemmer();

    private static <K> void incrementValue(Map<K, Integer> map, K key) {
        // containsKey() checks if this map contains a mapping for a key
        Integer count = map.getOrDefault(key, 0);
        map.put(key, count + 1);
    }

    private static <K> void setValueZero(Map<K, Integer> map, K key) {
        map.put(key, map.getOrDefault(key, 0));
    }

    @Override public ResponseEntity createQuiz(Quiz quiz) {
        if (quiz.getIdCourse() == null || quiz.getTitle() == null || quiz.getDescription() == null
            || quiz.getDuration() == null || quiz.getIdCourse().equals("")
            || quiz.getIdQuestionBank() == null || quiz.getIdQuestionBank().equals("")) {
            return new ResponseEntity<>(new SimpleCustomResponse(400, "Value can't be null"),
                HttpStatus.BAD_REQUEST);
        }
        if (quiz.getStartDate() < Calendar.getInstance().getTimeInMillis()) {
            return new ResponseEntity<>(new SimpleCustomResponse(400, "startdate cant be past"),
                HttpStatus.BAD_REQUEST);
        }
        Course course = courseRepository.findCourseByIdCourse(quiz.getIdCourse());
        quiz.setStatus("INCOMING");
        quiz.setEndDate(quiz.getStartDate() + quiz.getDuration());
        quizRepository.save(quiz);
        for (String idStudent : course.getIdStudent()) {
            Report report = new Report();
            report.setIdStudent(idStudent);
            CourseServiceImpl.setReport(course, quiz, report, reportRepository);
        }
        return new ResponseEntity<>(new SimpleCustomResponse(200, "Success create quiz"),
            HttpStatus.OK);
    }

    @Override public ResponseEntity updateQuiz(Quiz quiz) {
        if (quiz.getStartDate() != null && quiz.getStartDate() < Calendar.getInstance()
            .getTimeInMillis()) {
            return new ResponseEntity<>(new SimpleCustomResponse(400, "startdate cant be past"),
                HttpStatus.BAD_REQUEST);
        }
        Quiz quizExist = quizRepository.findQuizByIdQuiz(quiz.getIdQuiz());
        if (quizExist.getStartDate()
            < Calendar.getInstance().getTimeInMillis() - 60000) { //minus 1 minutes
            return new ResponseEntity<>(
                new SimpleCustomResponse(400, "You can't edit ongoing / passed quiz"),
                HttpStatus.BAD_REQUEST);
        }
        Course course = courseRepository.findCourseByIdCourse(quizExist.getIdCourse());
        if (quiz.getTitle() != null) {
            quizExist.setTitle(quiz.getTitle());
        }
        if (quiz.getDescription() != null) {
            quizExist.setDescription(quiz.getDescription());
        }
        if (quiz.getStatus() != null) {
            quizExist.setStatus(quiz.getStatus());
        }
        if (quiz.getDuration() != null) {
            quizExist.setDuration(quiz.getDuration());
        }
        if (quiz.getStartDate() != null) {
            quizExist.setStartDate(quiz.getStartDate());
        }
        quizExist.setEndDate(quizExist.getStartDate() + quizExist.getDuration());
        quizRepository.save(quizExist);
        for (String idStudent : course.getIdStudent()) {
            Report report =
                reportRepository.findReportByIdQuizAndIdStudent(quizExist.getIdQuiz(), idStudent);
            CourseServiceImpl.setReport(course, quizExist, report, reportRepository);
        }
        return new ResponseEntity<>(new SimpleCustomResponse(200, "Success update quiz"),
            HttpStatus.OK);
    }

    @Override public ResponseEntity deleteQuiz(String id_quiz) {
        Quiz quiz = quizRepository.findQuizByIdQuiz(id_quiz);
        Course course = courseRepository.findCourseByIdCourse(quiz.getIdCourse());
        for (String idStudent : course.getIdStudent()) {
            Report report =
                reportRepository.findReportByIdQuizAndIdStudent(quiz.getIdQuiz(), idStudent);
            if (report != null) {
                reportRepository.delete(report);
            }
        }
        quizRepository.delete(quiz);
        return new ResponseEntity<>(new SimpleCustomResponse(200, "Success delete quiz"),
            HttpStatus.OK);
    }

    @Override
    public ResponseEntity listQuizStudent(String id_course, Integer page, Principal principal) {
        User user = userRepository.findByEmail(principal.getName());
        PageRequest request = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "quizDate"));
        return ResponseEntity.ok(reportRepository
            .findReportByIdStudentAndIdCourse(user.getIdUser(), id_course, request));
    }

    @Override public ResponseEntity startQuizStudent(String id_quiz, Principal principal) {
        User user = userRepository.findByEmail(principal.getName());
        Report report = reportRepository.findReportByIdQuizAndIdStudent(id_quiz, user.getIdUser());
        return reportDetails(report);
    }

    @Override public ResponseEntity createComplaint(String fcm, String id_quiz, String complaint,
        Principal principal) {
        User user = userRepository.findByEmail(principal.getName());
        Quiz quiz = quizRepository.findQuizByIdQuiz(id_quiz);
        Course course = courseRepository.findCourseByIdCourse(quiz.getIdCourse());
        Report report =
            reportRepository.findReportByIdQuizAndIdStudent(quiz.getIdQuiz(), user.getIdUser());
        Complaint newComplaint = new Complaint();
        newComplaint.setQuizTitle(quiz.getTitle());
        newComplaint.setIdReport(report.getIdReport());
        newComplaint.setIdUser(user.getIdUser());
        newComplaint.setDescription(complaint);
        newComplaint.setCreatedAt(Calendar.getInstance().getTimeInMillis());
        newComplaint.setStatus("PENDING");
        newComplaint.setIdInstructor(course.getIdInstructor());
        newComplaint.setIdCourse(course.getIdCourse());
        newComplaint.setFcm(fcm);
        complaintRepository.save(newComplaint);

        return new ResponseEntity<>(new SimpleCustomResponse(200, "Complaint created"),
            HttpStatus.OK);
    }

    @Override
    public ResponseEntity updateComplaint(String idComplaint, String status, String reason,
        Map<String, Integer> updatedScore) {
        Complaint complaint = complaintRepository.findComplaintByIdComplaint(idComplaint);
        if (status.equals("APPROVE")) {
            complaint.setStatus("APPROVE");
            complaint.setReason(reason);
            complaintRepository.save(complaint);
            Report report = reportRepository.findReportByIdReport(complaint.getIdReport());
            List<AnsweredQuestion> answeredQuestionList = report.getAnsweredQuestionList();
            for (Map.Entry<String, Integer> update : updatedScore.entrySet()) {
                for (AnsweredQuestion answeredQuestion : answeredQuestionList) {
                    if (answeredQuestion.getIdQuestionFulfilled().equals(update.getKey())) {
                        report.setScore(report.getScore() + (update.getValue() - answeredQuestion
                            .getStudentScore()));
                        answeredQuestion.setStudentScore(update.getValue());
                    }
                }
            }
            report.setAnsweredQuestionList(answeredQuestionList);
            reportRepository.save(report);
            return new ResponseEntity<>(new SimpleCustomResponse(200, "Complaint approved"),
                HttpStatus.OK);
        } else {
            complaint.setStatus("DECLINE");
            complaint.setReason(reason);
            complaintRepository.save(complaint);
            return new ResponseEntity<>(new SimpleCustomResponse(200, "Complaint declined"),
                HttpStatus.OK);
        }

    }

    @Override
    public ResponseEntity listQuizInstructor(String id_course, Integer page, Principal principal) {
        PageRequest request = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "quizDate"));
        return ResponseEntity.ok(quizRepository.findAllByIdCourse(id_course, request));
    }

    @Override public ResponseEntity listReportInstructor(String id_quiz, Principal principal) {
        List<Report> reportPage = reportRepository.findAllByIdQuizOrderByQuizDateDesc(id_quiz);
        for (Report report : reportPage) {
            report.setIdStudent(userRepository.findByIdUser(report.getIdStudent()).getName());
        }
        return ResponseEntity.ok(reportPage);
    }

    @Override public ResponseEntity reportDetailsInstructor(String id_report) {
        Report report = reportRepository.findReportByIdReport(id_report);
        return reportDetails(report);
    }

    private ResponseEntity reportDetails(Report report) {
        Quiz quiz = quizRepository.findQuizByIdQuiz(report.getIdQuiz());
        QuestionBank questionBank =
            questionBankRepository.findQuestionBankByIdQuestionBank(quiz.getIdQuestionBank());

        if (report.getScore() == null) {
            StartQuizResponse startQuizResponse = new StartQuizResponse();
            startQuizResponse.setTitle(report.getTitleQuiz());
            startQuizResponse.setDescription(report.getDescriptionQuiz());
            startQuizResponse.setDuration(report.getQuizDuration());
            startQuizResponse.setStartDate(quiz.getStartDate());
            List<Question> questionList = new ArrayList<>();
            for (String idQuestion : questionBank.getListIdQuestion()) {
                Question question = questionRepository.findQuestionByIdQuestion(idQuestion);
                question.setAnswerKey(null);
                questionList.add(question);
            }
            Collections.shuffle(questionList);
            startQuizResponse.setQuestionList(questionList);
            return new ResponseEntity<>(startQuizResponse, HttpStatus.OK);
        } else {
            ReportQuizResponse reportQuizResponse = new ReportQuizResponse();
            reportQuizResponse.setTitle(report.getTitleQuiz());
            reportQuizResponse.setDescription(report.getDescriptionQuiz());
            reportQuizResponse.setDuration(report.getQuizDuration());
            reportQuizResponse.setQuizDate(report.getQuizDate());
            reportQuizResponse.setAssignAt(report.getAssignedAt());
            reportQuizResponse.setScore(report.getScore());
            List<ReportQuestion> reportQuestionList = new ArrayList<>();
            for (AnsweredQuestion answeredQuestion : report.getAnsweredQuestionList()) {
                ReportQuestion reportQuestion = new ReportQuestion();
                QuestionFulfilled question = questionFulfilledRepository
                    .findQuestionByIdQuestion(answeredQuestion.getIdQuestionFulfilled());
                reportQuestion.setAnswer(question.getAnswer());
                reportQuestion.setAnswerKey(question.getAnswerKey());
                reportQuestion.setIdQuestionFulfilled(question.getIdQuestion());
                reportQuestion.setStudentAnswer(answeredQuestion.getStudentAnswer());
                reportQuestion.setQuestion(question.getQuestion());
                reportQuestion.setQuestionType(question.getQuestionType());
                reportQuestion.setScore(question.getScore());
                reportQuestion.setStudentScore(answeredQuestion.getStudentScore());
                reportQuestion.setRatioMap(answeredQuestion.getRatioMap());
                reportQuestionList.add(reportQuestion);

            }

            reportQuizResponse.setReportQuizResponses(reportQuestionList);
            return new ResponseEntity<>(reportQuizResponse, HttpStatus.OK);
        }
    }

    @Override public ResponseEntity submitQuiz(String fcm, String id_quiz,
        Map<Integer, AnsweredQuestion> answeredQuestion, Principal principal) throws IOException {
        User user = userRepository.findByEmail(principal.getName());
        Report report = reportRepository.findReportByIdQuizAndIdStudent(id_quiz, user.getIdUser());
        Quiz quiz = quizRepository.findQuizByIdQuiz(id_quiz);

        report.setAssignedAt(Calendar.getInstance().getTimeInMillis());
        reportRepository.save(report);

        List<AnsweredQuestion> answeredQuestionList = new ArrayList<>();

        for (Map.Entry<Integer, AnsweredQuestion> entry : answeredQuestion.entrySet()) {
            answeredQuestionList.add(new AnsweredQuestion(entry.getValue().getIdQuestionFulfilled(),
                entry.getValue().getStudentAnswer(), 0, null));
        }
        report.setAnsweredQuestionList(answeredQuestionList);

        //Save question to fulfilled
        if (questionFulfilledRepository
            .findQuestionByIdQuestion(answeredQuestionList.get(0).getIdQuestionFulfilled())
            == null) {
            QuestionBank questionBank =
                questionBankRepository.findQuestionBankByIdQuestionBank(quiz.getIdQuestionBank());
            for (String idQuestion : questionBank.getListIdQuestion()) {
                Question question = questionRepository.findQuestionByIdQuestion(idQuestion);
                questionFulfilledRepository.save(
                    new QuestionFulfilled(question.getIdQuestion(), question.getQuestion(),
                        question.getAnswer(), question.getQuestionType(), question.getAnswerKey(),
                        question.getSteamedAnswerKey(), question.getScore(),
                        question.getSynonymAnswerKey()));
            }
        }
        checkScore(report);
        FcmClient fcmClient;
        fcmClient = new FcmClient();
        try {
            fcmClient.sendPushNotification(fcm, user.getName());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(new SimpleCustomResponse(200, "Report created"), HttpStatus.OK);
    }

    private void checkScore(Report report) throws IOException {
        Integer score = 0;
        List<AnsweredQuestion> answeredQuestionList = new ArrayList<>();
        for (AnsweredQuestion answeredQuestion : report.getAnsweredQuestionList()) {
            QuestionFulfilled question = questionFulfilledRepository.findQuestionByIdQuestion(answeredQuestion.getIdQuestionFulfilled());
            if (question.getQuestionType().equals("MULTIPLECHOICE")) {
                if (question.getAnswerKey().equals(answeredQuestion.getStudentAnswer())) {
                    score += question.getScore();
                    answeredQuestion.setStudentScore(question.getScore());
                }
                answeredQuestionList.add(answeredQuestion);
            } else {
                //COSINE SIMILARITY
                int vectorD = 0;
                int vectorQ = 0;
                int sumVector = 0;

                List<String> splittedAnswerKey = Arrays.asList(question.getSteamedAnswerKey());
                //http://www.regular-expressions.info/posixbrackets.html
                String[] splittedAnswer = answeredQuestion.getStudentAnswer().toLowerCase()
                    .split("\\P{Alpha}+"); //Tokenizing and case folding
                List<String> formattedAnswer = new ArrayList<>();

                for (String s : splittedAnswer) {
                    if (!FieldName.Constants.STOPWORD.matches(".*\\b" + s + "\\b.*")) { //check if word is stopword or not
                        formattedAnswer.add(stemmer.steam(s)); //stemming
                    }
                }

                //change answer word with main word of synonyms
                HashMap<String, List<String>> replaceForSynonyms = new HashMap<>();

                setupSynonyms(question, formattedAnswer, replaceForSynonyms);
                setupSynonyms(question, splittedAnswerKey, replaceForSynonyms);


                HashMap<String, Integer> splittedAnswerKeyMap = new HashMap<>();
                HashMap<String, Integer> splittedAnswerMap = new HashMap<>();

                for (String s : splittedAnswerKey) {
                    incrementValue(splittedAnswerKeyMap, s);
                    setValueZero(splittedAnswerMap, s);
                }

                for (String s : formattedAnswer) {
                    incrementValue(splittedAnswerMap, s);
                    setValueZero(splittedAnswerKeyMap, s);
                }

                HashMap<String, Ratio> ratioHashMap = new HashMap<>();
                for (Map.Entry<String, Integer> entry : splittedAnswerMap.entrySet()) {
                    String key = entry.getKey();
                    Integer value = entry.getValue();
                    int valueAnswerKey = splittedAnswerKeyMap.get(key);
                    vectorD += value * value;
                    vectorQ += valueAnswerKey * valueAnswerKey;
                    sumVector += value * valueAnswerKey;
                    ratioHashMap.put(entry.getKey(), new Ratio(valueAnswerKey, value));
                    for (HashMap.Entry<String, List<String>> synonyms : replaceForSynonyms
                        .entrySet()) {
                        if (synonyms.getKey().equals(entry.getKey())) {
                            ratioHashMap.remove(key);
                            ratioHashMap.put(synonyms.getKey() + " " + synonyms.getValue(),
                                new Ratio(valueAnswerKey, value));
                        }
                    }
                }
                int baseScore = (int) Math.ceil(question.getScore() * Math
                    .ceil(sumVector / (Math.sqrt(vectorD) * Math.sqrt(vectorQ)) * 100) / 100);

                if (baseScore > question.getScore()) {
                    baseScore = question.getScore();
                }
                score += baseScore;
                answeredQuestion.setStudentScore(baseScore);

                answeredQuestion.setRatioMap(ratioHashMap);
                answeredQuestionList.add(answeredQuestion);
            }
        }
        report.setAnsweredQuestionList(answeredQuestionList);
        report.setScore(score);
        reportRepository.save(report);
    }

    private void setupSynonyms(QuestionFulfilled question, List<String> splittedAnswerKey,
        HashMap<String, List<String>> replaceForSynonyms) {
        for (int j = 0; j < splittedAnswerKey.size(); j++) {
            for (int i = 0; i < question.getSynonymAnswerKey().size(); i++) {
                if (question.getSynonymAnswerKey().get(i) != null && question.getSynonymAnswerKey()
                    .get(i).getSynonyms().contains(splittedAnswerKey.get(j))
                    || question.getSynonymAnswerKey().get(i) != null && question
                    .getSynonymAnswerKey().get(i).getMainWord().equals(splittedAnswerKey.get(j))) {
                    List<String> mapValueReplaceSynonyms =
                        replaceForSynonyms.values().stream().flatMap(Collection::stream)
                            .collect(Collectors.toList());
                    if (replaceForSynonyms.containsKey(splittedAnswerKey.get(j))) {
                        //do nothing
                    } else if (mapValueReplaceSynonyms.contains(splittedAnswerKey.get(j))) {
                        String synonym = splittedAnswerKey.get(j);
                        int finalJ1 = j;
                        replaceForSynonyms.forEach((key, value) -> {
                            if (value.contains(synonym)) {
                                splittedAnswerKey.set(finalJ1, key);
                            }
                        });
                    } else {
                        if (question.getSynonymAnswerKey().get(i).getMainWord()
                            .equals(splittedAnswerKey.get(j))) {
                            for (String synonymAnswerKey : question.getSynonymAnswerKey().get(i)
                                .getSynonyms()) {
                                if (replaceForSynonyms.containsKey(synonymAnswerKey)) {
                                    List<String> list = replaceForSynonyms.get(synonymAnswerKey);
                                    if (list != null) {
                                        if (!list.contains(splittedAnswerKey.get(j))) {
                                            list.add(splittedAnswerKey.get(j));
                                            replaceForSynonyms.put(synonymAnswerKey, list);
                                        }
                                    }
                                    splittedAnswerKey.set(j, synonymAnswerKey);
                                } else if (mapValueReplaceSynonyms.contains(synonymAnswerKey)) {
                                    int finalJ2 = j;
                                    replaceForSynonyms.forEach((key, value) -> {
                                        if (value.contains(synonymAnswerKey)) {
                                            List<String> list = replaceForSynonyms.get(key);
                                            if (list != null) {
                                                if (!list.contains(synonymAnswerKey)) {
                                                    list.add(synonymAnswerKey);
                                                    replaceForSynonyms.put(synonymAnswerKey, list);
                                                }
                                            } else {
                                                replaceForSynonyms
                                                    .put(synonymAnswerKey, new ArrayList<>());
                                            }
                                            splittedAnswerKey.set(finalJ2, key);
                                        }
                                    });
                                }
                            }
                        } else {
                            if (replaceForSynonyms.containsKey(splittedAnswerKey.get(j))) {
                                List<String> list =
                                    replaceForSynonyms.get(splittedAnswerKey.get(j));
                                setSynonymMapValue(splittedAnswerKey, replaceForSynonyms, j, list);
                                splittedAnswerKey.set(j, splittedAnswerKey.get(j));
                            } else if (mapValueReplaceSynonyms.contains(splittedAnswerKey.get(j))) {
                                int finalJ = j;
                                replaceForSynonyms.forEach((key, value) -> {
                                    if (value.contains(splittedAnswerKey.get(finalJ))) {
                                        List<String> list = replaceForSynonyms.get(key);
                                        setSynonymMapValue(splittedAnswerKey, replaceForSynonyms,
                                            finalJ, list);
                                        splittedAnswerKey.set(finalJ, key);
                                    }
                                });
                            } else {
                                List<String> list = replaceForSynonyms
                                    .get(question.getSynonymAnswerKey().get(i).getMainWord());
                                if (list != null) {
                                    if (!list.contains(splittedAnswerKey.get(j))) {
                                        list.add(splittedAnswerKey.get(j));
                                        replaceForSynonyms.put(
                                            question.getSynonymAnswerKey().get(i).getMainWord(),
                                            list);
                                    }
                                } else {
                                    replaceForSynonyms
                                        .put(splittedAnswerKey.get(j), new ArrayList<>());
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void setSynonymMapValue(List<String> formattedAnswer,
        HashMap<String, List<String>> replaceForSynonyms, int finalJ, List<String> list) {
        if (list != null) {
            if (!list.contains(formattedAnswer.get(finalJ))) {
                list.add(formattedAnswer.get(finalJ));
                replaceForSynonyms.put(formattedAnswer.get(finalJ), list);
            }
        } else {
            replaceForSynonyms.put(formattedAnswer.get(finalJ), new ArrayList<>());
        }
    }
}


      /*  Tesaurus tesaurus = new Tesaurus();
        tesaurus.setMainWord(splittedAnswerKey[1]);
        Set<String> synonyms = new TreeSet<>();
        synonyms.add(splittedAnswerKey[2]);
        synonyms.add(splittedAnswerKey[3]);
        synonyms.add(splittedAnswerKey[1]);
        tesaurus.setSynonyms(synonyms);
        tesaurusRepository.save(tesaurus);*/


//        synonymChecker(splittedAnswerKey);


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

