package ru.digitalleague.backend.mainservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.digitalleague.backend.javamodel.entities.Department;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {
}
