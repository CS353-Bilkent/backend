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
import com.backend.artbase.utils.FileUtils;

import ch.qos.logback.core.util.FileUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequiredArgsConstructor
@RequestMapping("/file")
public class FileController {
    private final FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<?>> uploadFile(HttpServletRequest request, @RequestPart MultipartFile file,
            @RequestPart String filename) {
        fileService.uploadFile(file, filename);
        return ResponseEntity.ok(ApiResponse.builder().build());
    }

    @GetMapping("/{filename}")
    public ResponseEntity<byte[]> getFile(@PathVariable String filename) {
        return FileUtils.createFileResponse(filename, fileService.getFile(filename));
    }

}
