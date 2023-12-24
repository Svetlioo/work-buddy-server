package com.WorkBuddy.app.model.domain;

import java.util.ArrayList;
import java.util.List;

public class CollaborationResult {
    private int empID1;
    private int empID2;
    private List<ProjectCollaboration> projectCollaborations;
    private int totalCollaborationDays;


    public CollaborationResult(int empID1, int empID2) {
        this.empID1 = empID1;
        this.empID2 = empID2;
        this.projectCollaborations = new ArrayList<>();
        this.totalCollaborationDays = 0;
    }

    public CollaborationResult() {

    }

    public int getEmpID1() {
        return empID1;
    }

    public void setEmpID1(int empID1) {
        this.empID1 = empID1;
    }

    public int getEmpID2() {
        return empID2;
    }

    public void setEmpID2(int empID2) {
        this.empID2 = empID2;
    }

    public List<ProjectCollaboration> getProjectCollaborations() {
        return projectCollaborations;
    }

    public void setProjectCollaborations(List<ProjectCollaboration> projectCollaborations) {
        this.projectCollaborations = projectCollaborations;
    }

    public int getTotalCollaborationDays() {
        return totalCollaborationDays;
    }

    public void setTotalCollaborationDays(int totalCollaborationDays) {
        this.totalCollaborationDays = totalCollaborationDays;
    }

    public void updateTotalCollaborationDays(int duration) {
        this.totalCollaborationDays += duration;
    }

    public void addProjectCollaboration(ProjectCollaboration projectCollaboration) {
        this.projectCollaborations.add(projectCollaboration);
    }

}

