package com.school.management.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "exam_results", uniqueConstraints = {@UniqueConstraint(columnNames = {"exam_id", "student_id"})})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "result_id")
    private Long resultId;

    @ManyToOne
    @JoinColumn(name = "exam_id", nullable = false)
    private Exam exam;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(name = "marks_obtained", nullable = false)
    private Double marksObtained;

    @Column(nullable = false)
    private Boolean passed;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt = LocalDateTime.now();
}
