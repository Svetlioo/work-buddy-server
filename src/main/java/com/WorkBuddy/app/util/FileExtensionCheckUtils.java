package com.WorkBuddy.app.util;

import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;


public class FileExtensionCheckUtils {
    public static boolean isCsvFile(MultipartFile file) {
        // Check if the content type is compatible with "text/csv"
        String contentTypeString = file.getContentType();
        if (contentTypeString != null) {
            MediaType contentType = MediaType.parseMediaType(contentTypeString);
            if (contentType.isCompatibleWith(MediaType.parseMediaType("text/csv"))) {
                return true;
            }
        }

        // Check if the file name has a ".csv" extension
        String filename = file.getOriginalFilename();
        return (filename != null && filename.toLowerCase().endsWith(".csv"));
    }
}
