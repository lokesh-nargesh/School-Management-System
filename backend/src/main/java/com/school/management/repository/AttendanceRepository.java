package com.school.management.repository;

import com.school.management.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByStudentStudentId(Long studentId);
    List<Attendance> findByStudentStudentIdAndDateBetween(Long studentId, LocalDate start, LocalDate end);
    Optional<Attendance> findByStudentStudentIdAndDate(Long studentId, LocalDate date);
}
