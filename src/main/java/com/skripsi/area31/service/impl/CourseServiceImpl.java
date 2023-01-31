package com.skripsi.area31.service.impl;

import com.skripsi.area31.AmazonClient;
import com.skripsi.area31.model.course.*;
import com.skripsi.area31.model.qna.QnaPost;
import com.skripsi.area31.model.response.CheckCustomResponse;
import com.skripsi.area31.model.response.CourseDetailsResponse;
import com.skripsi.area31.model.response.CourseResourceResponse;
import com.skripsi.area31.model.response.SimpleCustomResponse;
import com.skripsi.area31.model.user.User;
import com.skripsi.area31.repository.*;
import com.skripsi.area31.service.CourseService;
import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.*;

@Service(value = "courseService") public class CourseServiceImpl implements CourseService {
    @Autowired CourseRepository courseRepository;
    @Autowired UserRepository userRepository;
    @Autowired ReportRepository reportRepository;
    @Autowired QuizRepository quizRepository;
    @Autowired QnaPostRepository qnaPostRepository;
    @Autowired PasswordEncoder passwordEncoder;
    @Autowired AmazonClient amazonClient;

    static void setReport(Course course, Quiz quiz, Report report,
        ReportRepository reportRepository) {
        report.setIdQuiz(quiz.getIdQuiz());
        report.setQuizDate(quiz.getStartDate());
        report.setQuizDuration(quiz.getDuration());
        report.setTitleQuiz(quiz.getTitle());
        report.setDescriptionQuiz(quiz.getDescription());
        report.setIdCourse(course.getIdCourse());
        reportRepository.save(report);
    }

    @Override public ResponseEntity createCourse(Course course, Principal principal)
        throws IOException {
        User user = userRepository.findByEmail(principal.getName());

        course.setIdInstructor(user.getIdUser());
        course.setInstructorName(user.getName());
        if (courseRepository.findCourseByCourseId(course.getCourseId()) != null) {
            return new ResponseEntity<>(new SimpleCustomResponse(400, "Course ID already taken"),
                HttpStatus.BAD_REQUEST);
        } else {
            course.setCourseId(course.getCourseId());
            if (course.getCoursePassword() == null) {
                course.setCoursePassword(principal.getName());
            } else {
                course.setCoursePassword(course.getCoursePassword());
            }
            course.setIdStudent(new HashSet<>());
            courseRepository.save(course);
            course.setQr(
                generateQR(course.getCourseId(), course.getIdInstructor(), course.getIdCourse()));
            courseRepository.save(course);
            return new ResponseEntity<>(new SimpleCustomResponse(200, course.getIdCourse()),
                HttpStatus.OK);
        }
    }

    @Override public ResponseEntity updateCourse(Course course, Principal principal)
        throws IOException {
        Course courseExist = courseRepository.findCourseByIdCourse(course.getIdCourse());

        if (courseRepository.findCourseByCourseId(course.getCourseId()) != null) {
            return new ResponseEntity<>(new SimpleCustomResponse(400, "Course ID already taken"),
                HttpStatus.BAD_REQUEST);
        } else {
            if (course.getCourseId() != null && !course.getCourseId()
                .equals(courseExist.getCourseId())) {
                courseExist.setCourseId(course.getCourseId());
                courseExist.setQr(generateQR(course.getCourseId(), courseExist.getIdInstructor(),
                    courseExist.getIdCourse()));
            }
            if (course.getCoursePassword() != null) {
                courseExist.setCoursePassword(course.getCoursePassword());
            }
            if (course.getStatus() != null) {
                courseExist.setStatus(course.getStatus());
            }
            if (course.getName() != null) {
                if (!course.getName().equals(courseExist.getName())) {
                    List<QnaPost> qnaPostList =
                        qnaPostRepository.findAllByIdCourse(course.getIdCourse());
                    for (QnaPost qnaPost : qnaPostList) {
                        qnaPost.setCourseName(course.getName());
                        qnaPostRepository.save(qnaPost);
                    }
                }
                courseExist.setName(course.getName());
            }
            courseRepository.save(courseExist);
            return ResponseEntity.ok(courseExist);
        }
    }

    @Override public ResponseEntity archiveCourse(String id_course, Principal principal) {
        Course course = courseRepository.findCourseByIdCourse(id_course);
        course.setStatus("UNAVAILABLE");
        for (String idStudent : course.getIdStudent()) {
            User user = userRepository.findByIdUser(idStudent);
            user.getIdCourse().remove(course.getIdCourse());
            user.setIdCourse(user.getIdCourse());
        }
        course.setIdStudent(new HashSet<>());
        courseRepository.save(course);
        return ResponseEntity.ok("Course archived !");
    }

    private String generateQR(String courseId, String idInstructor, String idCourse)
        throws IOException {
        String filename;
        ByteArrayOutputStream bout =
            QRCode.from("QR" + courseId).withSize(250, 250).to(ImageType.PNG).stream();
        filename =
            "QR" + idInstructor.replaceAll("\\s+", "") + "-" + idCourse.replaceAll("\\s+", "")
                + ".png";
        filename = amazonClient.convertMultiPartToFileQR(bout, filename);
        return filename;
    }

    @Override public ResponseEntity getAllCourse(Principal principal) {
        User user = userRepository.findByEmail(principal.getName());
        return new ResponseEntity<>(courseRepository.findCourseByIdInstructor(user.getIdUser()),
            HttpStatus.OK);
    }

    @Override public ResponseEntity loadListCourse(Integer page, Principal principal) {
        User user = userRepository.findByEmail(principal.getName());
        PageRequest request = PageRequest.of(page, 10, Sort.by(Sort.Direction.ASC, "name"));
        return ResponseEntity.ok(courseRepository.findAllByIdStudent(user.getIdUser(), request));
    }

    @Override public ResponseEntity checkCourse(String courseId, Principal principal) {
        User user = userRepository.findByEmail(principal.getName());
        Course course = courseRepository.findCourseByCourseId(courseId);
        if (course == null) {
            return new ResponseEntity<>(new CheckCustomResponse(400, "Course not found"),
                HttpStatus.BAD_REQUEST);
        } else if (course.getIdStudent().contains(user.getIdUser())) {
            return new ResponseEntity<>(new CheckCustomResponse(400, "You already in this course"),
                HttpStatus.BAD_REQUEST);
        } else {
            course.setIdCourse(null);
            course.setIdInstructor(null);
            course.setIdStudent(null);
            course.setCoursePassword(null);
            return new ResponseEntity<>(new CheckCustomResponse(200, course), HttpStatus.OK);
        }
    }

    @Override public ResponseEntity courseDetails(String idCourse, Principal principal) {
        User user = userRepository.findByEmail(principal.getName());
        Course course = courseRepository.findCourseByIdCourse(idCourse);
        return new ResponseEntity<>(new CourseDetailsResponse(200,
            new CourseDetails(course.getName(), course.getDescription(), course.getInstructorName(),
                course.getIdStudent().size(), user.getIdUser())), HttpStatus.OK);
    }

    @Override public ResponseEntity courseDetailsInstructor(String idCourse, Principal principal) {
        return new ResponseEntity<>(
            new CheckCustomResponse(200, courseRepository.findCourseByIdCourse(idCourse)),
            HttpStatus.OK);
    }

    @Override public ResponseEntity leaveCourse(String idCourse, Principal principal) {
        Course course = courseRepository.findCourseByIdCourse(idCourse);
        User user = userRepository.findByEmail(principal.getName());
        if (course.getIdStudent().contains(user.getIdUser())) {
            exitCourse(course, user);
            return new ResponseEntity<>(new SimpleCustomResponse(200, "Success leave this course"),
                HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new SimpleCustomResponse(400, "You are not on this course"),
                HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity uploadResource(Principal principal, MultipartFile file, String idCourse) {
        Course course = courseRepository.findCourseByIdCourse(idCourse);
        if (course.getResources() == null) {
            course.setResources(new HashMap<>());
        }
        HashMap<String, String> resources = course.getResources();
        if (checkIsFile(file) && file.getOriginalFilename() != null) {
            String filename = file.getOriginalFilename().replace(".", " - ");
            if (course.getResources().containsKey(filename)) {
                return new ResponseEntity<>(
                    new SimpleCustomResponse(400, "File name already registered! use unique one"),
                    HttpStatus.BAD_REQUEST);
            }
            resources.put(filename,
                amazonClient.uploadFile(file, file.getOriginalFilename()));
            course.setResources(resources);
            courseRepository.save(course);
            return new ResponseEntity<>(new SimpleCustomResponse(200, "Success upload the file"),
                HttpStatus.OK);
        } else {
            return new ResponseEntity<>(
                new SimpleCustomResponse(400, "Some error occurred. Failed to upload file"),
                HttpStatus.BAD_REQUEST);
        }
    }

    @Override public ResponseEntity courseResource(String idCourse, Principal principal) {
        Course course = courseRepository.findCourseByIdCourse(idCourse);
        if (course.getResources() == null) {
            course.setResources(new HashMap<>());
        }
        List<Resource> resources = new ArrayList<>();
        for (Map.Entry resource : course.getResources().entrySet()) {
            resources
                .add(new Resource(resource.getKey().toString(), resource.getValue().toString()));
        }
        resources.sort(Comparator.comparing(Resource::getFileName));
        return new ResponseEntity<>(
            new CourseResourceResponse(200, "Success get resources", resources), HttpStatus.OK);
    }

    @Override public ResponseEntity uploadFile(MultipartFile file) {
        return new ResponseEntity<>(new SimpleCustomResponse(200,
            amazonClient.uploadFile(file, "UPLOADbyAPI - " + file.getOriginalFilename())),
            HttpStatus.OK);
    }

    @Override public ResponseEntity courseMembers(String idCourse, Principal principal) {
        Course course = courseRepository.findCourseByIdCourse(idCourse);
        List<Student> studentList = new ArrayList<>();
        for (String idStudent : course.getIdStudent()) {
            studentList
                .add(new Student(idStudent, userRepository.findByIdUser(idStudent).getName()));
        }
        return ResponseEntity.ok(studentList);
    }

    @Override public ResponseEntity deleteMember(String idUser, String idCourse) {
        Course course = courseRepository.findCourseByIdCourse(idCourse);
        User user = userRepository.findByIdUser(idUser);
        if (course.getIdStudent().contains(user.getIdUser())) {
            exitCourse(course, user);
            return new ResponseEntity<>(new SimpleCustomResponse(200, "Success remove user"),
                HttpStatus.OK);
        } else {
            return new ResponseEntity<>(
                new SimpleCustomResponse(400, "User are not on this course"),
                HttpStatus.BAD_REQUEST);
        }
    }

    @Override public ResponseEntity deleteResource(String fileName, String idCourse) {
        Course course = courseRepository.findCourseByIdCourse(idCourse);
        amazonClient.deleteFile(course.getResources().get(fileName).substring(51));
        course.getResources().remove(fileName);
        course.setResources(course.getResources());
        courseRepository.save(course);
        return new ResponseEntity<>(new SimpleCustomResponse(200, "Success delete resource"),
            HttpStatus.OK);
    }

    private void exitCourse(Course course, User user) {
        course.getIdStudent().remove(user.getIdUser());
        user.getIdCourse().remove(course.getIdCourse());
        courseRepository.save(course);
        userRepository.save(user);
        List<Report> reportList = reportRepository
            .findReportByIdStudentAndIdCourseAndAssignedAtIsNull(user.getIdUser(),
                course.getIdCourse());
        for (Report report : reportList) {
            reportRepository.delete(report);
        }
    }

    private boolean checkIsFile(MultipartFile file) {
        boolean isImage = false;
        if (null != file && !StringUtils.isEmpty(file.getOriginalFilename())) {
            isImage = true;
        }
        return isImage;
    }

    @Override
    public ResponseEntity joinCourse(String courseId, String password, Principal principal) {
        User user = userRepository.findByEmail(principal.getName());
        Course course = courseRepository.findCourseByCourseId(courseId);
        if (course == null) {
            return new ResponseEntity<>(new SimpleCustomResponse(400, "Course not found"),
                HttpStatus.BAD_REQUEST);
        } else if (course.getIdStudent().contains(user.getIdUser())) {
            return new ResponseEntity<>(new SimpleCustomResponse(400, "You already in this course"),
                HttpStatus.BAD_REQUEST);
        } else if (course.getStatus().equals("UNAVAILABLE")) {
            return new ResponseEntity<>(
                new SimpleCustomResponse(400, "This course status in unavailable"),
                HttpStatus.BAD_REQUEST);
        } else if (!course.getCoursePassword().equals(password)) {
            return new ResponseEntity<>(new SimpleCustomResponse(400, "Wrong course password"),
                HttpStatus.BAD_REQUEST);
        } else {
            Set<String> listIdStudent = course.getIdStudent();
            Set<String> listIdCourse = user.getIdCourse();
            listIdStudent.add(user.getIdUser());
            listIdCourse.add(course.getIdCourse());
            userRepository.save(user);
            courseRepository.save(course);
            List<Quiz> quizList = quizRepository.findQuizByIdCourse(course.getIdCourse());
            for (Quiz quiz : quizList) {
                if (reportRepository
                    .findReportByIdQuizAndIdStudent(quiz.getIdQuiz(), user.getIdUser()) == null) {
                    Report report = new Report();
                    report.setIdStudent(user.getIdUser());
                    setReport(course, quiz, report, reportRepository);
                }
            }
            return new ResponseEntity<>(new SimpleCustomResponse(200, "Success join course"),
                HttpStatus.OK);
        }
    }
}
