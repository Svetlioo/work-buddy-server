package com.WorkBuddy.app.controller;


import com.WorkBuddy.app.model.domain.CollaborationResult;
import com.WorkBuddy.app.model.domain.ProjectWorkEntry;
import com.WorkBuddy.app.service.EmployeeCollaborationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/collaboration")
public class CollaborationInfoController {
    private final EmployeeCollaborationService<CollaborationResult, ProjectWorkEntry> employeeCollaborationService;

    @Autowired
    public CollaborationInfoController(EmployeeCollaborationService<CollaborationResult, ProjectWorkEntry> employeeCollaborationService) {
        this.employeeCollaborationService = employeeCollaborationService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<CollaborationResult>> getCollaborationResults(@RequestParam(defaultValue = "desc") String sortOrder) {
        List<CollaborationResult> collaborationResults = employeeCollaborationService.getCollaborationResultsSortedByDays(sortOrder);
        return ResponseEntity.ok(collaborationResults);
    }

    @GetMapping("/longest")
    public ResponseEntity<?> getLongestPairCollaboration() {
        CollaborationResult collaborationResults = employeeCollaborationService.getCollaborationResultWithMostDays();
        return ResponseEntity.ok(collaborationResults);
    }

    @GetMapping("/all/map")
    public ResponseEntity<?> getCollaborationResultsMap() {
        Map<String, CollaborationResult> collaborationResultMap = employeeCollaborationService.getCollaborationResultsMap();
        return ResponseEntity.ok(collaborationResultMap);
    }

    @GetMapping("/{empID1}/{empID2}")
    public ResponseEntity<?> getCollaborationResultByPairIDs(@PathVariable Integer empID1, @PathVariable Integer empID2) {
        CollaborationResult collaborationResult = employeeCollaborationService.getCollaborationResultByPairIDs(empID1, empID2);
        return ResponseEntity.ok(collaborationResult);
    }

}
