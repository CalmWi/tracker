package edu.grsu.tracker.storage.entity;

import lombok.Builder;
import lombok.Data;
@Builder
@Data
public class LoadFile {

    private Long issueId;

    private String filename;

    private String fileType;

    private String fileSize;

    private byte[] file;
}
