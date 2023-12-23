package com.WorkBuddy.app.model.domain;

import java.time.LocalDate;

public class ProjectWorkEntry {
    private int empID;
    private int projectID;
    private LocalDate dateFrom;
    private LocalDate dateTo;

    public ProjectWorkEntry(int empID, int projectID, LocalDate dateFrom, LocalDate dateTo) {
        this.empID = empID;
        this.projectID = projectID;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }


    public ProjectWorkEntry() {

    }

    public int getEmpID() {
        return empID;
    }

    public void setEmpID(int empID) {
        this.empID = empID;
    }

    public int getProjectID() {
        return projectID;
    }

    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }

    public LocalDate getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(LocalDate dateFrom) {
        this.dateFrom = dateFrom;
    }

    public LocalDate getDateTo() {
        return dateTo;
    }

    public void setDateTo(LocalDate dateTo) {
        this.dateTo = dateTo;
    }

    @Override
    public String toString() {
        return "ProjectWorkEntry{" +
                "empID=" + empID +
                ", projectID=" + projectID +
                ", dateFrom=" + dateFrom +
                ", dateTo=" + dateTo +
                '}';
    }
}

