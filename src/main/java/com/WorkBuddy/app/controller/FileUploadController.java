package com.WorkBuddy.app.controller;

import com.WorkBuddy.app.model.domain.CollaborationResult;
import com.WorkBuddy.app.model.domain.ProjectWorkEntry;
import com.WorkBuddy.app.service.CSVReaderService;
import com.WorkBuddy.app.service.EmployeeCollaborationService;
import com.WorkBuddy.app.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/upload")
public class FileUploadController {
    private final CSVReaderService<ProjectWorkEntry> csvReaderService;
    private final EmployeeCollaborationService<CollaborationResult, ProjectWorkEntry> employeeCollaborationService;
    private final FileStorageService fileStorageService;

    @Autowired
    public FileUploadController(CSVReaderService<ProjectWorkEntry> csvReaderService,
                                EmployeeCollaborationService<CollaborationResult, ProjectWorkEntry> employeeCollaborationService,
                                FileStorageService fileStorageService) {
        this.csvReaderService = csvReaderService;
        this.employeeCollaborationService = employeeCollaborationService;
        this.fileStorageService = fileStorageService;
    }

    @PostMapping()
    // Required is false so that when the user does not input the file, file variable becomes null.
    // Then I can catch the appropriate exception in the service in order to send the correct error message, instead of catching a global exception.
    public ResponseEntity<?> handleFileUpload(
            @RequestParam(name = "file", required = false) MultipartFile file,
            @RequestParam(name = "hasHeader", defaultValue = "false") String hasHeader,
            @RequestParam(name = "storeFile", defaultValue = "false") String storeFile
    ) {
        List<ProjectWorkEntry> projects = csvReaderService.readCSV(file, hasHeader);
        employeeCollaborationService.setUploadedData(projects);
        if (storeFile.equalsIgnoreCase("true")) {
            fileStorageService.storeFile(file);
        }
        return ResponseEntity.ok(projects);
    }

}
