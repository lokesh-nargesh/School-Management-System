package com.school.management.controller;

import com.school.management.dto.Dtos.*;
import com.school.management.model.*;
import com.school.management.repository.*;
import com.school.management.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private ClassRoomRepository classRoomRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);

        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(new LoginResponse(
                jwt, user.getEmail(), user.getName(), user.getRole().name(), user.getId()
        ));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity.badRequest().body("Email address already in use!");
        }

        Role role;
        try {
            role = Role.valueOf(registerRequest.getRole().toUpperCase());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid role selected");
        }

        User user = new User();
        user.setName(registerRequest.getName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole(role);
        user.setPhone(registerRequest.getPhone());
        user = userRepository.save(user);

        if (role == Role.STUDENT) {
            Student student = new Student();
            student.setUser(user);
            student.setStudentId(user.getId());
            student.setRollNo(registerRequest.getRollNo() != null ? registerRequest.getRollNo() : "R-" + user.getId());
            student.setParentName(registerRequest.getParentName() != null ? registerRequest.getParentName() : "Parent of " + user.getName());
            student.setParentPhone(registerRequest.getParentPhone() != null ? registerRequest.getParentPhone() : "1234567890");
            student.setAdmissionDate(LocalDate.now());

            if (registerRequest.getClassId() != null) {
                ClassRoom classRoom = classRoomRepository.findById(registerRequest.getClassId()).orElse(null);
                student.setClassRoom(classRoom);
            }
            studentRepository.save(student);
        } else if (role == Role.TEACHER) {
            Teacher teacher = new Teacher();
            teacher.setUser(user);
            teacher.setTeacherId(user.getId());
            teacher.setSpecialization(registerRequest.getSpecialization());
            teacher.setSalary(registerRequest.getSalary() != null ? registerRequest.getSalary() : 30000.0);
            teacher.setJoiningDate(LocalDate.now());
            teacherRepository.save(teacher);
        }

        return ResponseEntity.ok("User registered successfully!");
    }
}
