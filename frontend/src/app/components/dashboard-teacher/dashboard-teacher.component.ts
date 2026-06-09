import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { SchoolService } from '../../services/school.service';

@Component({
  selector: 'app-dashboard-teacher',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './dashboard-teacher.component.html'
})
export class DashboardTeacherComponent implements OnInit {
  teacherUser: any = null;

  constructor(
    private authService: AuthService,
    private schoolService: SchoolService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.teacherUser = this.authService.getUser();
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
