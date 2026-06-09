import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { SchoolService } from '../../services/school.service';

@Component({
  selector: 'app-dashboard-admin',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './dashboard-admin.component.html'
})
export class DashboardAdminComponent implements OnInit {
  adminUser: any = null;

  constructor(
    private authService: AuthService,
    private schoolService: SchoolService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.adminUser = this.authService.getUser();
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
