package com.back.banka.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailConfig {
    @Bean
    public JavaMailSender javaMailSender() {
    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

    // Configura los parámetros SMTP según tu proveedor de correo
    mailSender.setHost("smtp.gmail.com");  // Cambia esto si usas otro proveedor
    mailSender.setPort(587);  // El puerto SMTP de Gmail
    mailSender.setUsername("tu-email@gmail.com");  // Tu correo electrónico
    mailSender.setPassword("tu-contraseña");  // Tu contraseña o aplicación de contraseña (si usas Gmail con 2FA)

    Properties props = mailSender.getJavaMailProperties();
    props.put("mail.transport.protocol", "smtp");
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.debug", "true");

    return mailSender;
}
}