package com.school.management.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "teachers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Teacher {
    @Id
    @Column(name = "teacher_id")
    private Long teacherId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "teacher_id")
    private User user;

    private String specialization;

    private Double salary;

    @Column(name = "joining_date")
    private LocalDate joiningDate = LocalDate.now();
}
