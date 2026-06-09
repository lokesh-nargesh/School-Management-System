package com.school.management.repository;

import com.school.management.model.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ExamRepository extends JpaRepository<Exam, Long> {
    List<Exam> findBySubjectSubjectId(Long subjectId);
}
