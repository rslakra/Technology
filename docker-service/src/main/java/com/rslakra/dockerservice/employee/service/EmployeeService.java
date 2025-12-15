package com.rslakra.dockerservice.employee.service;

import com.rslakra.dockerservice.employee.domain.Employee;
import com.rslakra.dockerservice.employee.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;

/**
 * @author Rohtash Lakra
 * @created 12/2/22 12:41 PM
 */
@Service
public class EmployeeService {

    @Resource
    private EmployeeRepository employeeRepository;

    @PostConstruct
    public void init() {
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee("Roh"));
        employees.add(new Employee("Sin"));
        employees.add(new Employee("Lak"));
        employees.add(new Employee("San"));
        employees = employeeRepository.saveAll(employees);
        System.out.println("Employees:" + employees);
    }

    /**
     * @return
     */
    public Collection<Employee> getEmployees() {
        return employeeRepository.findAll();
    }

}
