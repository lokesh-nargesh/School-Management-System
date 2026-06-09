package com.school.management.repository;

import com.school.management.model.ExamResult;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ExamResultRepository extends JpaRepository<ExamResult, Long> {
    List<ExamResult> findByStudentStudentId(Long studentId);
    List<ExamResult> findByExamExamId(Long examId);
    Optional<ExamResult> findByExamExamIdAndStudentStudentId(Long examId, Long studentId);
}
