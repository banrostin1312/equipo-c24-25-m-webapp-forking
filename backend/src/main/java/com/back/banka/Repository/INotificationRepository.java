package com.back.banka.Repository;

import com.back.banka.Model.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;

public interface INotificationRepository extends JpaRepository<Notifications, Long> {
}
