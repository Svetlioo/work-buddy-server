package com.WorkBuddy.app.service;

import com.WorkBuddy.app.exception.EmptyCSVException;
import com.WorkBuddy.app.exception.NoUploadedFileException;
import com.WorkBuddy.app.exception.PairNotFoundException;
import com.WorkBuddy.app.model.domain.CollaborationResult;
import com.WorkBuddy.app.model.domain.ProjectCollaboration;
import com.WorkBuddy.app.model.domain.ProjectWorkEntry;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@SessionScope
// Without this session scope annotation the uploaded data from the previous user will be displayed to the next user that uses the app
public class EmployeeCollaborationServiceImpl implements EmployeeCollaborationService<CollaborationResult, ProjectWorkEntry> {

    //  Here I save the uploaded data, so I do not have to read the csv file again when doing the calculations.
    //  This is shared between the two controllers.
    private List<ProjectWorkEntry> uploadedData;

    // Ensuring that the main algorithm is not calculated everytime a request is made
    private Map<String, CollaborationResult> calculatedData;

    public EmployeeCollaborationServiceImpl() {
        this.uploadedData = new ArrayList<>();
        this.calculatedData = new HashMap<>();
    }

    @Override
    public void setUploadedData(List<ProjectWorkEntry> data) {
        // Already validated but just in case someone finds a way to bypass the first validation
        if (data.isEmpty()) throw new EmptyCSVException("The uploaded CSV file is empty.");
        this.uploadedData = new ArrayList<>(data);
        // Setting the old calculated data back to empty list because the new data needs to be processed through the main algorithm
        this.calculatedData = new HashMap<>();
    }

    @Override
    public CollaborationResult getCollaborationResultWithMostDays() {
        ensureCollaborationResultsNotEmpty();
        return calculatedData.values().stream()
                .max(Comparator.comparingInt(CollaborationResult::getTotalCollaborationDays))
                .orElseThrow(() -> new RuntimeException("Something went wrong."));
        // this exception cannot be reached because it is validated in the ensureCollaborationResultNotEmpty method
    }

    @Override
    public List<CollaborationResult> getCollaborationResultsSortedByDays(String param) {
        ensureCollaborationResultsNotEmpty();
        List<CollaborationResult> collaborationResults = new ArrayList<>(calculatedData.values());
        // If it is asc sort it by days , if it is desc or any other - sort it by days, but reversed
        // (default param is desc)
        collaborationResults.sort((param.equalsIgnoreCase("asc"))
                ? Comparator.comparingInt(CollaborationResult::getTotalCollaborationDays)
                : Comparator.comparingInt(CollaborationResult::getTotalCollaborationDays).reversed());
        return collaborationResults;
    }

    @Override
    public Map<String, CollaborationResult> getCollaborationResultsMap() {
        ensureCollaborationResultsNotEmpty();
        return calculatedData;
    }

    @Override
    public CollaborationResult getCollaborationResultByPairIDs(Integer empID1, Integer empID2) {
        ensureCollaborationResultsNotEmpty();
        // Sort them ascending so that the hashing algorithm works
        if (empID1 > empID2) {
            int temp = empID1;
            empID1 = empID2;
            empID2 = temp;
        }

        String key = generateCollaborationKey(empID1, empID2);
        CollaborationResult collaborationResult = calculatedData.get(key);
        if (collaborationResult == null) {
            throw new PairNotFoundException(String.format("%d and %d do not have collaborations with each other.", empID1, empID2));
        }
        return collaborationResult;
    }

    private void ensureCollaborationResultsNotEmpty() {
        if (calculatedData.isEmpty()) {
            calculateCollaborationDetails();
        }

        // This is if someone tries to send a request to get collaborations without uploading a file first
        if (calculatedData.isEmpty()) {
            throw new NoUploadedFileException("File is not uploaded.");
        }
    }


    //    Main algorithm
    private void calculateCollaborationDetails() {
        // Already validated but just in case
        if (uploadedData.isEmpty()) throw new NoUploadedFileException("File is not uploaded.");

        Map<Integer, List<ProjectWorkEntry>> projectGroupMap = new HashMap<>();

        // Group entries by ProjectID (if I want to add feature to search by project id)
        for (ProjectWorkEntry entry : uploadedData) {
            projectGroupMap.computeIfAbsent(entry.getProjectID(), k -> new ArrayList<>()).add(entry);
        }

        // Map to store CollaborationResults with a unique key for efficient retrieval O(1) in the average case
        // (there is a method for generating that key)
        Map<String, CollaborationResult> collaborationResultsMap = new HashMap<>();

        // Iterate through project groups
        for (List<ProjectWorkEntry> projectGroup : projectGroupMap.values()) {
            // Compare each pair of entries
            for (int i = 0; i < projectGroup.size() - 1; i++) {
                for (int j = i + 1; j < projectGroup.size(); j++) {
                    ProjectWorkEntry entry1 = projectGroup.get(i);
                    ProjectWorkEntry entry2 = projectGroup.get(j);

                    if (isCollaborating(entry1, entry2)) {
                        LocalDate overlapStartDate = findOverlapStartDate(entry1.getDateFrom(), entry2.getDateFrom());
                        LocalDate overlapEndDate = findOverlapEndDate(entry1.getDateTo(), entry2.getDateTo());
                        int duration = calculateDuration(overlapStartDate, overlapEndDate);
                        if (duration > 0) {
                            CollaborationResult collaborationResult = getCollaborationResult(collaborationResultsMap, entry1.getEmpID(), entry2.getEmpID());

                            // Add project collaboration
                            collaborationResult.addProjectCollaboration(new ProjectCollaboration(entry1.getProjectID(), duration, overlapStartDate, overlapEndDate));

                        }

                    }
                }
            }
        }
        this.calculatedData = collaborationResultsMap;
        calculateTotalCollaborationDays(new ArrayList<>(calculatedData.values()));
    }


    private static LocalDate findOverlapStartDate(LocalDate fromDateFirstEntry, LocalDate fromDateSecondEntry) {
        return fromDateFirstEntry.isAfter(fromDateSecondEntry) ? fromDateFirstEntry : fromDateSecondEntry;
    }

    private static LocalDate findOverlapEndDate(LocalDate toDateFirstEntry, LocalDate toDateSecondEntry) {
        return toDateFirstEntry.isBefore(toDateSecondEntry) ? toDateFirstEntry : toDateSecondEntry;
    }

    private int calculateDuration(LocalDate overlapStartDate, LocalDate overlapEndDate) {
        return (int) ChronoUnit.DAYS.between(overlapStartDate, overlapEndDate);
    }

    private boolean isCollaborating(ProjectWorkEntry entry1, ProjectWorkEntry entry2) {
        return entry1.getEmpID() != entry2.getEmpID() && entry1.getDateTo() != null && entry2.getDateTo() != null &&
                entry1.getDateTo().isAfter(entry2.getDateFrom()) &&
                entry2.getDateTo().isAfter(entry1.getDateFrom());
    }

    // This is to ensure that if there are employees working on different projects at the same time and collaborating on them at the same time
    // They may have worked 300 days on project 1 and 200 days on project 2 in the same year, however, the unique total days are not 500 in that year
    // This ensures unique days
    private void calculateTotalCollaborationDays(List<CollaborationResult> collaborationResults) {
        for (CollaborationResult collaborationResult : collaborationResults) {
            Set<LocalDate> uniqueDays = new HashSet<>();
            for (ProjectCollaboration collaboration : collaborationResult.getProjectCollaborations()) {
                LocalDate startDate = collaboration.getDateFrom();
                LocalDate endDate = collaboration.getDateTo();

                while (startDate.isBefore(endDate) && !startDate.isEqual(endDate)) {
                    uniqueDays.add(startDate);
                    startDate = startDate.plusDays(1);
                }
            }

            collaborationResult.setTotalCollaborationDays(uniqueDays.size());
        }

    }

    // The efficient get pair by id method using the hashmap and the custom key (the ids can be inputted in any order)
    private CollaborationResult getCollaborationResult(Map<String, CollaborationResult> collaborationResultsMap, int empID1, int empID2) {
        // Generate the unique key
        String collaborationKey = generateCollaborationKey(empID1, empID2);

        // Get the collaboration result from the map
        CollaborationResult existingResult = collaborationResultsMap.get(collaborationKey);
        if (existingResult != null) {
            return existingResult;
        }

        // If not found, create a new collaboration result
        CollaborationResult newResult = new CollaborationResult(empID1, empID2);
        collaborationResultsMap.put(collaborationKey, newResult);
        return newResult;
    }

    // Generates a unique collaboration key based on the provided employee IDs to enable us to search by IDs with an average time complexity of O(1)
    // Can be improved with different hashing
    // TODO Improve with better hashing
    private String generateCollaborationKey(int empID1, int empID2) {

        // Sort them so that the hashing works both ways no matter the order of the ids
        if (empID1 > empID2) {
            int temp = empID1;
            empID1 = empID2;
            empID2 = temp;
        }
        return empID1 + "_" + empID2;

        // This made collisions when there were about 10 000 different collaborations
        //        return Objects.hash(empID1, empID2);
        //        int prime = 31;  // I found that prime number should be used for hashing to minimize collisions
        //        return empID1 * prime + empID2;
    }

}