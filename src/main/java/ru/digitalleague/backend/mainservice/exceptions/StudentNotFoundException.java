package ru.digitalleague.backend.mainservice.exceptions;

public class StudentNotFoundException extends RuntimeException{
    public StudentNotFoundException(Integer id) {
        super(String.format("Студент с id %d не найден", id));
    }
}
