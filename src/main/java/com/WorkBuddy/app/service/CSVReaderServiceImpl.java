package com.WorkBuddy.app.service;

import com.WorkBuddy.app.exception.*;
import com.WorkBuddy.app.model.domain.ProjectWorkEntry;
import com.WorkBuddy.app.util.FileExtensionCheckUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class CSVReaderServiceImpl implements CSVReaderService<ProjectWorkEntry> {

    @Override
    public List<ProjectWorkEntry> readCSV(MultipartFile file, String hasHeader) {

        if (file == null) throw new MissingFileException("No file included in the form data.");

        if (file.isEmpty()) throw new EmptyCSVException("The uploaded file is empty.");

        if (!FileExtensionCheckUtils.isCsvFile(file))
            throw new IncompatibleFileTypeException("The uploaded file is not csv format.");

        List<ProjectWorkEntry> entries = new ArrayList<>();
        int lineNumber = 0;
        String line = "";
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            while ((line = br.readLine()) != null) {
                lineNumber++;
                // Skip the first line if the csv has header
                // Doing it with Strings so that if somebody somehow enters wrong data (by using postman or curl) it will just use the default value hasHeader = false
                // In the frontend there will be just a checkbox to indicate if the csv has header or not
                if (hasHeader.equalsIgnoreCase("true")) {
                    hasHeader = "false";
                    continue;
                }
                String[] values = line.split("\\s*,\\s*");
                if (values.length != 4) {
                    throw new InvalidCSVFormatException("The CSV should have only 4 properties.\n(Line " + lineNumber + ") " + line);
                }
                int empID = Integer.parseInt(values[0]);
                int projectID = Integer.parseInt(values[1]);
                LocalDate dateFrom = parseDate(values[2]);
                LocalDate dateTo = values[3].equalsIgnoreCase("NULL") ? LocalDate.now() : parseDate(values[3]);
                entries.add(new ProjectWorkEntry(empID, projectID, dateFrom, dateTo));
            }
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            throw new CSVProcessingException("Error processing CSV file.\n(Line " + lineNumber + ") " + line);
        } catch (ParseException e) {
            throw new InvalidDateFormatException("Date format is not supported.\n(Line " + lineNumber + ") " + line + "\n" + "Invalid date: " + e.getMessage());
        } catch (IOException e) {
            throw new CSVIOException("Error reading the CSV file.");
        }

        return entries;
    }


    private static final String[] DATE_FORMATS = {
            // From most to least common
            "yyyy-MM-dd",        // Example: 2023-12-17 (Original Format)
            "yyyy.MM.dd",        // Example: 2023.12.17
            "yyyy/MM/dd",        // Example: 2023/12/17
            "yyyy-MMM-dd",       // Example: 2023-Dec-17
            "yyyy/MMMM/dd",      // Example: 2023-December-17

            "dd-MM-yyyy",        // Example: 17-12-2023 (Common in Europe)
            "dd.MM.yyyy",        // Example: 17.12.2023 (Common in Europe)
            "dd/MM/yyyy",        // Example: 17/12/2023 (Common in Europe)
            "dd-MMM-yyyy",       // Example: 17-Dec-2023 (Common in Europe)
            "dd-MMMM-yyyy",       // Example: 17-December-2023 (Common in Europe)

            "MM-dd-yyyy",        // Example: 12-17-2023
            "MM.dd.yyyy",        // Example: 12.17.2023
            "MM/dd/yyyy",        // Example: 12/17/2023
            "MMM-dd-yyyy",       // Example: Dec-17-2023
            "MMMM-dd-yyyy",       // Example: December-17-2023

            "yyyy-dd-MM",        // Example: 2023-17-12
            "yyyy.dd.MM",        // Example: 2023.17.12
            "yyyy/dd/MM",        // Example: 2023/17/12
            "yyyy-dd-MMM",       // Example: 2023-12-Dec
            "yyyy/dd/MMMM",      // Example: 2023-17-December
    };

    private LocalDate parseDate(String dateString) throws ParseException {
        for (String format : DATE_FORMATS) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
                return LocalDate.parse(dateString, formatter);
            } catch (Exception ignored) {
                // If it's not a valid pattern just ignore it and continue to the next one
            }
        }

//      If the date format is not supported (not in the list), which is unlikely -
//      Include the dateString in the exception message to notify the user exactly which date is not supported
        throw new ParseException(dateString, 0);
    }

}
