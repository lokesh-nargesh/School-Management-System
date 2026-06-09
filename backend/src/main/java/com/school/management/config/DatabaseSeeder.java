package com.school.management.config;

import com.school.management.model.*;
import com.school.management.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private ClassRoomRepository classRoomRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByEmail("admin@school.com").isPresent()) {
            System.out.println("Dummy seed data already exists. Skipping seeder.");
            return;
        }

        System.out.println("Database is empty or missing admin. Seeding dummy school data...");

        try {
            // 1. Create ClassRooms
            ClassRoom classA = new ClassRoom();
            classA.setClassName("Class 10-A");
            classA.setSection("A");
            classA.setRoomNo("101");
            classA = classRoomRepository.saveAndFlush(classA);

            ClassRoom classB = new ClassRoom();
            classB.setClassName("Class 10-B");
            classB.setSection("B");
            classB.setRoomNo("102");
            classB = classRoomRepository.saveAndFlush(classB);

            // 2. Create Admin User
            User adminUser = new User();
            adminUser.setName("School Admin");
            adminUser.setEmail("admin@school.com");
            adminUser.setPassword(passwordEncoder.encode("admin123"));
            adminUser.setRole(Role.ADMIN);
            adminUser.setPhone("9999999999");
            adminUser = userRepository.saveAndFlush(adminUser);

            // 3. Create Teacher User
            User teacherUser = new User();
            teacherUser.setName("Jane Teacher");
            teacherUser.setEmail("teacher@school.com");
            teacherUser.setPassword(passwordEncoder.encode("teacher123"));
            teacherUser.setRole(Role.TEACHER);
            teacherUser.setPhone("8888888888");
            teacherUser = userRepository.saveAndFlush(teacherUser);

            Teacher teacher = new Teacher();
            teacher.setUser(teacherUser);
            teacher.setTeacherId(teacherUser.getId());
            teacher.setSpecialization("Mathematics");
            teacher.setSalary(50000.0);
            teacher.setJoiningDate(LocalDate.now());
            teacher = teacherRepository.saveAndFlush(teacher);

            // 4. Create Student User
            User studentUser = new User();
            studentUser.setName("John Student");
            studentUser.setEmail("student@school.com");
            studentUser.setPassword(passwordEncoder.encode("student123"));
            studentUser.setRole(Role.STUDENT);
            studentUser.setPhone("7777777777");
            studentUser = userRepository.saveAndFlush(studentUser);

            Student student = new Student();
            student.setUser(studentUser);
            student.setStudentId(studentUser.getId());
            student.setRollNo("S101");
            student.setParentName("Richard Parent");
            student.setParentPhone("6666666666");
            student.setClassRoom(classA);
            student.setAdmissionDate(LocalDate.now());
            student.setDob(LocalDate.of(2010, 5, 15));
            student.setAddress("123 School Lane, City");
            student = studentRepository.saveAndFlush(student);

            // 5. Create Parent User
            User parentUser = new User();
            parentUser.setName("Richard Parent");
            parentUser.setEmail("parent@school.com");
            parentUser.setPassword(passwordEncoder.encode("parent123"));
            parentUser.setRole(Role.PARENT);
            parentUser.setPhone("6666666666");
            parentUser = userRepository.saveAndFlush(parentUser);

            // 6. Create Subjects
            Subject math = new Subject();
            math.setSubjectName("Mathematics");
            math.setSubjectCode("MATH101");
            math.setClassRoom(classA);
            math.setTeacher(teacher);
            math = subjectRepository.saveAndFlush(math);

            Subject science = new Subject();
            science.setSubjectName("Science");
            science.setSubjectCode("SCI101");
            science.setClassRoom(classA);
            science.setTeacher(teacher);
            science = subjectRepository.saveAndFlush(science);

            // 7. Create a Notice
            Notice notice = new Notice();
            notice.setTitle("Annual Exam Schedule");
            notice.setContent("The Annual Examinations will begin on June 20th. Please prepare accordingly.");
            notice.setCreatedBy(adminUser);
            notice.setTargetRole("ALL");
            notice.setDateCreated(LocalDateTime.now());
            noticeRepository.saveAndFlush(notice);

            // 8. Create an Exam with Questions for the Student
            Exam exam = new Exam();
            exam.setTitle("Math Midterm Quiz");
            exam.setSubject(math);
            exam.setDate(LocalDate.now().plusDays(2));
            exam.setDurationMinutes(30);
            exam.setMaxMarks(10);
            exam.setType("MCQ");
            exam = examRepository.saveAndFlush(exam);

            Question q1 = new Question();
            q1.setExam(exam);
            q1.setQuestionText("What is the value of x in 2x + 5 = 15?");
            q1.setOptionA("2");
            q1.setOptionB("5");
            q1.setOptionC("10");
            q1.setOptionD("15");
            q1.setCorrectOption("B");
            q1.setMarks(5);
            questionRepository.saveAndFlush(q1);

            Question q2 = new Question();
            q2.setExam(exam);
            q2.setQuestionText("What is the area of a rectangle with length 10m and width 5m?");
            q2.setOptionA("15 sq m");
            q2.setOptionB("25 sq m");
            q2.setOptionC("50 sq m");
            q2.setOptionD("100 sq m");
            q2.setCorrectOption("C");
            q2.setMarks(5);
            questionRepository.saveAndFlush(q2);

            System.out.println("Dummy data seeded successfully!");
        } catch (Exception e) {
            System.err.println("Failed to seed dummy data: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}
