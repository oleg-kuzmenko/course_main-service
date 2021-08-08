package ru.digitalleague.backend.mainservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.digitalleague.backend.javamodel.entities.Class;

import java.util.Optional;

public interface ClassRepository extends JpaRepository<Class, Integer> {

    Optional<Class> findByName(String className);

}
