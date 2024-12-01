package com.rslakra.dockerservice.employee.repository;

import com.rslakra.dockerservice.employee.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Rohtash Lakra
 * @created 12/2/22 12:28 PM
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}
