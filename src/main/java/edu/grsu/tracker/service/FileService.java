package edu.grsu.tracker.service;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import edu.grsu.tracker.controller.exception.TrackerExceptoin;
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
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public LoadFile downloadFile(final String id) {
        GridFSFile gridFSFile = template.findOne(
                new Query(Criteria.where("_id").is(id)));

        if (gridFSFile == null) {
            throw new TrackerExceptoin("File not found");
        }

        return getLoadFile(gridFSFile);
    }

    public List<LoadFile> getFilesByIssue(final Long issueId) {
        ArrayList<GridFSFile> gridFSFiles = template.find(
                        new Query(Criteria.where("metadata.issueId").is(issueId)))
                .into(new ArrayList<>());

        return gridFSFiles.stream().map(this::getLoadFile).collect(Collectors.toList());
    }

    private LoadFile getLoadFile(final GridFSFile gridFSFile) {
        try {
            return LoadFile.builder()
                    .id(gridFSFile.getId().asObjectId().getValue().toHexString())
                    .filename(gridFSFile.getFilename())
                    .uploadDate(gridFSFile.getUploadDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                    .fileType(gridFSFile.getMetadata().get("_contentType").toString())
                    .fileSize(gridFSFile.getMetadata().get("fileSize").toString())
                    .issueId(Long.valueOf(gridFSFile.getMetadata().get("issueId").toString()))
                    .file(IOUtils.toByteArray(operations.getResource(gridFSFile).getInputStream()))
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
