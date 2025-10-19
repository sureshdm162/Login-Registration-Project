package com.credit.userms.service;

import com.credit.userms.repository.UserRepository;
import com.credit.userms.service.impl.UserServiceImpl;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@Getter
@Setter
public class EmailService {
    private final JavaMailSender mailSender;
    private UserRepository userRepository;
    private final String from = "accm47216@gmail.com";
    @Autowired
    UserServiceImpl userServiceImpl;
    @Value("${server.port}")
    private String portNumber;

    @Autowired
    public EmailService(JavaMailSender mailSender){
         this.mailSender=mailSender;
    }

    public String sendRegistrationEmail(String email,String username){
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper= new MimeMessageHelper(message,true);// true = multipart, meaning used for embedding images or attachmentsv .
            String to = email;
            String subject = "Dear "+username+", Let's get you started";
            String body = "<html>"
                    + "<body>"
                    + "<h2>Welcome to Vontobel family!</h2>"
                    + "<img src='cid:logoImage' alt='Logo' style='width:100%; height:auto;'/><br/>"
                    + "<p>Your registration was successful. We are glad to have you onboard!</p><br>"
                    + "<p>We will send you further steps soon!</p><br>"
                    +"<p>Regards,</p>"
                    +"<p>John</p>"
                    +"<p>Senior Executive</p>"
                    +"<p>Vontobel</p>"
                    + "</body>"
                    + "</html>";
            //Set mail details with image.
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body,true);// true = enable HTML
            helper.setFrom(from);

            // Embed image using Content-ID
            File imageFile = new File("src/main/resources/images/vontobelLogo.png");
            if (imageFile.exists()) {
                FileSystemResource image = new FileSystemResource(imageFile);
                helper.addInline("logoImage", image); // 'logoImage' must match cid:logoImage
            } else {
                System.out.println("Image not found: " + imageFile.getAbsolutePath());
            }
            mailSender.send(message);     // 🔹 Actually sends the email
            return "User registration is successful!";
        }catch (Exception e){
            e.getStackTrace();
            return "Unfortunate,registration is incomplete";
        }
    }

    public String sendForgotPasswordMail(String userEmail)  {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(from);
            helper.setSubject("Reset password");
            helper.setTo(userEmail);
            String serverPortNumber="";
            String body = "<html>"
                    + "<div>"
                    + "<a href=\"http://localhost:"+portNumber+"/auth/set-password?userEmail="+ userEmail + "\" target=\"_blank\">"
                    + "click here to reset password."
                    + "</a>"
                    + "</div>"
                    + "</html>";
            helper.setText(body, true);
            mailSender.send(mimeMessage);
        }catch (MessagingException me){
            me.getStackTrace();
            return "problem occurred sending \"Forgot password reset link\"";
        }
        return "Forgot password reset link is sent";
    }

    public String setPassword(String newPassword, String userEmail, PasswordEncoder passwordEncoder) {
        return userServiceImpl.setPasswordAtUserServiceImpl(newPassword,userEmail, passwordEncoder);
    }
}
