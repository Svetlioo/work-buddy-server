package com.WorkBuddy.app.controller;

import com.WorkBuddy.app.model.entity.Employee;
import com.WorkBuddy.app.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> createEmployee(@Valid @RequestBody(required = false) Employee newEmployee) {
        employeeService.saveEmployee(newEmployee);
        return new ResponseEntity<>(String.format("Employee with id %d created successfully.", newEmployee.getId()), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEmployeeById(@PathVariable String id) {
        Employee employee = employeeService.getEmployeeById(id);
        return new ResponseEntity<>(employee, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<?> getEmployees() {
        List<Employee> employees = employeeService.getAllEmployees();
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmployeeById(@PathVariable String id) {
        employeeService.deleteEmployeeById(id);
        return new ResponseEntity<>(String.format("Employee with id %s deleted successfully.", id),HttpStatus.OK);
    }


    @PutMapping("/edit/{id}")
    public ResponseEntity<?> updateEmployee(
            @PathVariable String id,
            @Valid @RequestBody(required = false) Employee updatedEmployee
    ) {
        employeeService.editEmployee(id, updatedEmployee);
        return new ResponseEntity<>(String.format("Employee with id %s updated successfully.", id), HttpStatus.OK);
    }

}
