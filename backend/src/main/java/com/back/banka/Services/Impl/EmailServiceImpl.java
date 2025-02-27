package com.back.banka.Services.Impl;

import com.back.banka.Services.IServices.IEmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.nio.charset.StandardCharsets;
import java.util.Map;


@Service
@AllArgsConstructor
public class EmailServiceImpl implements IEmailService {

    private  final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    @Value("${mail.username}")
    private String mailUsername;

    @Override
    public void sendEmail(String to, String subject, String templateName, Map<String, Object> variables) {
        try{
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
            Context context = new Context();
            context.setVariables(variables);
            String htmlContent = templateEngine.process(templateName, context);

            //Construcción del correo
            helper.setFrom(mailUsername);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            javaMailSender.send(message);
        } catch (MessagingException e){
            throw new RuntimeException("Hubo un error al enviar el correo", e);
        }
    }

}