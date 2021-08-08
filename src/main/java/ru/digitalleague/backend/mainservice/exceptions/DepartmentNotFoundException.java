package ru.digitalleague.backend.mainservice.exceptions;

public class DepartmentNotFoundException extends RuntimeException{

    public DepartmentNotFoundException(Integer id) {
        super("Отсутствует кафедра с id " + id);
    }
}
