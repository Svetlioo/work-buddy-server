package com.WorkBuddy.app.service;

import java.util.List;
import java.util.Map;

public interface EmployeeCollaborationService<T, U> {
    void setUploadedData(List<U> data);

    List<T> getCollaborationResultsSortedByDays(String param);

    T getCollaborationResultWithMostDays();

    Map<String, T> getCollaborationResultsMap();

    T getCollaborationResultByPairIDs(Integer empID1, Integer empID2);
}
