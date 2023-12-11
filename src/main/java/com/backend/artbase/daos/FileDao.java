package com.backend.artbase.daos;

import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import com.backend.artbase.core.CustomJdbcTemplate;
import com.backend.artbase.core.CustomSqlParameters;
import com.backend.artbase.core.ResultSetWrapper;
import com.backend.artbase.errors.RuntimeFileException;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class FileDao {

    private final CustomJdbcTemplate jdbcTemplate;

    public List<String> getArtworkFilenames(Integer artworkId) {
        CustomSqlParameters params = CustomSqlParameters.create();
        params.put("artwork_id", artworkId);

        //@formatter:off
        String sql =
            "SELECT filename FROM GCS_FILES WHERE artwork_id = :artwork_id";
        //@formatter:on

        return jdbcTemplate.query(sql, params, (rs, rnum) -> {
            ResultSetWrapper rsw = new ResultSetWrapper(rs);
            return rsw.getString("filename");
        });
    }

    public String getFilename(Integer fileId) {
        CustomSqlParameters params = CustomSqlParameters.create();
        params.put("file_id", fileId);

        //@formatter:off
        String sql =
            "SELECT filename FROM GCS_FILES WHERE file_id = :file_id";
        //@formatter:on

        try {
            return jdbcTemplate.queryForObject(sql, params, (rs, rnum) -> {
                ResultSetWrapper rsw = new ResultSetWrapper(rs);
                return rsw.getString("filename");
            });
        } catch (EmptyResultDataAccessException ex) {
            throw new RuntimeFileException("There is no file with given ID", HttpStatus.NOT_FOUND);
        }
    }

    public Integer getNextFileId() {
        String sql = "SELECT nextval('gcs_files_file_id_seq') AS gcs_file_id";

        CustomSqlParameters params = CustomSqlParameters.create();

        return jdbcTemplate.queryForObject(sql, params, (rs, rnum) -> {
            ResultSetWrapper rsw = new ResultSetWrapper(rs);
            return rsw.getInteger("gcs_file_id");
        });
    }

    public void saveFile(Integer fileId, String filename) {

        CustomSqlParameters params = CustomSqlParameters.create();
        params.put("file_id", fileId);
        params.put("filename", filename);
        //@formatter:off
        String sql =
            "INSERT INTO GCS_FILES (file_id, filename) VALUES (:file_id, :filename)";
        //@formatter:on

        jdbcTemplate.update(sql, params);
    }

    public void saveArtworkFile(Integer fileId, String filename, Integer artworkId) {
        CustomSqlParameters params = CustomSqlParameters.create();
        params.put("file_id", fileId);
        params.put("filename", filename);
        params.put("artwork_id", artworkId);

        //@formatter:off
        String sql =
            "INSERT INTO GCS_FILES (file_id, filename, artwork_id) VALUES (:file_id, :filename, :artwork_id)";
        //@formatter:on

        jdbcTemplate.update(sql, params);
    }

    public void deleteFile(Integer fileId) {

        CustomSqlParameters params = CustomSqlParameters.create();
        params.put("file_id", fileId);

        //@formatter:off
        String sql =
            "DELETE FROM GCS_FILES WHERE file_id = :file_id";
        //@formatter:on

        jdbcTemplate.update(sql, params);
    }

}
