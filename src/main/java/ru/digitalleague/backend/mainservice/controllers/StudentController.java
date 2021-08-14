package ru.digitalleague.backend.mainservice.controllers;

import org.hibernate.EntityMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import ru.digitalleague.backend.javamodel.entities.Student;
import ru.digitalleague.backend.javamodel.entities.Class;
import ru.digitalleague.backend.mainservice.assemblers.StudentModelAssembler;
import ru.digitalleague.backend.mainservice.exceptions.StudentNotFoundException;
import ru.digitalleague.backend.mainservice.repositories.ClassRepository;
import ru.digitalleague.backend.mainservice.repositories.StudentRepository;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import static ru.digitalleague.backend.mainservice.specification.StudentSpecification.firstNameLike;
import static ru.digitalleague.backend.mainservice.specification.StudentSpecification.genderEqual;

@RestController
@RequestMapping("/students")
public class StudentController {

    @Value("${student.backendServiceHost}")
    private String backendServiceHost;

    @Value("${student.backendServicePort}")
    private Integer backendServicePort;

    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    ClassRepository classRepository;

    @Autowired
    private StudentModelAssembler assembler;

//    @GetMapping("")
//    public CollectionModel<EntityModel<Student>> all() {
//        List<EntityModel<Student>> students = studentRepository.findAll()
//                .stream()
//                .map(assembler::toModel)
//                .collect(Collectors.toList());
//
//        return CollectionModel.of(
//                students, linkTo(methodOn(StudentController.class).all()).withSelfRel()
//        );
//    }

    @GetMapping("")
    public CollectionModel<EntityModel<Student>> all(
            @RequestParam("gender") Optional<String> gender,
            @RequestParam(value = "first_name", required = true) String firstName
    ) {
        Specification<Student> specification = Specification
                .where(genderEqual(gender))
                .and(firstNameLike(firstName));

        List<Student> students = studentRepository.findAll(specification);
        return assembler.toCollectionModel(students)
                .add(linkTo(methodOn(StudentController.class).all(gender, firstName)).withSelfRel().expand());
    }

    @GetMapping("/{id}")
    //public ResponseEntity<Student> getById(@PathVariable Integer id) {
    public EntityModel<Student> getById(@PathVariable Integer id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));

        return assembler.toModel(student);

//        Optional<Student> student = studentRepository.findById(id);
//        return student.map(ResponseEntity::ok)
//                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/add/{className}")
    public ResponseEntity<Student> extractStudentAndSave(@PathVariable String className) {

        String backendServiceUrl = String.format("http://%s:%d/api/student", backendServiceHost, backendServicePort);
        Student student = restTemplate.getForObject(backendServiceUrl, Student.class);

        List<Class> classList = classRepository.findByName(className)
                .map(List::of)
                .orElse(Collections.emptyList());

        student.setClassList(classList);
        Student savedStudent = studentRepository.save(student);
        URI location = URI.create("/students" + savedStudent.getStudentId());
        return ResponseEntity.created(location).body(savedStudent);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> deleteStudent(@PathVariable Integer id) {
        studentRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/replace")
    public ResponseEntity<Student> replaceStudent (@RequestBody Student modifiedStudent, @PathVariable Integer id) {
        Student replacedStudent = studentRepository.findById(id)
                .map(
                        s -> {
                            s.setLastName(modifiedStudent.getLastName());
                            s.setFirstName(modifiedStudent.getFirstName());
                            s.setDob(modifiedStudent.getDob());
                            s.setGender(modifiedStudent.getGender());
                            s.setEmail(modifiedStudent.getEmail());
                            return studentRepository.save(s);
                        }
                )
                .orElseGet(() -> {
                    //т к у нас PUT запрос, предполагающий, что создается новый ресурс или заполняется представлениие целевого ресурса
                    return studentRepository.save(modifiedStudent);
                });

        URI location = MvcUriComponentsBuilder
                .fromMethodName(this.getClass(), "getById", replacedStudent.getStudentId())
                .buildAndExpand(replacedStudent.getStudentId())
                .toUri();

        return  ResponseEntity.created(location).body(replacedStudent);
    }
}
