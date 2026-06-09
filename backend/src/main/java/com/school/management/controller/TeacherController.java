package com.school.management.controller;

import com.school.management.model.*;
import com.school.management.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/teacher")
@PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
@CrossOrigin(origins = "*")
public class TeacherController {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudyMaterialRepository studyMaterialRepository;

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ExamResultRepository examResultRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    // --- MARK ATTENDANCE ---
    @PostMapping("/attendance")
    public ResponseEntity<?> markAttendance(@RequestBody List<Attendance> attendanceList) {
        for (Attendance att : attendanceList) {
            Student student = studentRepository.findById(att.getStudent().getStudentId()).orElse(null);
            if (student == null) continue;

            Attendance existing = attendanceRepository
                .findByStudentStudentIdAndDate(student.getStudentId(), att.getDate() != null ? att.getDate() : LocalDate.now())
                .orElse(null);

            if (existing != null) {
                existing.setStatus(att.getStatus());
                attendanceRepository.save(existing);
            } else {
                att.setStudent(student);
                if (att.getDate() == null) att.setDate(LocalDate.now());
                attendanceRepository.save(att);
            }
        }
        return ResponseEntity.ok("Attendance marked successfully");
    }

    // --- STUDY MATERIALS ---
    @PostMapping("/materials")
    public StudyMaterial uploadMaterial(@RequestBody StudyMaterial material) {
        return studyMaterialRepository.save(material);
    }

    @GetMapping("/materials/subject/{subjectId}")
    public List<StudyMaterial> getMaterialsBySubject(@PathVariable Long subjectId) {
        return studyMaterialRepository.findBySubjectSubjectId(subjectId);
    }

    // --- ASSIGNMENTS ---
    @PostMapping("/assignments")
    public Assignment createAssignment(@RequestBody Assignment assignment) {
        return assignmentRepository.save(assignment);
    }

    @GetMapping("/submissions/assignment/{assignmentId}")
    public List<Submission> getSubmissionsForAssignment(@PathVariable Long assignmentId) {
        return submissionRepository.findByAssignmentAssignmentId(assignmentId);
    }

    @PutMapping("/submissions/{submissionId}/grade")
    public Submission gradeSubmission(
            @PathVariable Long submissionId,
            @RequestParam Integer marks,
            @RequestParam String feedback) {
        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Submission not found"));
        submission.setMarksObtained(marks);
        submission.setFeedback(feedback);
        submission.setStatus("GRADED");
        return submissionRepository.save(submission);
    }

    // --- EXAMS ---
    @PostMapping("/exams")
    public Exam createExam(@RequestBody Exam exam) {
        return examRepository.save(exam);
    }

    @PostMapping("/exams/{examId}/questions")
    public Question addQuestionToExam(@PathVariable Long examId, @RequestBody Question question) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new RuntimeException("Exam not found"));
        question.setExam(exam);
        return questionRepository.save(question);
    }

    @GetMapping("/exams/{examId}/results")
    public List<ExamResult> getExamResults(@PathVariable Long examId) {
        return examResultRepository.findByExamExamId(examId);
    }
}
