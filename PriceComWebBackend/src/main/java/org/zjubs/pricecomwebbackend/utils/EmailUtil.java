package org.zjubs.pricecomwebbackend.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailUtil {

    @Autowired
    private JavaMailSender mailSender;

    public void sendAuthCodeEmail(String email, String verificationCode) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            String fromEmail = "zjubszjl@163.com";
            message.setFrom(fromEmail);
            message.setTo(email);
            message.setSubject("Verification Code");
            message.setText("Your verification code is: " + verificationCode);
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendRemainderEmail(String email, String description, Double oldPrice, Double newPrice, String url) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            String fromEmail = "zjubszjl@163.com";
            message.setFrom(fromEmail);
            message.setTo(email);
            message.setSubject("降价提醒");
            String remindMessage = "您关注的商品 " + description + " 降价了，您设置关注时的价格为 ￥" + oldPrice + "，当前的价格为 ￥" + newPrice + "，赶紧去看看吧!\n商品链接：" + url;
            message.setText(remindMessage);
            mailSender.send(message);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
