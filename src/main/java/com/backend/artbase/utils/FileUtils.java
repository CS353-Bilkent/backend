package com.backend.artbase.utils;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class FileUtils {

    public static ResponseEntity<byte[]> createFileResponse(String fileHeader, byte[] body) {
        return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/octet-stream"))
                .header("Content-disposition", fileHeader).body(body);
    }
}
