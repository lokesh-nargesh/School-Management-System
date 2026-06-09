import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { SchoolService } from '../../services/school.service';

@Component({
  selector: 'app-dashboard-parent',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './dashboard-parent.component.html'
})
export class DashboardParentComponent implements OnInit {
  parentUser: any = null;

  constructor(
    private authService: AuthService,
    private schoolService: SchoolService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.parentUser = this.authService.getUser();
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
