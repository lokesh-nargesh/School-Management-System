package com.school.management.controller;

import com.school.management.model.*;
import com.school.management.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/student")
@PreAuthorize("hasAnyRole('STUDENT', 'PARENT', 'ADMIN')")
@CrossOrigin(origins = "*")
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private SubjectRepository subjectRepository;

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
    private FeeRepository feeRepository;

    @Autowired
    private NoticeRepository noticeRepository;

    // --- STUDENT PROFILE ---
    @GetMapping("/{studentId}/profile")
    public ResponseEntity<Student> getProfile(@PathVariable Long studentId) {
        Student student = studentRepository.findById(studentId).orElse(null);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    // --- ATTENDANCE ---
    @GetMapping("/{studentId}/attendance")
    public List<Attendance> getAttendance(@PathVariable Long studentId) {
        return attendanceRepository.findByStudentStudentId(studentId);
    }

    // --- SUBJECTS ---
    @GetMapping("/{studentId}/subjects")
    public List<Subject> getSubjects(@PathVariable Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        if (student.getClassRoom() == null) {
            return List.of();
        }
        return subjectRepository.findByClassRoomClassId(student.getClassRoom().getClassId());
    }

    // --- STUDY MATERIALS ---
    @GetMapping("/materials/subject/{subjectId}")
    public List<StudyMaterial> getMaterials(@PathVariable Long subjectId) {
        return studyMaterialRepository.findBySubjectSubjectId(subjectId);
    }

    // --- ASSIGNMENTS ---
    @GetMapping("/assignments/subject/{subjectId}")
    public List<Assignment> getAssignments(@PathVariable Long subjectId) {
        return assignmentRepository.findBySubjectSubjectId(subjectId);
    }

    @PostMapping("/assignments/{assignmentId}/submit")
    public Submission submitAssignment(
            @PathVariable Long assignmentId,
            @RequestParam Long studentId,
            @RequestParam String fileUrl) {

        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Submission submission = submissionRepository
                .findByAssignmentAssignmentIdAndStudentStudentId(assignmentId, studentId)
                .orElse(new Submission());

        submission.setAssignment(assignment);
        submission.setStudent(student);
        submission.setFileUrl(fileUrl);
        submission.setSubmissionDate(LocalDateTime.now());
        submission.setStatus("SUBMITTED");

        return submissionRepository.save(submission);
    }

    // --- ONLINE QUIZ (MCQ) ---
    @GetMapping("/exams/subject/{subjectId}")
    public List<Exam> getExams(@PathVariable Long subjectId) {
        return examRepository.findBySubjectSubjectId(subjectId);
    }

    @GetMapping("/exams/{examId}/questions")
    public List<Question> getExamQuestions(@PathVariable Long examId) {
        return questionRepository.findByExamExamId(examId);
    }

    @PostMapping("/exams/{examId}/submit")
    public ResponseEntity<?> submitExam(
            @PathVariable Long examId,
            @RequestParam Long studentId,
            @RequestBody List<String> studentAnswers) {

        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new RuntimeException("Exam not found"));
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        List<Question> questions = questionRepository.findByExamExamId(examId);

        double marksObtained = 0.0;
        int maxIndex = Math.min(questions.size(), studentAnswers.size());
        for (int i = 0; i < maxIndex; i++) {
            if (questions.get(i).getCorrectOption().equalsIgnoreCase(studentAnswers.get(i))) {
                marksObtained += questions.get(i).getMarks();
            }
        }

        boolean passed = marksObtained >= (exam.getMaxMarks() * 0.40); // 40% pass mark

        ExamResult result = examResultRepository
                .findByExamExamIdAndStudentStudentId(examId, studentId)
                .orElse(new ExamResult());

        result.setExam(exam);
        result.setStudent(student);
        result.setMarksObtained(marksObtained);
        result.setPassed(passed);
        result.setSubmittedAt(LocalDateTime.now());

        examResultRepository.save(result);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{studentId}/results")
    public List<ExamResult> getResults(@PathVariable Long studentId) {
        return examResultRepository.findByStudentStudentId(studentId);
    }

    // --- FEES ---
    @GetMapping("/{studentId}/fees")
    public List<Fee> getFees(@PathVariable Long studentId) {
        return feeRepository.findByStudentStudentId(studentId);
    }

    // --- NOTICES ---
    @GetMapping("/notices")
    public List<Notice> getNotices() {
        return noticeRepository.findByTargetRoleOrTargetRoleOrderByDateCreatedDesc("STUDENT", "ALL");
    }
}
