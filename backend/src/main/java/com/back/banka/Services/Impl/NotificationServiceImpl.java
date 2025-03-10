package com.back.banka.Services.Impl;

import com.back.banka.Dtos.RequestDto.NotificationRequestDto;
import com.back.banka.Enums.NotificationsType;
import com.back.banka.Model.Notifications;
import com.back.banka.Model.User;
import com.back.banka.Repository.INotificationRepository;
import com.back.banka.Services.IServices.INotificationService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements INotificationService {

    private final INotificationRepository notificationRepository;
    private final JavaMailSender mailSender;

    //Se crea y guarda la notificación DB
    public Notifications createAndNotify(String title, String body, User user, NotificationsType type) {
        Notifications notification = Notifications.builder()
                .title(title)
                .body(body)
                .date(LocalDateTime.now())
                .user(user)
                .type(type)
                .build();

        return notificationRepository.save(notification);
    }

    // Se envía correo con los datos de la notificación
    private void sendEmailNotification(String to, String subject, String content) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);

            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Notifications createNotification(NotificationRequestDto request) {
        return null;
    }
}
