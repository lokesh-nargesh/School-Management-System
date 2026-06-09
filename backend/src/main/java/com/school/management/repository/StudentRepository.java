package com.school.management.repository;

import com.school.management.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByClassRoomClassId(Long classId);
}
