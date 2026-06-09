package com.school.management.repository;

import com.school.management.model.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    List<Submission> findByStudentStudentId(Long studentId);
    List<Submission> findByAssignmentAssignmentId(Long assignmentId);
    Optional<Submission> findByAssignmentAssignmentIdAndStudentStudentId(Long assignmentId, Long studentId);
}
