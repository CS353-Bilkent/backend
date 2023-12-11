package com.backend.artbase.services;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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

import com.backend.artbase.daos.FileDao;
import com.backend.artbase.errors.RuntimeFileException;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import lombok.RequiredArgsConstructor;

@Transactional
@Service
@RequiredArgsConstructor
public class FileService {

    private final FileDao fileDao;

    @Value("${gcp.config.file}")
    private String keyFile;

    @Value("${gcp.project.id}")
    private String projectId;

    @Value("${gcp.bucket.id}")
    private String bucketId;

    @Value("${gcp.dir.name}")
    private String dirName;

    public void uploadFiles(MultipartFile[] files, String commonPrefix) {
        for (int i = 0; i < files.length; i++) {
            MultipartFile file = files[i];
            String fileName = commonPrefix + "_" + i;

            Integer fileID = fileDao.getNextFileId();

            fileDao.saveFile(fileID, fileName + checkFileExtension(file.getOriginalFilename()));
            uploadFile(file, fileName);
        }
    }

    public void uploadArtworkFiles(MultipartFile[] files, String commonPrefix, Integer artworkId) {
        for (int i = 0; i < files.length; i++) {
            MultipartFile file = files[i];
            String fileName = commonPrefix + "_" + i;

            Integer fileID = fileDao.getNextFileId();

            fileDao.saveArtworkFile(fileID, fileName + checkFileExtension(file.getOriginalFilename()), artworkId);
            uploadFile(file, fileName);
        }
    }

    public void uploadFile(MultipartFile multipartFile, String fileName) {

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

    public byte[] getFile(String filename) {
        return getFileAsByteArrayFromGCS(filename);
    }

    public List<byte[]> getArtworkFiles(Integer artworkId) {
        List<String> artworkFilenames = fileDao.getArtworkFilenames(artworkId);

        List<byte[]> fileList = new ArrayList<>();

        for (String filename : artworkFilenames) {
            byte[] file = getFileAsByteArrayFromGCS(filename);
            fileList.add(file);
        }
        if (fileList.isEmpty()) {
            throw new RuntimeFileException("Image files with given artwork id cannot be found", HttpStatus.NOT_FOUND);
        }
        return fileList;

    }

    private byte[] getFileAsByteArrayFromGCS(String filename) {
        try {
            InputStream inputStream = new ClassPathResource(keyFile).getInputStream();

            StorageOptions options = StorageOptions.newBuilder().setProjectId(projectId)
                    .setCredentials(GoogleCredentials.fromStream(inputStream)).build();

            Storage storage = options.getService();
            BlobId blobId = BlobId.of(bucketId, dirName + "/" + filename);
            byte[] fileContent = storage.readAllBytes(blobId);

            return fileContent;
        } catch (IOException e) {
            throw new RuntimeFileException("An error occurred while retrieving the file from GCS", HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
