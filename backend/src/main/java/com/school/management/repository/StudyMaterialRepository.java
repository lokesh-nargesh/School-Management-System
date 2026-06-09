package com.school.management.repository;

import com.school.management.model.StudyMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface StudyMaterialRepository extends JpaRepository<StudyMaterial, Long> {
    List<StudyMaterial> findBySubjectSubjectId(Long subjectId);
}
