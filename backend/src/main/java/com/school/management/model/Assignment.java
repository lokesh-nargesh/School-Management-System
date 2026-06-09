package com.school.management.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "assignments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Assignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "assignment_id")
    private Long assignmentId;

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(name = "due_date", nullable = false)
    private LocalDateTime dueDate;

    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @Column(name = "file_url")
    private String fileUrl;

    @Column(name = "max_marks", nullable = false)
    private Integer maxMarks;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private Teacher createdBy;
}
