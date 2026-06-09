import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class SchoolService {
  private baseAdminUrl = 'http://localhost:8080/api/admin';
  private baseTeacherUrl = 'http://localhost:8080/api/teacher';
  private baseStudentUrl = 'http://localhost:8080/api/student';
  private baseAiUrl = 'http://localhost:8080/api/ai';

  constructor(private http: HttpClient, private authService: AuthService) {}

  private getHeaders(): HttpHeaders {
    const token = this.authService.getToken();
    return new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });
  }

  // --- ADMIN METHODS ---
  getClasses(): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseAdminUrl}/classes`, { headers: this.getHeaders() });
  }

  createClass(classData: any): Observable<any> {
    return this.http.post<any>(`${this.baseAdminUrl}/classes`, classData, { headers: this.getHeaders() });
  }

  deleteClass(id: number): Observable<any> {
    return this.http.delete<any>(`${this.baseAdminUrl}/classes/${id}`, { headers: this.getHeaders() });
  }

  getSubjects(): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseAdminUrl}/subjects`, { headers: this.getHeaders() });
  }

  createSubject(subjectData: any): Observable<any> {
    return this.http.post<any>(`${this.baseAdminUrl}/subjects`, subjectData, { headers: this.getHeaders() });
  }

  getStudents(): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseAdminUrl}/students`, { headers: this.getHeaders() });
  }

  getTeachers(): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseAdminUrl}/teachers`, { headers: this.getHeaders() });
  }

  getFees(): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseAdminUrl}/fees`, { headers: this.getHeaders() });
  }

  createFee(feeData: any): Observable<any> {
    return this.http.post<any>(`${this.baseAdminUrl}/fees`, feeData, { headers: this.getHeaders() });
  }

  // --- TEACHER METHODS ---
  markAttendance(attendanceList: any[]): Observable<any> {
    return this.http.post<any>(`${this.baseTeacherUrl}/attendance`, attendanceList, { headers: this.getHeaders() });
  }

  uploadMaterial(material: any): Observable<any> {
    return this.http.post<any>(`${this.baseTeacherUrl}/materials`, material, { headers: this.getHeaders() });
  }

  createAssignment(assignment: any): Observable<any> {
    return this.http.post<any>(`${this.baseTeacherUrl}/assignments`, assignment, { headers: this.getHeaders() });
  }

  getSubmissions(assignmentId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseTeacherUrl}/submissions/assignment/${assignmentId}`, { headers: this.getHeaders() });
  }

  gradeSubmission(submissionId: number, marks: number, feedback: string): Observable<any> {
    return this.http.put<any>(`${this.baseTeacherUrl}/submissions/${submissionId}/grade?marks=${marks}&feedback=${feedback}`, {}, { headers: this.getHeaders() });
  }

  createExam(exam: any): Observable<any> {
    return this.http.post<any>(`${this.baseTeacherUrl}/exams`, exam, { headers: this.getHeaders() });
  }

  addQuestion(examId: number, question: any): Observable<any> {
    return this.http.post<any>(`${this.baseTeacherUrl}/exams/${examId}/questions`, question, { headers: this.getHeaders() });
  }

  // --- STUDENT METHODS ---
  getStudentProfile(studentId: number): Observable<any> {
    return this.http.get<any>(`${this.baseStudentUrl}/${studentId}/profile`, { headers: this.getHeaders() });
  }

  getStudentAttendance(studentId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseStudentUrl}/${studentId}/attendance`, { headers: this.getHeaders() });
  }

  getStudentSubjects(studentId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseStudentUrl}/${studentId}/subjects`, { headers: this.getHeaders() });
  }

  getStudyMaterials(subjectId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseStudentUrl}/materials/subject/${subjectId}`, { headers: this.getHeaders() });
  }

  getAssignments(subjectId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseStudentUrl}/assignments/subject/${subjectId}`, { headers: this.getHeaders() });
  }

  submitAssignment(assignmentId: number, studentId: number, fileUrl: string): Observable<any> {
    return this.http.post<any>(`${this.baseStudentUrl}/assignments/${assignmentId}/submit?studentId=${studentId}&fileUrl=${fileUrl}`, {}, { headers: this.getHeaders() });
  }

  getExams(subjectId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseStudentUrl}/exams/subject/${subjectId}`, { headers: this.getHeaders() });
  }

  getExamQuestions(examId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseStudentUrl}/exams/${examId}/questions`, { headers: this.getHeaders() });
  }

  submitExam(examId: number, studentId: number, answers: string[]): Observable<any> {
    return this.http.post<any>(`${this.baseStudentUrl}/exams/${examId}/submit?studentId=${studentId}`, answers, { headers: this.getHeaders() });
  }

  getStudentResults(studentId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseStudentUrl}/${studentId}/results`, { headers: this.getHeaders() });
  }

  getStudentFees(studentId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseStudentUrl}/${studentId}/fees`, { headers: this.getHeaders() });
  }

  getStudentNotices(): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseStudentUrl}/notices`, { headers: this.getHeaders() });
  }

  // --- AI METHODS ---
  askChatbot(message: string): Observable<any> {
    return this.http.post<any>(`${this.baseAiUrl}/chatbot`, { message }, { headers: this.getHeaders() });
  }

  getAiRecommendations(studentId: number): Observable<any> {
    return this.http.get<any>(`${this.baseAiUrl}/recommendations?studentId=${studentId}`, { headers: this.getHeaders() });
  }

  predictPerformance(studentId: number): Observable<any> {
    return this.http.get<any>(`${this.baseAiUrl}/predict?studentId=${studentId}`, { headers: this.getHeaders() });
  }
}
