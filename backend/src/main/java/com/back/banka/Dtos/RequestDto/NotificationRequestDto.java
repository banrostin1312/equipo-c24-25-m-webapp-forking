package com.back.banka.Dtos.RequestDto;

import com.back.banka.Enums.NotificationsType;
import com.back.banka.Model.User;
import lombok.Data;

@Data
public class NotificationRequestDto {
    private String title;
    private String body;
    private User user;
    private NotificationsType type;
}
