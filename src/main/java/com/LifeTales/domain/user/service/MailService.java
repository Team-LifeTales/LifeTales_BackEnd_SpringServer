package com.LifeTales.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class MailService {

    @Autowired
    JavaMailSender emailSender;
    public static final String ePw = createKey();

    public String sendSimpleMessage(String to)throws Exception {
        // TODO Auto-generated method stub
        MimeMessage message = createMessage(to);
        try{//예외처리
            emailSender.send(message);
        }catch(MailException es){
            es.printStackTrace();
            throw new IllegalArgumentException();
        }
        return ePw;
    }
    private MimeMessage createMessage(String to)throws Exception{
        System.out.println("보내는 대상 : "+ to);
        System.out.println("인증 번호 : "+ePw);
        MimeMessage  message = emailSender.createMimeMessage();
        String[] parts = to.split("@");
        String userName = parts[0];

        message.addRecipients(MimeMessage.RecipientType.TO, to);//보내는 대상
        message.setSubject("LifeTales 안녕하세요 !"+userName+"님");//제목

        String msgg="";
        msgg+= " <div class=\"container\" style=\"display: flex; justify-content: center; font-family: 'Gill Sans', 'Gill Sans MT', Calibri, 'Trebuchet MS', sans-serif;\">";
        msgg+= "<div class=\"box\" style=\"display: flex; flex-direction: column; border: 1px solid rgba(0, 0, 0, 0.25); height: 600px; width: 800px; text-align: center; background:url(https://cdn.pixabay.com/photo/2023/03/25/21/15/clover-7876940_1280.png); background-size:cover; padding: 1em; box-shadow: rgba(50, 50, 93, 0.25) 0px 13px 27px -5px, rgba(0, 0, 0, 0.3) 0px 8px 16px -8px;\">";
        msgg+= "<div class=\"text\" style=\"flex: 1;\">";
        msgg+= "<h1 style=\"color: rgba(0, 0, 0, 0.9); font-size: 44px;\">LifeTales - 회원가입 인증</h1>";
        msgg+= "<p style=\"color: rgba(0, 0, 0, 0.8); font-size: 20px;\">안녕하세요 LifeTales 인증 센터입니다."+userName+"님의 가입을 진심으로 환영합니다.</p>";
        msgg+= "</div>";
        msgg+= "<div class=\"text\" style=\"flex: 1;\">";
        msgg+= "<p style=\"color: rgba(0, 0, 0, 0.8); font-size: 20px;\">아래 인증 코드를 회원가입란의 입력 해주십시오.</p>";
        msgg+= "</div>";
        msgg+= " <div class=\"text-box\" style=\"flex: 1; border: 1px solid rgba(0, 0, 0, 0.4); box-shadow: rgba(50, 50, 93, 0.25) 0px 50px 100px -20px, rgba(0, 0, 0, 0.3) 0px 30px 60px -30px;\">";
        msgg+= "<h3 style=\"font-size: 32px;\">회원 인증 코드</h3>";
        msgg+= "<p style=\"font-size: 20px;\">CODE : <strong>"+ePw+"</strong></p> ";
        msgg+= "</div>";
        msgg+= "</div>";
        msgg+= "</div>";
        message.setText(msgg, "utf-8", "html");//내용
        message.setFrom(new InternetAddress("lifetales56@gmail.com","lfeTales"));//보내는 사람

        return message;
    }

    public static String createKey() {
        StringBuffer key = new StringBuffer();
        Random rnd = new Random();

        for (int i = 0; i < 8; i++) { // 인증코드 8자리
            int index = rnd.nextInt(3); // 0~2 까지 랜덤

            switch (index) {
                case 0:
                    key.append((char) ((int) (rnd.nextInt(26)) + 97));
                    //  a~z  (ex. 1+97=98 => (char)98 = 'b')
                    break;
                case 1:
                    key.append((char) ((int) (rnd.nextInt(26)) + 65));
                    //  A~Z
                    break;
                case 2:
                    key.append((rnd.nextInt(10)));
                    // 0~9
                    break;
            }
        }
        return key.toString();
    }


}
