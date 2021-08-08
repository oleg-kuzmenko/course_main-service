package ru.digitalleague.backend.mainservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.digitalleague.backend.javamodel.entities.Department;
import ru.digitalleague.backend.mainservice.repositories.DepartmentRepository;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/departments")
public class DepartmentController {

    @Autowired
    DepartmentRepository departmentRepository;

    @GetMapping("/{id}")
    public ResponseEntity<Department> getById(@PathVariable Integer id) {
        Optional<Department> item = departmentRepository.findById(id);
        return item.map(department -> ResponseEntity.ok(department))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("")
    public ResponseEntity<Department> addDepartment(@RequestBody Department department) {
        Department savedDepartment = departmentRepository.save(department);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedDepartment.getDepartmentId())
                .toUri();

        return ResponseEntity.created(location)
                .body(savedDepartment);
    }
}
