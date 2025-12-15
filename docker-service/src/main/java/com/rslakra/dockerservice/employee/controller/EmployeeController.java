package com.rslakra.dockerservice.employee.controller;

import com.rslakra.dockerservice.employee.domain.Employee;
import com.rslakra.dockerservice.employee.service.EmployeeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

import jakarta.annotation.Resource;

/**
 * @author Rohtash Lakra
 * @created 12/2/22 12:30 PM
 */
@RequestMapping("/api/employees")
@RestController
class EmployeeController {

    @Resource
    private EmployeeService employeeService;

    @GetMapping
    public Collection<Employee> getEmployees() {
        return employeeService.getEmployees();
    }
}
