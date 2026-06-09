package com.school.management.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "submissions", uniqueConstraints = {@UniqueConstraint(columnNames = {"assignment_id", "student_id"})})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Submission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "submission_id")
    private Long submissionId;

    @ManyToOne
    @JoinColumn(name = "assignment_id", nullable = false)
    private Assignment assignment;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(name = "submission_date")
    private LocalDateTime submissionDate = LocalDateTime.now();

    @Column(name = "file_url", nullable = false)
    private String fileUrl;

    @Column(name = "marks_obtained")
    private Integer marksObtained;

    private String feedback;

    @Column(nullable = false)
    private String status = "SUBMITTED"; // SUBMITTED, GRADED
}
