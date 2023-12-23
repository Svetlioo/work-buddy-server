package com.WorkBuddy.app.model.entity;

import jakarta.persistence.*;

// FileEntity because there is a java class called File
@Entity
@Table(name = "files")
public class FileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String fileName;

    @Lob
    private byte[] fileContent;

    public FileEntity() {
    }

    public FileEntity(String fileName, byte[] fileContent) {
        this.fileName = fileName;
        this.fileContent = fileContent;
    }


}
