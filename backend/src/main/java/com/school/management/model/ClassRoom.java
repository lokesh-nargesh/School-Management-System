package com.school.management.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "classes", uniqueConstraints = {@UniqueConstraint(columnNames = {"class_name", "section"})})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "class_id")
    private Long classId;

    @Column(name = "class_name", nullable = false)
    private String className;

    @Column(nullable = false)
    private String section;

    @Column(name = "room_no")
    private String roomNo;
}
