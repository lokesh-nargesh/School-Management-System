package com.school.management.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "students")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    @Id
    @Column(name = "student_id")
    private Long studentId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "student_id")
    private User user;

    @Column(name = "roll_no", unique = true, nullable = false)
    private String rollNo;

    @Column(name = "parent_name", nullable = false)
    private String parentName;

    @Column(name = "parent_phone", nullable = false)
    private String parentPhone;

    @ManyToOne
    @JoinColumn(name = "class_id")
    private ClassRoom classRoom;

    @Column(name = "admission_date")
    private LocalDate admissionDate = LocalDate.now();

    private LocalDate dob;

    private String address;
}
