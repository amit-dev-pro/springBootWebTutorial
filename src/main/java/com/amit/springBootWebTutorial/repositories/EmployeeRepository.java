package com.amit.springBootWebTutorial.repositories;

import com.amit.springBootWebTutorial.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<EmployeeEntity,Long> {
}
