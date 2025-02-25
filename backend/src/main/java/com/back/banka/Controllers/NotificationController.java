package com.back.banka.Controllers;

import com.back.banka.Dtos.RequestDto.NotificationRequestDto;
import com.back.banka.Model.Notifications;
import com.back.banka.Services.Impl.NotificationServiceImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public class NotificationController {

    private NotificationServiceImpl notificationService;

    @PostMapping
    public Notifications createNotification(@RequestBody NotificationRequestDto request){
        return notificationService.createNotification(
                request.getTitle(),
                request.getBody(),
                request.getUser(),
                request.getType()
        );
    }
}
