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
    private final TemplateEngine templateEngine;

    @Value("${mail.username}")
    private String mailUsername;

    @Override
    public void sendEmail(String to, String subject, String content) {
        try{
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true,StandardCharsets.UTF_8.name());

            //Construcción del correo
            helper.setFrom(mailUsername);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);

            javaMailSender.send(message);
        } catch (MessagingException e){
            throw new RuntimeException("Hubo un error al enviar el correo", e);
        }



    }

    public void sendEmailTemplate(String to, String subject, String templatename,Map<String,Object> variables ) {
        try{
            String content = renderTemplate(templatename, variables);

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true,StandardCharsets.UTF_8.name());

            //Construcción del correo
            helper.setFrom(mailUsername);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);

            javaMailSender.send(message);
        } catch (MessagingException e){
            throw new RuntimeException("Hubo un error al enviar el correo", e);
        }

    }
    private String renderTemplate(String templateName, Map<String, Object> variables) {
        Context context = new Context();
        context.setVariables(variables);
        return templateEngine.process(templateName, context);
    }
}