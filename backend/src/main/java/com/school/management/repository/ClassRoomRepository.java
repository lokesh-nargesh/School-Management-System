package com.school.management.repository;

import com.school.management.model.ClassRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ClassRoomRepository extends JpaRepository<ClassRoom, Long> {
    Optional<ClassRoom> findByClassNameAndSection(String className, String section);
}
