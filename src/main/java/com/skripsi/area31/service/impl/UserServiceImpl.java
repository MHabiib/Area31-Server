package com.skripsi.area31.service.impl;

import com.skripsi.area31.config.MongoTokenStore;
import com.skripsi.area31.model.course.*;
import com.skripsi.area31.model.qna.QnaComment;
import com.skripsi.area31.model.qna.QnaReplies;
import com.skripsi.area31.model.response.GetProfileCustomResponse;
import com.skripsi.area31.model.response.InstructorDashboardResponse;
import com.skripsi.area31.model.response.SimpleCustomResponse;
import com.skripsi.area31.model.user.User;
import com.skripsi.area31.repository.*;
import com.skripsi.area31.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.*;

@Service public class UserServiceImpl implements UserService {
    @Autowired UserRepository userRepository;
    @Autowired CourseRepository courseRepository;
    @Autowired ComplaintRepository complaintRepository;
    @Autowired QuestionBankRepository questionBankRepository;
    @Autowired MongoTokenStore mongoTokenStore;
    @Autowired QnaCommentRepository qnaCommentRepository;
    @Autowired QnaRepliesRepository qnaRepliesRepository;
    @Autowired ReportRepository reportRepository;
    @Autowired PasswordEncoder passwordEncoder;

    @Qualifier("getJavaMailSender") @Autowired JavaMailSender emailSender;

    static void setupComplainDetails(List<ComplaintResponse> complaintResponseList,
        List<Complaint> complaintList, ReportRepository reportRepository) {
        for (Complaint complaint : complaintList) {
            Report report = reportRepository.findReportByIdReport(complaint.getIdReport());
            if (report != null) {
                complaintResponseList.add(
                    new ComplaintResponse(complaint.getIdComplaint(), complaint.getQuizTitle(),
                        complaint.getStatus(), complaint.getDescription(), complaint.getReason(),
                        report.getQuizDate(), report.getAssignedAt(), report.getScore()));
            }
        }
    }

    @Override public ResponseEntity createUser(User user) {
        if (userRepository.findByEmail(user.getEmail()) == null) {
            if (user.getRole().equals("ROLE_STUDENT")) {
                user.setIdCourse(new HashSet<>());
            }
            userRepository.save(user);
            return new ResponseEntity<>(new SimpleCustomResponse(200, "Create user successful !"),
                HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new SimpleCustomResponse(400, "Email already registered !"),
                HttpStatus.BAD_REQUEST);
        }
    }

    @Override public ResponseEntity updateUser(User user, Principal principal, String newPassword) {
        if (null != userRepository.findByEmail(user.getEmail())) {
            if (null != userRepository.findByEmail(user.getEmail()) && !user.getEmail()
                .equals(principal.getName())) {
                return new ResponseEntity<>(
                    new SimpleCustomResponse(400, "Email already registered !"),
                    HttpStatus.BAD_REQUEST);
            }
        }
        User userExist = userRepository.findByEmail(principal.getName());
        if (!"".equals(newPassword) && null != newPassword && !newPassword.equals("\"\"")) {
            if (!"".equals(user.getPassword()) && null != user.getPassword()) {
                if (passwordEncoder.matches(user.getPassword(), userExist.getPassword())) {
                    userExist.setPassword(passwordEncoder.encode(newPassword));
                    userRepository.save(userExist);
                    return new ResponseEntity<>(
                        new SimpleCustomResponse(200, "Success update password"), HttpStatus.OK);
                } else
                    return new ResponseEntity<>(new SimpleCustomResponse(400, "Password not match"),
                        HttpStatus.BAD_REQUEST);
            } else
                return new ResponseEntity<>(
                    new SimpleCustomResponse(400, "Password can't be empty"),
                    HttpStatus.BAD_REQUEST);
        } else if (userExist == null) {
            return new ResponseEntity<>(new SimpleCustomResponse(400, "User not found"),
                HttpStatus.BAD_REQUEST);
        } else {
            return updateUser2ndStep(user, userExist);
        }
    }

    @Override public ResponseEntity isAuthorize(String role, Principal principal) {
        User userExist = userRepository.findByEmail(principal.getName());
        if (userExist.getRole().equals(role)) {
            return new ResponseEntity<>(new SimpleCustomResponse(200, "Authorized"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new SimpleCustomResponse(400, "Unauthorized"),
                HttpStatus.BAD_REQUEST);
        }
    }

    private ResponseEntity updateUser2ndStep(User user, User userExist) {
        String message = "Update user successful !";
        userExist.setPhone(user.getPhone());
        if (!user.getEmail().equals(userExist.getEmail()) && user.getEmail() != null && !user
            .getEmail().equals("")) {
            mongoTokenStore.revokeToken(user.getEmail());
            userExist.setEmail(user.getEmail());
            message = "Update user successful with email changing!";
        }
        if (!user.getName().equals(userExist.getName())) {
            if (userExist.getRole().equals("ROLE_INSTRUCTOR")) {
                List<Course> courseList =
                    courseRepository.findCourseByIdInstructor(userExist.getIdUser());
                for (Course course : courseList) {
                    course.setInstructorName(user.getName());
                    courseRepository.save(course);
                }
            }
            List<QnaComment> qnaCommentList =
                qnaCommentRepository.findAllByIdUser(userExist.getIdUser());
            List<QnaReplies> qnaRepliesList =
                qnaRepliesRepository.findAllByIdUser(userExist.getIdUser());
            for (QnaComment qnaComment : qnaCommentList) {
                qnaComment.setName(user.getName());
                qnaCommentRepository.save(qnaComment);
            }
            for (QnaReplies qnaReplies : qnaRepliesList) {
                qnaReplies.setName(user.getName());
                qnaRepliesRepository.save(qnaReplies);
            }
        }
        userExist.setName(user.getName());
        userRepository.save(userExist);
        return new ResponseEntity<>(new SimpleCustomResponse(200, message), HttpStatus.OK);
    }

    @Override public ResponseEntity getUser() {
        ResponseEntity response;
        response = ResponseEntity.ok(userRepository.findAll());
        return response;
    }

    @Override public ResponseEntity logout(Principal principal) {
        mongoTokenStore.revokeToken(principal.getName());
        return new ResponseEntity<>("Logout successful !", HttpStatus.OK);
    }

    @Override public ResponseEntity getProfile(Principal principal) {
        User user = userRepository.findByEmail(principal.getName());
        if (user != null) {
            user.setPassword("********");
            return new ResponseEntity<>(new GetProfileCustomResponse(200, user), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new GetProfileCustomResponse(400, "User not found"),
                HttpStatus.NOT_FOUND);
        }
    }

    @Override public ResponseEntity forgotPassword(String email) throws MessagingException {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            int code;
            if (user.getResetCode() == null) {
                code = new Random().nextInt(900000) + 100000;
                user.setResetCode(code);
                userRepository.save(user);
                new Timer().schedule(new TimerTask() {
                    @Override public void run() {
                        user.setResetCode(null);
                        userRepository.save(user);
                    }
                }, 300000);
            } else {
                code = user.getResetCode();
            }
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setFrom("noreply@area31.com");
            helper.setTo(user.getEmail());
            helper.setSubject("Area 31: Reset Password");
            helper.setText("<html><body>"
                    + "<img src=\"https://s3.ap-southeast-1.amazonaws.com/pms-future/UPLOADbyAPI - ujol_logo.png\"  width=\"178\" height=\"106\" style=\"border:0px;\">"
                    + "<tr><td style=\"padding:15px;\"><p>Halo " + user.getName()
                    + "<br><br>Forgotten your password? No problem. Simply input this code within 5 minutes on your apps!"
                    + "<br><br><b>" + code + "</b>"
                    + "<br><br>This email is part of our procedure to create a new password. If you don’t need to change your password then please ignore this email and use your existing password to sign in.<br><br><br><br>Regards,<br>Area31 Apps</p></td></tr></body></html>",
                true);
            emailSender.send(message);
            return new ResponseEntity<>(
                new SimpleCustomResponse(200, "Success request reset password"), HttpStatus.OK);
        }
        return new ResponseEntity<>(new SimpleCustomResponse(400, "Email not found"),
            HttpStatus.BAD_REQUEST);
    }

    @Override public ResponseEntity forgotPasswordNextStep(String email, Integer code) {
        User user = userRepository.findByEmail(email);
        if (user.getResetCode() != null && user.getResetCode().equals(code)) {
            return new ResponseEntity<>(new SimpleCustomResponse(200, "Success, the code correct"),
                HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new SimpleCustomResponse(400, "Incorrect code !"),
                HttpStatus.BAD_REQUEST);
        }
    }

    @Override public ResponseEntity resetPassword(String email, String password, Integer code) {
        User user = userRepository.findByEmail(email);
        if (user.getResetCode() != null && user.getResetCode().equals(code)) {
            user.setResetCode(null);
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);
            mongoTokenStore.revokeToken(user.getEmail());
            return new ResponseEntity<>(new SimpleCustomResponse(200, "Success reset password"),
                HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new SimpleCustomResponse(400, "Incorrect code !"),
                HttpStatus.BAD_REQUEST);
        }
    }

    @Override public ResponseEntity instructorDashboard(Principal principal) {
        User user = userRepository.findByEmail(principal.getName());
        List<ComplaintResponseDashboard> complaintResponseDashboardList = new ArrayList<>();
        List<Complaint> complaintList =
            complaintRepository.findTop5ByIdInstructor(user.getIdUser());

        for (Complaint complaint : complaintList) {
            Report report = reportRepository.findReportByIdReport(complaint.getIdReport());
            if (report != null) {
                complaintResponseDashboardList.add(
                    new ComplaintResponseDashboard(complaint.getIdComplaint(),
                        complaint.getQuizTitle(), complaint.getStatus(),
                        userRepository.findByIdUser(complaint.getIdUser()).getName(),
                        courseRepository.findCourseByIdCourse(complaint.getIdCourse()).getName(),
                        complaint.getDescription(), complaint.getReason(), report.getQuizDate(),
                        report.getAssignedAt(), report.getScore()));
            }
        }

        return ResponseEntity.ok(new InstructorDashboardResponse(
            courseRepository.findTop5ByIdInstructorAndStatus(user.getIdUser(), "AVAILABLE"),
            questionBankRepository.findTop5ByIdInstructor(user.getIdUser()),
            complaintResponseDashboardList));
    }

    @Override public ResponseEntity email(String name, String email, String message)
        throws MessagingException, UnsupportedEncodingException {
        MimeMessage mime = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mime);
        helper.setFrom("noreply@hidev.com");
        helper.setTo("mhabibofficial@yahoo.com");
        mime.addRecipients(Message.RecipientType.CC, InternetAddress.parse("andriwinanda1@gmail.com"));
        mime.setFrom(new InternetAddress("noreply@hidev.com", "HiDev"));
        helper.setSubject("HiDev: Contact");
        helper.setText("<html><body>"
                + "<img src=\"https://i.ibb.co/V9wm9qm/hidev-logo.png\" style=\"border:0px;\">"
                + "<tr><td style=\"padding:15px;\"><p>Halo I'm " + name + " (" + email + ")"
                + "<br>Message:<br>" + message + "<br><br><br><br>Regards,<br>HiDev Apps</p></td></tr></body></html>",
            true);
        emailSender.send(mime);
        return new ResponseEntity<>(new SimpleCustomResponse(200, "Email sent "),
            HttpStatus.OK);
    }

}
