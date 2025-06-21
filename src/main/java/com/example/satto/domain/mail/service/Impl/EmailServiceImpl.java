package com.example.satto.domain.mail.service.Impl;

import com.example.satto.domain.mail.service.EmailService;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender emailSender;
    private final StringRedisTemplate redisTemplate;

    private final long EXPIRE_MINUTES = 5;

    private MimeMessage createMessage(String to, String code) throws Exception {
        MimeMessage message = emailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, to);    // 보내는 대상
        message.setSubject("이메일 인증");   // 제목

        String html = "<div style='margin:15px;'>"
                + "<h1> SATTO </h1>"
                + "<p>아래 코드를 복사해 입력해주세요.</p>"
                + "<div align='center' style='border:1px solid black;'>"
                + "<h3 style='color:blue;'>이메일 인증 코드입니다.</h3>"
                + "<div style='font-size:130%'>CODE : <strong>" + code + "</strong></div>"
                + "</div></div>";

//        String msgg="";
//        msgg+= "<div style='margin:15px;'>";
//        msgg+= "<h1> SATTO </h1>";
//        msgg+= "<br>";
//        msgg+= "<p>아래 코드를 복사해 입력해주세요<p>";
//        msgg+= "<br>";
//        msgg+= "<p>감사합니다.<p>";
//        msgg+= "<br>";
//        msgg+= "<div align='center' style='border:1px solid black; font-family:verdana';>";
//        msgg+= "<h3 style='color:blue;'>이메일 인증 코드입니다.</h3>";
//        msgg+= "<div style='font-size:130%'>";
//        msgg+= "CODE : <strong>";
//        msgg+= ePw+"</strong><div><br/> ";
//        msgg+= "</div>";
        message.setText(html, "utf-8", "html");//내용
        message.setFrom(new InternetAddress("satto202409@gmail.com", "satto"));//보내는 사람
        return message;
    }

    public static String createKey() {
        StringBuilder key = new StringBuilder();
        Random rnd = new Random();
        for (int i = 0; i < 8; i++) { // 인증코드 8자리
            int index = rnd.nextInt(3); // 0~2 까지 랜덤
            switch (index) {    // java 14 (향상된 switch 표현식)
                case 0 -> key.append((char) ((int) (rnd.nextInt(26)) + 97));    //  a~z
                case 1 -> key.append((char) ((int) (rnd.nextInt(26)) + 65));    //  A~Z
                case 2 -> key.append((rnd.nextInt(10)));    // 0~9
            }
        }
        return key.toString();
    }

    @Override
    public String sendSimpleMessage(String to) throws Exception {
        String code = createKey();
        MimeMessage message = createMessage(to, code);
        try {
            emailSender.send(message);
            redisTemplate.opsForValue().set(to, code, Duration.ofMinutes(EXPIRE_MINUTES));
        } catch (MailException es) {
            es.printStackTrace();
            throw new IllegalArgumentException("메일 전송 실패");
        }
        return code;
    }

    public boolean verifyCode(String email, String inputCode) {
        String savedCode = redisTemplate.opsForValue().get(email);
        return savedCode != null && savedCode.equals(inputCode);
    }

    @Override
    public String sendFindPwMessage(String toEmail) throws Exception {
        String tempPw = createKey();
        // TODO Auto-generated method stub
        MimeMessage message = findPwMessage(toEmail, tempPw);

        try {
            emailSender.send(message);
            // Redis에 인증번호 저장
        } catch (MailException es) {
            es.printStackTrace();
            throw new IllegalArgumentException("메일 전송 실패");
        }
        return tempPw;
    }


    private MimeMessage findPwMessage(String toEmail, String tempPw) throws Exception {
        MimeMessage message = emailSender.createMimeMessage();
        ((MimeMessage) message).addRecipients(MimeMessage.RecipientType.TO, toEmail);   // 보내는 대상
        message.setSubject("임시 비밀번호 발급");   // 제목

        String html = "<div style='margin:15px;'>"
                + "<h1> SATTO </h1>"
                + "<p>아래 임시 비밀번호를 복사해 입력해주세요.</p>"
                + "<div align='center' style='border:1px solid black;'>"
                + "<h3 style='color:blue;'>임시 비밀번호입니다.</h3>"
                + "<div style='font-size:130%'>PASSWORD : <strong>" + tempPw + "</strong></div>"
                + "</div></div>";

//        String msgg="";
//        msgg+= "<div style='margin:15px;'>";
//        msgg+= "<h1> SATTO </h1>";
//        msgg+= "<br>";
//        msgg+= "<p>아래 임시 비밀번호를 복사해 입력해주세요<p>";
//        msgg+= "<br>";
//        msgg+= "<p>감사합니다.<p>";
//        msgg+= "<br>";
//        msgg+= "<div align='center' style='border:1px solid black; font-family:verdana';>";
//        msgg+= "<h3 style='color:blue;'>임시 비밀번호 코드입니다.</h3>";
//        msgg+= "<div style='font-size:130%'>";
//        msgg+= "CODE : <strong>";
//        msgg+= findePw+"</strong><div><br/> ";
//        msgg+= "</div>";
        message.setText(html, "utf-8", "html"); // 내용
        message.setFrom(new InternetAddress("satto202409@gmail.com", "satto"));//보내는 사람
        return message;

    }

}
