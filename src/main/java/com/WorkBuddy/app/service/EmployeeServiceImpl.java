package com.WorkBuddy.app.service;

import com.WorkBuddy.app.exception.EmployeeNotFoundException;
import com.WorkBuddy.app.exception.InvalidPathVariable;
import com.WorkBuddy.app.exception.RequestBodyMissingException;
import com.WorkBuddy.app.model.entity.Employee;
import com.WorkBuddy.app.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public List<Employee> getAllEmployees() {
        return this.employeeRepository.findAll();
    }

    @Override
    public Employee getEmployeeById(String id) {
        int employeeId;
        try {
            employeeId = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            throw new InvalidPathVariable("Enter an Integer value for the id.");
        }
        Optional<Employee> optional = this.employeeRepository.findById(employeeId);
        return optional.orElseThrow(() -> new EmployeeNotFoundException(String.format("Employee with id %d not found.", employeeId)));
    }

    @Override
    public void saveEmployee(Employee employee) {
        if (employee == null) throw new RequestBodyMissingException("Include the employee request body.");
        this.employeeRepository.save(employee);
    }

    @Override
    public void deleteEmployeeById(String id) {
        Employee employee = this.getEmployeeById(id);
        if (employee != null) {
            this.employeeRepository.deleteById(employee.getId());
        } else {
            // This will be thrown only if the id is an integer because it is checked in the getEmployeeById method
            // So there is no possibility to receive invalid exception response with id like "asd"
            throw new EmployeeNotFoundException("Employee not found for id: " + id);
        }
    }

    @Override
    public void editEmployee(String id, Employee updatedEmployee) {
        if (updatedEmployee == null) throw new RequestBodyMissingException("Request body is missing");

        Employee existingEmployee = getEmployeeById(id);

        Employee editedEmployee = Employee.builder()
                .firstName(updatedEmployee.getFirstName())
                .lastName(updatedEmployee.getLastName())
                .role(updatedEmployee.getRole())
                .build();
        // Can use model mapper, but it is not worth using in this simple project
        existingEmployee.setFirstName(editedEmployee.getFirstName());
        existingEmployee.setLastName(editedEmployee.getLastName());
        existingEmployee.setRole(editedEmployee.getRole());

        this.employeeRepository.save(existingEmployee);
    }

}
