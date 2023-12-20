package com.backend.artbase.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.artbase.daos.NotificationDao;
import com.backend.artbase.entities.Notification;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class NotificationService {

    private final NotificationDao notificationDao;

    public List<Notification> getNotificationsByUserId(Integer userId) {
        return notificationDao.getNotificationsByUserId(userId);
    }

    public void deleteNotification(Integer notificationId) {
        notificationDao.deleteNotification(notificationId);
    }

}
