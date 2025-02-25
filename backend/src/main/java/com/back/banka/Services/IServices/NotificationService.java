package com.back.banka.Services.IServices;

import com.back.banka.Dtos.RequestDto.NotificationRequestDto;
import com.back.banka.Model.Notifications;

public interface NotificationService {
    Notifications createNotification(NotificationRequestDto request);
}
