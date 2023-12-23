package com.WorkBuddy.app.service;

import com.WorkBuddy.app.exception.CSVIOException;
import com.WorkBuddy.app.exception.NoUploadedFileException;
import com.WorkBuddy.app.model.entity.FileEntity;
import com.WorkBuddy.app.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private final FileRepository fileRepository;

    @Autowired
    public FileStorageServiceImpl(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Override
    public String storeFile(MultipartFile file) {
        if (file == null) {
            throw new NoUploadedFileException("No file uploaded");
        }

        String fileName = file.getOriginalFilename();
        if (fileName == null) {
            throw new NoUploadedFileException("Your file has no name");
        }

        try {
            byte[] bytes = file.getBytes();
            FileEntity fileEntity = new FileEntity(fileName, bytes);
            fileRepository.save(fileEntity);
        } catch (IOException e) {
            throw new CSVIOException("Error reading the file when storing it.");
        }
        return fileName;
    }


}
