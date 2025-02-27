package com.back.banka.Services.Impl;

import com.back.banka.Services.IServices.IEmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.nio.charset.StandardCharsets;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements IEmailService {

    private  final JavaMailSender javaMailSender;

    @Value("${mail.username}")
    private String mailUsername;

    @Override
    public void sendEmail(String email, String subject, String body) {
        //Enviar correo

            try{
                MimeMessage message = javaMailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                helper.setFrom(mailUsername);
                helper.setTo(email);
                helper.setSubject(subject);
                helper.setText(body, true);
                javaMailSender.send(message);
            } catch (MessagingException e){
                throw new RuntimeException("Hubo un error al enviar el correo", e);
            }
    }
}