package com.backend.artbase.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.artbase.entities.ApiResponse;
import com.backend.artbase.entities.Notification;
import com.backend.artbase.entities.User;
import com.backend.artbase.services.NotificationService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<Notification>>> getNotificationsByUserId(HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        List<Notification> userNotifications = notificationService.getNotificationsByUserId(user.getUserId());
        return ResponseEntity.ok(ApiResponse.<List<Notification>>builder().operationResultData(userNotifications).build());
    }

    @PostMapping("/delete/{notificationId}")
    public ResponseEntity<ApiResponse<Void>> deleteNotification(@PathVariable Integer notificationId) {
        notificationService.deleteNotification(notificationId);
        return ResponseEntity.ok(ApiResponse.<Void>builder().build());
    }
}
