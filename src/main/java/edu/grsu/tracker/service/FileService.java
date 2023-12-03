package edu.grsu.tracker.service;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import edu.grsu.tracker.exception.TrackerExceptoin;
import edu.grsu.tracker.storage.entity.LoadFile;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class FileService {

    private final GridFsTemplate template;
    private final GridFsOperations operations;

    public String addFile(final Long issueId, final MultipartFile upload) throws IOException {

        DBObject metadata = new BasicDBObject();
        metadata.put("fileSize", upload.getSize());
        metadata.put("issueId", issueId);

        Object fileID = template.store(upload.getInputStream(),
                upload.getOriginalFilename(), upload.getContentType(), metadata);

        return fileID.toString();
    }

    public LoadFile downloadFile(final Long id) throws IOException {
        GridFSFile gridFSFile = template.findOne(
                new Query(Criteria.where("metadata.issueId").is(id)));

        if (gridFSFile == null) {
            throw new TrackerExceptoin("File not found");
        }

        return LoadFile.builder()
                .filename(gridFSFile.getFilename())
                .fileType(gridFSFile.getMetadata().get("_contentType").toString())
                .fileSize(gridFSFile.getMetadata().get("fileSize").toString())
                .issueId(Long.valueOf(gridFSFile.getMetadata().get("issueId").toString()))
                .file(IOUtils.toByteArray(operations.getResource(gridFSFile).getInputStream()))
                .build();
    }
}
