package com.school.management.repository;

import com.school.management.model.Fee;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FeeRepository extends JpaRepository<Fee, Long> {
    List<Fee> findByStudentStudentId(Long studentId);
}
