package com.WorkBuddy.app.service;

import com.WorkBuddy.app.model.entity.Employee;

import java.util.List;

public interface EmployeeService {
    List<Employee> getAllEmployees();

    Employee getEmployeeById(String id);

    void saveEmployee(Employee employee);

    void deleteEmployeeById(String id);

    void editEmployee(String id, Employee updatedEmployee);

}
