package ru.digitalleague.backend.mainservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class NotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(DepartmentNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String departmentNotFoundHandler(DepartmentNotFoundException departmentNotFoundException) {
        return departmentNotFoundException.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(BackendServerOffline.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    String serverOffline(BackendServerOffline backendServerOffline) {
        return backendServerOffline.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(StudentNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String studentNotFoundException(StudentNotFoundException snfe){
        return snfe.getMessage();
    }
}
