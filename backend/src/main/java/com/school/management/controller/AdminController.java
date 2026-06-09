package com.school.management.controller;

import com.school.management.model.*;
import com.school.management.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private ClassRoomRepository classRoomRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private FeeRepository feeRepository;

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private NoticeRepository noticeRepository;

    // --- CLASSES ---
    @GetMapping("/classes")
    public List<ClassRoom> getAllClasses() {
        return classRoomRepository.findAll();
    }

    @PostMapping("/classes")
    public ClassRoom createClass(@RequestBody ClassRoom classRoom) {
        return classRoomRepository.save(classRoom);
    }

    @DeleteMapping("/classes/{id}")
    public ResponseEntity<?> deleteClass(@PathVariable Long id) {
        classRoomRepository.deleteById(id);
        return ResponseEntity.ok("Class deleted");
    }

    // --- SUBJECTS ---
    @GetMapping("/subjects")
    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    @PostMapping("/subjects")
    public Subject createSubject(@RequestBody Subject subject) {
        return subjectRepository.save(subject);
    }

    // --- USERS LIST ---
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // --- STUDENTS LIST ---
    @GetMapping("/students")
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // --- TEACHERS LIST ---
    @GetMapping("/teachers")
    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    // --- FEES MANAGEMENT ---
    @GetMapping("/fees")
    public List<Fee> getAllFees() {
        return feeRepository.findAll();
    }

    @PostMapping("/fees")
    public Fee createFeeRecord(@RequestBody Fee fee) {
        return feeRepository.save(fee);
    }

    // --- TRANSPORT ---
    @GetMapping("/routes")
    public List<Route> getAllRoutes() {
        return routeRepository.findAll();
    }

    @PostMapping("/routes")
    public Route createRoute(@RequestBody Route route) {
        return routeRepository.save(route);
    }

    @GetMapping("/vehicles")
    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    @PostMapping("/vehicles")
    public Vehicle createVehicle(@RequestBody Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    // --- NOTICES ---
    @PostMapping("/notices")
    public Notice createNotice(@RequestBody Notice notice) {
        return noticeRepository.save(notice);
    }
}
