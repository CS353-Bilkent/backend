package com.backend.artbase.daos;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public class FileDao {

    public List<String> getArtworkFilenames(Integer artwork_id) {
        return null;
    }

    public String getFilename(Integer file_id) {
        return null;
    }

    public Integer getNextFileId() {
        return 0;
    }

    public void saveFile(Integer file_id, String filename) {

    }

    public void saveArtworkFile(Integer file_id, String filename, Integer artwork_id) {

    }

    public void deleteFile(Integer file_id) {

    }

}
