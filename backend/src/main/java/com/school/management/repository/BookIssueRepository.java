package com.school.management.repository;

import com.school.management.model.BookIssue;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookIssueRepository extends JpaRepository<BookIssue, Long> {
    List<BookIssue> findByUserId(Long userId);
}
