package com.WorkBuddy.app.service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface CSVReaderService<T> {
    List<T> readCSV(MultipartFile file, String hasHeader);
}
