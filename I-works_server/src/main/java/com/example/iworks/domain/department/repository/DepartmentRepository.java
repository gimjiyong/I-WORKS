package com.example.iworks.domain.department.repository;

import com.example.iworks.domain.department.domain.Department;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {

    Department findByDepartmentName(String name);
    Department findByDepartmentId(Integer departmentId);

}
