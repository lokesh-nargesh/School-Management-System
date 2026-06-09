package com.school.management.repository;

import com.school.management.model.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    List<Assignment> findBySubjectSubjectId(Long subjectId);
}
