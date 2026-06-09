-- Create Database
CREATE DATABASE IF NOT EXISTS school_db;
USE school_db;

-- 1. Users Table
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    phone VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Classes Table
CREATE TABLE IF NOT EXISTS classes (
    class_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    class_name VARCHAR(50) NOT NULL,
    section VARCHAR(20) NOT NULL,
    room_no VARCHAR(20),
    UNIQUE(class_name, section)
);

-- 3. Students Table
CREATE TABLE IF NOT EXISTS students (
    student_id BIGINT PRIMARY KEY,
    roll_no VARCHAR(50) UNIQUE NOT NULL,
    parent_name VARCHAR(100) NOT NULL,
    parent_phone VARCHAR(20) NOT NULL,
    class_id BIGINT,
    admission_date DATE,
    dob DATE,
    address TEXT,
    FOREIGN KEY (student_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (class_id) REFERENCES classes(class_id) ON DELETE SET NULL
);

-- 4. Teachers Table
CREATE TABLE IF NOT EXISTS teachers (
    teacher_id BIGINT PRIMARY KEY,
    specialization VARCHAR(100),
    salary DECIMAL(10, 2),
    joining_date DATE,
    FOREIGN KEY (teacher_id) REFERENCES users(id) ON DELETE CASCADE
);

-- 5. Subjects Table
CREATE TABLE IF NOT EXISTS subjects (
    subject_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    subject_name VARCHAR(100) NOT NULL,
    subject_code VARCHAR(20) UNIQUE NOT NULL,
    class_id BIGINT,
    teacher_id BIGINT,
    FOREIGN KEY (class_id) REFERENCES classes(class_id) ON DELETE SET NULL,
    FOREIGN KEY (teacher_id) REFERENCES teachers(teacher_id) ON DELETE SET NULL
);

-- 6. Timetable Table
CREATE TABLE IF NOT EXISTS timetables (
    timetable_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    class_id BIGINT NOT NULL,
    subject_id BIGINT NOT NULL,
    day_of_week VARCHAR(20) NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    FOREIGN KEY (class_id) REFERENCES classes(class_id) ON DELETE CASCADE,
    FOREIGN KEY (subject_id) REFERENCES subjects(subject_id) ON DELETE CASCADE
);

-- 7. Attendance Table
CREATE TABLE IF NOT EXISTS attendance (
    attendance_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id BIGINT NOT NULL,
    date DATE NOT NULL,
    status VARCHAR(20) NOT NULL, -- PRESENT, ABSENT, LATE
    marked_by BIGINT,
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE,
    FOREIGN KEY (marked_by) REFERENCES teachers(teacher_id) ON DELETE SET NULL,
    UNIQUE(student_id, date)
);

-- 8. Study Materials Table
CREATE TABLE IF NOT EXISTS study_materials (
    material_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(150) NOT NULL,
    description TEXT,
    subject_id BIGINT NOT NULL,
    file_url VARCHAR(255) NOT NULL,
    file_type VARCHAR(50) NOT NULL, -- PDF, PPT, VIDEO, etc.
    uploaded_by BIGINT,
    upload_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (subject_id) REFERENCES subjects(subject_id) ON DELETE CASCADE,
    FOREIGN KEY (uploaded_by) REFERENCES teachers(teacher_id) ON DELETE SET NULL
);

-- 9. Assignments Table
CREATE TABLE IF NOT EXISTS assignments (
    assignment_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(150) NOT NULL,
    description TEXT,
    due_date TIMESTAMP NOT NULL,
    subject_id BIGINT NOT NULL,
    file_url VARCHAR(255),
    max_marks INT NOT NULL,
    created_by BIGINT,
    FOREIGN KEY (subject_id) REFERENCES subjects(subject_id) ON DELETE CASCADE,
    FOREIGN KEY (created_by) REFERENCES teachers(teacher_id) ON DELETE SET NULL
);

-- 10. Submissions Table
CREATE TABLE IF NOT EXISTS submissions (
    submission_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    assignment_id BIGINT NOT NULL,
    student_id BIGINT NOT NULL,
    submission_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    file_url VARCHAR(255) NOT NULL,
    marks_obtained INT,
    feedback TEXT,
    status VARCHAR(20) NOT NULL DEFAULT 'SUBMITTED', -- SUBMITTED, GRADED
    FOREIGN KEY (assignment_id) REFERENCES assignments(assignment_id) ON DELETE CASCADE,
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE,
    UNIQUE(assignment_id, student_id)
);

-- 11. Exams Table
CREATE TABLE IF NOT EXISTS exams (
    exam_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(150) NOT NULL,
    subject_id BIGINT NOT NULL,
    date DATE NOT NULL,
    duration_minutes INT NOT NULL,
    max_marks INT NOT NULL,
    type VARCHAR(20) NOT NULL, -- MCQ, WRITTEN
    FOREIGN KEY (subject_id) REFERENCES subjects(subject_id) ON DELETE CASCADE
);

-- 12. Questions Table (For MCQ Exams)
CREATE TABLE IF NOT EXISTS questions (
    question_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    exam_id BIGINT NOT NULL,
    question_text TEXT NOT NULL,
    option_a VARCHAR(255) NOT NULL,
    option_b VARCHAR(255) NOT NULL,
    option_c VARCHAR(255) NOT NULL,
    option_d VARCHAR(255) NOT NULL,
    correct_option CHAR(1) NOT NULL, -- A, B, C, D
    marks INT DEFAULT 1,
    FOREIGN KEY (exam_id) REFERENCES exams(exam_id) ON DELETE CASCADE
);

-- 13. Exam Results Table
CREATE TABLE IF NOT EXISTS exam_results (
    result_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    exam_id BIGINT NOT NULL,
    student_id BIGINT NOT NULL,
    marks_obtained DECIMAL(5, 2) NOT NULL,
    passed BOOLEAN NOT NULL,
    submitted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (exam_id) REFERENCES exams(exam_id) ON DELETE CASCADE,
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE,
    UNIQUE(exam_id, student_id)
);

-- 14. Fees Table
CREATE TABLE IF NOT EXISTS fees (
    fee_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id BIGINT NOT NULL,
    term VARCHAR(50) NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'UNPAID', -- PAID, UNPAID, PARTIAL
    payment_date TIMESTAMP NULL,
    payment_method VARCHAR(50),
    receipt_no VARCHAR(100) UNIQUE,
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE
);

-- 15. Notices Table
CREATE TABLE IF NOT EXISTS notices (
    notice_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(150) NOT NULL,
    content TEXT NOT NULL,
    target_role VARCHAR(20) DEFAULT 'ALL', -- ALL, TEACHER, STUDENT, PARENT
    created_by BIGINT,
    date_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE SET NULL
);

-- 16. Messages Table (Chat)
CREATE TABLE IF NOT EXISTS messages (
    message_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    sender_id BIGINT NOT NULL,
    receiver_id BIGINT NOT NULL,
    content TEXT NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_read BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (sender_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (receiver_id) REFERENCES users(id) ON DELETE CASCADE
);

-- 17. Books Table (Library)
CREATE TABLE IF NOT EXISTS books (
    book_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(150) NOT NULL,
    author VARCHAR(100) NOT NULL,
    isbn VARCHAR(50) UNIQUE,
    quantity INT NOT NULL DEFAULT 1,
    rack_no VARCHAR(20)
);

-- 18. Book Issues Table
CREATE TABLE IF NOT EXISTS book_issues (
    issue_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    book_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    issue_date DATE NOT NULL,
    due_date DATE NOT NULL,
    return_date DATE NULL,
    fine_amount DECIMAL(5, 2) DEFAULT 0.0,
    status VARCHAR(20) DEFAULT 'ISSUED', -- ISSUED, RETURNED, OVERDUE
    FOREIGN KEY (book_id) REFERENCES books(book_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- 19. Transport Routes
CREATE TABLE IF NOT EXISTS routes (
    route_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    route_name VARCHAR(100) NOT NULL,
    start_point VARCHAR(100) NOT NULL,
    end_point VARCHAR(100) NOT NULL,
    cost DECIMAL(8, 2) NOT NULL
);

-- 20. Vehicles
CREATE TABLE IF NOT EXISTS vehicles (
    vehicle_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    register_no VARCHAR(50) UNIQUE NOT NULL,
    driver_name VARCHAR(100) NOT NULL,
    driver_phone VARCHAR(20) NOT NULL,
    route_id BIGINT,
    FOREIGN KEY (route_id) REFERENCES routes(route_id) ON DELETE SET NULL
);

-- Insert Default Admin (Password is 'admin123' BCrypt hashed)
-- Note: A BCrypt encoder will be used in Java. The hash below is for 'admin123'.
INSERT INTO users (name, email, password, role, phone)
VALUES ('System Admin', 'admin@school.com', '$2a$10$8.UnVuG9HHgffUDAlk8GP.3nS8rL.S1dZ2nZ6.U1H97U.n6m7h22a', 'ADMIN', '1234567890')
ON DUPLICATE KEY UPDATE id=id;
