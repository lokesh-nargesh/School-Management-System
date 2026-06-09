import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { SchoolService } from '../../services/school.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-dashboard-student',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './dashboard-student.component.html',
  styleUrls: ['./dashboard-student.component.css']
})
export class DashboardStudentComponent implements OnInit {
  activeTab = 'overview';
  studentUser: any = null;
  profile: any = null;
  attendance: any[] = [];
  attendancePercent = 100.0;
  subjects: any[] = [];
  materials: any[] = [];
  assignments: any[] = [];
  exams: any[] = [];
  results: any[] = [];
  fees: any[] = [];
  notices: any[] = [];

  // Quiz execution states
  selectedSubjectId: number | null = null;
  selectedExam: any = null;
  examQuestions: any[] = [];
  studentAnswers: string[] = [];
  quizResult: any = null;

  // Assignment submission states
  selectedAssignment: any = null;
  submissionFileUrl = '';

  // AI Hub states
  chatMessage = '';
  chatHistory: { sender: string; text: string; timestamp: Date }[] = [];
  aiRecommendations: any = null;
  aiPrediction: any = null;

  constructor(
    private schoolService: SchoolService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.studentUser = this.authService.getUser();
    if (!this.studentUser) {
      this.router.navigate(['/login']);
      return;
    }
    
    // Welcome message in chat
    this.chatHistory.push({
      sender: 'ai',
      text: `Hello ${this.studentUser.name}! I am your AI Academic Assistant. How can I help you today?`,
      timestamp: new Date()
    });

    this.loadAllData();
  }

  loadAllData(): void {
    const sId = this.studentUser.id;

    // Profile
    this.schoolService.getStudentProfile(sId).subscribe(res => {
      this.profile = res;
      if (this.profile && this.profile.classRoom) {
        this.loadSubjectRelatedData();
      }
    });

    // Attendance
    this.schoolService.getStudentAttendance(sId).subscribe(res => {
      this.attendance = res;
      this.calculateAttendancePercentage();
    });

    // Results
    this.schoolService.getStudentResults(sId).subscribe(res => this.results = res);

    // Fees
    this.schoolService.getStudentFees(sId).subscribe(res => this.fees = res);

    // Notices
    this.schoolService.getStudentNotices().subscribe(res => this.notices = res);

    // Load AI metrics
    this.schoolService.getAiRecommendations(sId).subscribe(res => this.aiRecommendations = res);
    this.schoolService.predictPerformance(sId).subscribe(res => this.aiPrediction = res);
  }

  loadSubjectRelatedData(): void {
    const sId = this.studentUser.id;
    this.schoolService.getStudentSubjects(sId).subscribe(subs => {
      this.subjects = subs;
      if (this.subjects.length > 0) {
        this.selectedSubjectId = this.subjects[0].subjectId;
        this.loadSubjectMaterialsAndTasks();
      }
    });
  }

  loadSubjectMaterialsAndTasks(): void {
    if (!this.selectedSubjectId) return;

    this.schoolService.getStudyMaterials(this.selectedSubjectId).subscribe(res => this.materials = res);
    this.schoolService.getAssignments(this.selectedSubjectId).subscribe(res => this.assignments = res);
    this.schoolService.getExams(this.selectedSubjectId).subscribe(res => this.exams = res);
  }

  calculateAttendancePercentage(): void {
    if (this.attendance.length === 0) {
      this.attendancePercent = 100.0;
      return;
    }
    const presents = this.attendance.filter(a => a.status === 'PRESENT').length;
    this.attendancePercent = (presents / this.attendance.length) * 100.0;
  }

  onSubjectChange(event: any): void {
    this.selectedSubjectId = Number(event.target.value);
    this.loadSubjectMaterialsAndTasks();
  }

  // --- SUBMIT ASSIGNMENT ---
  selectAssignment(assign: any): void {
    this.selectedAssignment = assign;
    this.submissionFileUrl = '';
  }

  submitAssignment(): void {
    if (!this.selectedAssignment || !this.submissionFileUrl) return;
    this.schoolService.submitAssignment(
      this.selectedAssignment.assignmentId,
      this.studentUser.id,
      this.submissionFileUrl
    ).subscribe({
      next: (res) => {
        alert('Assignment submitted successfully!');
        this.selectedAssignment = null;
      },
      error: () => alert('Failed to submit assignment.')
    });
  }

  // --- QUIZ SYSTEM ---
  startExam(exam: any): void {
    this.selectedExam = exam;
    this.quizResult = null;
    this.schoolService.getExamQuestions(exam.examId).subscribe(questions => {
      this.examQuestions = questions;
      this.studentAnswers = new Array(questions.length).fill('');
    });
  }

  submitExam(): void {
    if (!this.selectedExam) return;
    this.schoolService.submitExam(
      this.selectedExam.examId,
      this.studentUser.id,
      this.studentAnswers
    ).subscribe({
      next: (res) => {
        this.quizResult = res;
        this.schoolService.getStudentResults(this.studentUser.id).subscribe(r => this.results = r);
      },
      error: () => alert('Failed to submit exam quiz.')
    });
  }

  closeExam(): void {
    this.selectedExam = null;
    this.examQuestions = [];
    this.quizResult = null;
  }

  // --- CHATBOT ---
  sendMessage(): void {
    if (!this.chatMessage.trim()) return;

    const userText = this.chatMessage;
    this.chatHistory.push({
      sender: 'user',
      text: userText,
      timestamp: new Date()
    });
    this.chatMessage = '';

    this.schoolService.askChatbot(userText).subscribe(res => {
      this.chatHistory.push({
        sender: 'ai',
        text: res.response,
        timestamp: new Date()
      });
    });
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
