package com.back.banka.Services.Impl;

import com.back.banka.Enums.NotificationsType;
import com.back.banka.Model.Notifications;
import com.back.banka.Model.User;
import com.back.banka.Repository.INotificationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class NotificationServiceImpl {

    private final INotificationRepository notificationRepository;

    public Notifications createNotification(String title, String body, User user, NotificationsType type) {
        Notifications notification = Notifications.builder()
                .title(title)
                .body(body)
                .date(LocalDateTime.now())
                .user(user)
                .type(type)
                .build();

        return notificationRepository.save(notification);
    }
}
