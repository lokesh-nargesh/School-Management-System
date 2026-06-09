package com.school.management.repository;

import com.school.management.model.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    List<Notice> findAllByOrderByDateCreatedDesc();
    List<Notice> findByTargetRoleOrTargetRoleOrderByDateCreatedDesc(String targetRole1, String targetRole2);
}
