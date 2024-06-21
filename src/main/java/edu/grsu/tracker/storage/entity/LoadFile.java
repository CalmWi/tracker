package edu.grsu.tracker.storage.entity;

import lombok.Builder;
import lombok.Data;
import org.bson.BsonValue;

import java.time.LocalDate;

@Builder
@Data
public class LoadFile {

    private String id;

    private Long issueId;

    private String filename;

    private String fileType;

    private String fileSize;

    private LocalDate uploadDate;

    private byte[] file;
}
