package com.WorkBuddy.app.repository;

import com.WorkBuddy.app.model.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
}
