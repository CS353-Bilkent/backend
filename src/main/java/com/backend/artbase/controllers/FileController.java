package com.backend.artbase.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.backend.artbase.entities.ApiResponse;
import com.backend.artbase.entities.User;
import com.backend.artbase.services.FileService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/file")
public class FileController {
    private final FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<?>> uploadFile(HttpServletRequest request, @RequestPart MultipartFile file) {
        User user = (User) request.getAttribute("user");
        fileService.uploadFile(file, "test_file" + user.getUserName());
        return ResponseEntity.ok(ApiResponse.builder().build());
    }

    @PostMapping("/uploadMulti")
    public ResponseEntity<ApiResponse<?>> uploadFile(HttpServletRequest request, @RequestPart MultipartFile[] files) {
        User user = (User) request.getAttribute("user");
        fileService.uploadFiles(files, "test_file" + user.getUserName());
        return ResponseEntity.ok(ApiResponse.builder().build());
    }
}
