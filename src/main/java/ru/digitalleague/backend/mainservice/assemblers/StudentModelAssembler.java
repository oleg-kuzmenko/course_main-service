package ru.digitalleague.backend.mainservice.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import ru.digitalleague.backend.javamodel.entities.Student;
import ru.digitalleague.backend.mainservice.controllers.StudentController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class StudentModelAssembler implements RepresentationModelAssembler<Student, EntityModel<Student>> {

    @Override
    public EntityModel<Student> toModel(Student student) {

        EntityModel<Student> studentEntityModel = EntityModel.of(
                student,
                linkTo(methodOn(StudentController.class).getById(student.getStudentId())).withSelfRel(),
                linkTo(methodOn(StudentController.class).deleteStudent(student.getStudentId())).withRel("delete"),
                linkTo(methodOn(StudentController.class).replaceStudent(null, student.getStudentId())).withRel("replcae")
        );

        return studentEntityModel;
    }
}

