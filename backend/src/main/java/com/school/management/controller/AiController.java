package com.school.management.controller;

import com.school.management.model.*;
import com.school.management.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "*")
public class AiController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ExamResultRepository examResultRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private StudyMaterialRepository studyMaterialRepository;

    @Value("${gemini.api.key:}")
    private String geminiApiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    // --- AI CHATBOT ---
    @PostMapping("/chatbot")
    public ResponseEntity<Map<String, String>> askChatbot(@RequestBody Map<String, String> request) {
        String prompt = request.get("message");
        if (prompt == null || prompt.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("response", "Message cannot be empty"));
        }

        String reply;
        if (geminiApiKey != null && !geminiApiKey.trim().isEmpty()) {
            try {
                String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + geminiApiKey;
                
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                // Prepare request body for Gemini API
                Map<String, Object> contentsPart = Map.of("text", "You are a friendly academic AI assistant for Smart School Management System. Answer this query: " + prompt);
                Map<String, Object> contents = Map.of("parts", List.of(contentsPart));
                Map<String, Object> body = Map.of("contents", List.of(contents));

                HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
                ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);

                // Extract reply text from Gemini response structure
                List candidates = (List) response.getBody().get("candidates");
                Map candidate = (Map) candidates.get(0);
                Map content = (Map) candidate.get("content");
                List parts = (List) content.get("parts");
                Map part = (Map) parts.get(0);
                reply = (String) part.get("text");
            } catch (Exception e) {
                reply = getFallbackChatbotReply(prompt) + " (AI API connection failed, showing fallback answer)";
            }
        } else {
            reply = getFallbackChatbotReply(prompt);
        }

        return ResponseEntity.ok(Map.of("response", reply));
    }

    // --- AI STUDY RECOMMENDER ---
    @GetMapping("/recommendations")
    public ResponseEntity<?> getRecommendations(@RequestParam Long studentId) {
        Student student = studentRepository.findById(studentId).orElse(null);
        if (student == null) {
            return ResponseEntity.badRequest().body("Student not found");
        }

        List<ExamResult> results = examResultRepository.findByStudentStudentId(studentId);
        Subject weakSubject = null;
        double lowestScore = 100.0;

        for (ExamResult result : results) {
            double scorePercentage = (result.getMarksObtained() / result.getExam().getMaxMarks()) * 100.0;
            if (scorePercentage < lowestScore) {
                lowestScore = scorePercentage;
                weakSubject = result.getExam().getSubject();
            }
        }

        Map<String, Object> recommendationData = new HashMap<>();
        recommendationData.put("studentName", student.getUser().getName());

        if (weakSubject != null && lowestScore < 60.0) {
            List<StudyMaterial> materials = studyMaterialRepository.findBySubjectSubjectId(weakSubject.getSubjectId());
            recommendationData.put("weakSubject", weakSubject.getSubjectName());
            recommendationData.put("lowestScore", lowestScore);
            recommendationData.put("recommendationReason", "We noticed your score in " + weakSubject.getSubjectName() + " is " + String.format("%.1f", lowestScore) + "%, which is below average. We recommend reviewing the following study materials:");
            recommendationData.put("materials", materials);
        } else {
            recommendationData.put("weakSubject", "None (Performing Well)");
            recommendationData.put("recommendationReason", "Keep up the great work! You are performing well across all subjects. Here are some advanced reading materials:");
            recommendationData.put("materials", studyMaterialRepository.findAll()); // Recommend general/advanced materials
        }

        return ResponseEntity.ok(recommendationData);
    }

    // --- AI PERFORMANCE RISK PREDICTOR ---
    @GetMapping("/predict")
    public ResponseEntity<?> predictPerformance(@RequestParam Long studentId) {
        Student student = studentRepository.findById(studentId).orElse(null);
        if (student == null) {
            return ResponseEntity.badRequest().body("Student not found");
        }

        List<ExamResult> results = examResultRepository.findByStudentStudentId(studentId);
        List<Attendance> attendances = attendanceRepository.findByStudentStudentId(studentId);

        double totalMarksObtained = 0.0;
        double totalMaxMarks = 0.0;
        for (ExamResult r : results) {
            totalMarksObtained += r.getMarksObtained();
            totalMaxMarks += r.getExam().getMaxMarks();
        }

        double averageScore = totalMaxMarks > 0 ? (totalMarksObtained / totalMaxMarks) * 100.0 : 70.0; // default to 70% if no exams

        double presentDays = 0;
        for (Attendance a : attendances) {
            if ("PRESENT".equalsIgnoreCase(a.getStatus())) {
                presentDays++;
            }
        }
        double attendanceRate = !attendances.isEmpty() ? (presentDays / attendances.size()) * 100.0 : 85.0; // default to 85% if no records

        String riskLevel;
        List<String> improvementPlan = new ArrayList<>();

        if (attendanceRate < 75.0 || averageScore < 50.0) {
            riskLevel = "HIGH RISK";
            improvementPlan.add("Mandatory counseling session with the class teacher and parent.");
            improvementPlan.add("Remedial classes for weak subjects (Score: " + String.format("%.1f", averageScore) + "%).");
            improvementPlan.add("Weekly attendance tracking to bring attendance above 75% (Current: " + String.format("%.1f", attendanceRate) + "%).");
        } else if (attendanceRate < 80.0 || averageScore < 65.0) {
            riskLevel = "MEDIUM RISK";
            improvementPlan.add("Attend extra support workshops for subjects scoring below 60%.");
            improvementPlan.add("Parent-teacher discussion to track home study schedule.");
            improvementPlan.add("Ensure consistent attendance in morning lecture sessions.");
        } else {
            riskLevel = "LOW RISK (EXCELLENT / STABLE)";
            improvementPlan.add("Participate in advanced peer-learning or mentoring groups.");
            improvementPlan.add("Maintain current study schedules and study material review habits.");
        }

        Map<String, Object> prediction = new HashMap<>();
        prediction.put("studentName", student.getUser().getName());
        prediction.put("attendanceRate", attendanceRate);
        prediction.put("averageExamScore", averageScore);
        prediction.put("riskLevel", riskLevel);
        prediction.put("improvementPlan", improvementPlan);

        return ResponseEntity.ok(prediction);
    }

    private String getFallbackChatbotReply(String message) {
        String lower = message.toLowerCase();
        if (lower.contains("admission")) {
            return "Admissions for the new academic year are currently open! You can fill out the admission form on our portal or contact the administration desk at admin@school.com for details.";
        } else if (lower.contains("fee") || lower.contains("payment")) {
            return "Fees can be paid online via our Fee Portal using NetBanking, UPI, or Card. If you face payment issues, please write to fees@school.com with your receipt number.";
        } else if (lower.contains("exam") || lower.contains("quiz") || lower.contains("test")) {
            return "Online tests are MCQ-based and time-limited. Once started, you must complete it within the allocated duration. Results are automatically generated upon submission.";
        } else if (lower.contains("holiday") || lower.contains("calendar")) {
            return "Please check the School Notice Board for the official holiday list and upcoming event calendars.";
        } else if (lower.contains("timetable") || lower.contains("schedule")) {
            return "Your class timetable can be viewed in the 'Timetable' section on the student or teacher dashboard.";
        } else {
            return "Thank you for contacting the Smart School E-Learning Support Bot. You can ask me about admissions, online exams, fee structure, class timetables, or attendance tracker.";
        }
    }
}
