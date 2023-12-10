package com.backend.artbase.services;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.backend.artbase.errors.RuntimeFileException;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import lombok.RequiredArgsConstructor;

@Transactional
@Service
@RequiredArgsConstructor
public class FileService {

    @Value("${gcp.config.file}")
    private String keyFile;

    @Value("${gcp.project.id}")
    private String projectId;

    @Value("${gcp.bucket.id}")
    private String bucketId;

    @Value("${gcp.dir.name}")
    private String dirName;

    public void uploadFiles(MultipartFile[] files, String commonPrefix) throws Exception {
        for (int i = 0; i < files.length; i++) {
            MultipartFile file = files[i];
            String fileName = commonPrefix + "-" + i;
            uploadFile(file, fileName);
        }
    }

    public void uploadFile(MultipartFile multipartFile, String fileName) throws Exception {

        String extension = checkFileExtension(multipartFile.getOriginalFilename());
        try {

            byte[] fileData = multipartFile.getBytes();

            InputStream inputStream = new ClassPathResource(keyFile).getInputStream();

            StorageOptions options = StorageOptions.newBuilder().setProjectId(projectId)
                    .setCredentials(GoogleCredentials.fromStream(inputStream)).build();

            Storage storage = options.getService();
            Bucket bucket = storage.get(bucketId, Storage.BucketGetOption.fields());

            Blob blob = bucket.create(dirName + "/" + fileName + "-" + extension, fileData);

            if (blob != null) {
                System.out.println("File successfully uploaded to GCS");
                return;
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeFileException("An error occurred while storing data to GCS", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        throw new RuntimeFileException("An error occurred while storing data to GCS", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private String checkFileExtension(String fileName) {
        if (fileName != null && fileName.contains(".")) {
            String[] extensionList = { ".png", ".jpeg", ".jpg" };

            for (String extension : extensionList) {
                if (fileName.endsWith(extension)) {
                    return extension;
                }
            }
        }
        throw new RuntimeFileException("Not a permitted file type", HttpStatus.BAD_REQUEST);
    }

}
