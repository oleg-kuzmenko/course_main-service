package ru.digitalleague.backend.mainservice.specification;

import org.springframework.data.jpa.domain.Specification;
import ru.digitalleague.backend.javamodel.entities.Student;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Optional;

public class StudentSpecification {

    public static Specification<Student> genderEqual(Optional<String> gender) {
        return new Specification<Student>() {
            @Override
            public Predicate toPredicate(Root<Student> root,
                                         CriteriaQuery<?> criteriaQuery,
                                         CriteriaBuilder criteriaBuilder) {
                return gender.map(g -> criteriaBuilder.equal(root.get("gender"), g))
                        .orElse(criteriaBuilder.and());
            }
        };
    }

    public static Specification<Student> firstNameLike(String firsName) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.like(root.get("firstName"), "%" + firsName + "%");
    }

}
