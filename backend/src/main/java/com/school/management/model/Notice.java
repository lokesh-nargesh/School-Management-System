package com.school.management.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "notices")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_id")
    private Long noticeId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "target_role")
    private String targetRole = "ALL"; // ALL, TEACHER, STUDENT, PARENT

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    @Column(name = "date_created")
    private LocalDateTime dateCreated = LocalDateTime.now();
}
