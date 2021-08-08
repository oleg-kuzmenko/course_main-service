package ru.digitalleague.backend.mainservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import ru.digitalleague.backend.javamodel.entities.Class;
import ru.digitalleague.backend.mainservice.exceptions.DepartmentNotFoundException;
import ru.digitalleague.backend.mainservice.repositories.ClassRepository;
import ru.digitalleague.backend.mainservice.repositories.DepartmentRepository;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/classes")
public class ClassController {

    @Autowired
    ClassRepository classRepository;

    @Autowired
    DepartmentRepository departmentRepository;

    @GetMapping("")
    public ResponseEntity<List<Class>> getAll() {
        List<Class> list = classRepository.findAll();
        if (list.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Class> getById(@PathVariable Integer id) {
        Optional<Class> item = classRepository.findById(id);
        return item.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

//    @PostMapping("")
//    public ResponseEntity<Class> addClass(@RequestBody Class item) {
//        Class savedClass = classRepository.save(item);
//        return null;
//    }

    @PostMapping("/department/{id}")
    public ResponseEntity<Class> addClass(@RequestBody Class item, @PathVariable Integer id) {
        Class savedClass = departmentRepository.findById(id)
                .map(
                        department -> {
                            item.setDepartment(department);
                            item.setDepartmentId(department.getDepartmentId());
                            return classRepository.save(item);
                        }
                )
                //.orElseThrow(() -> new RuntimeException(String.format("Department with %d is absent", id)));
                .orElseThrow(() -> new DepartmentNotFoundException(id));

        URI location = MvcUriComponentsBuilder
                .fromMethodName(this.getClass(), "getById", savedClass.getClassId())
                .buildAndExpand(savedClass.getClassId())
                .toUri();

        return ResponseEntity.created(location).body(savedClass);
    }
}
