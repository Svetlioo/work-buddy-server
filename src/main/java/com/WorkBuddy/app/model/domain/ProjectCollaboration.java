package com.WorkBuddy.app.model.domain;

import jakarta.persistence.*;

import java.time.LocalDate;


public class ProjectCollaboration {
    private int projectID;
    private int duration;
    private LocalDate dateFrom;
    private LocalDate dateTo;

    public ProjectCollaboration(int projectID, int duration, LocalDate dateFrom, LocalDate dateTo) {
        this.projectID = projectID;
        this.duration = duration;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    public ProjectCollaboration() {

    }

    public int getProjectID() {
        return projectID;
    }

    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
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
}

