package com.school.management.repository;

import com.school.management.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
    List<Subject> findByClassRoomClassId(Long classId);
    List<Subject> findByTeacherTeacherId(Long teacherId);
}
