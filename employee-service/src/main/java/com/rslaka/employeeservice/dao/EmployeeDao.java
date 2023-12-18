package com.rslaka.employeeservice.dao;

import com.rslaka.employeeservice.model.Employee;

import java.util.List;

public interface EmployeeDao {

    /**
     * @return
     */
    List<Employee> getEmployees();

    /**
     * @param employee
     */
    void saveEmployee(Employee employee);
}
