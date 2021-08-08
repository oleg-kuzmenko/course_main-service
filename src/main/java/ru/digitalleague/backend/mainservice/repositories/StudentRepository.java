package ru.digitalleague.backend.mainservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.digitalleague.backend.javamodel.entities.Student;

public interface StudentRepository extends JpaRepository<Student, Integer> {
}
