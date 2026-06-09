import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  isLoginTab = true;
  loginData = { email: '', password: '' };
  registerData = {
    name: '',
    email: '',
    password: '',
    role: 'STUDENT',
    phone: '',
    rollNo: '',
    parentName: '',
    parentPhone: '',
    classId: null as number | null,
    specialization: '',
    salary: null as number | null
  };
  errorMessage = '';
  successMessage = '';

  constructor(private authService: AuthService, private router: Router) {}

  onLogin() {
    this.errorMessage = '';
    this.authService.login(this.loginData).subscribe({
      next: (res) => {
        const role = this.authService.getRole();
        if (role === 'ADMIN') this.router.navigate(['/dashboard-admin']);
        else if (role === 'TEACHER') this.router.navigate(['/dashboard-teacher']);
        else if (role === 'STUDENT') this.router.navigate(['/dashboard-student']);
        else if (role === 'PARENT') this.router.navigate(['/dashboard-parent']);
      },
      error: (err) => {
        this.errorMessage = err.error || 'Invalid email or password';
      }
    });
  }

  onRegister() {
    this.errorMessage = '';
    this.successMessage = '';
    this.authService.register(this.registerData).subscribe({
      next: (res) => {
        this.successMessage = 'Registration successful! Please login.';
        this.isLoginTab = true;
      },
      error: (err) => {
        this.errorMessage = err.error || 'Registration failed';
      }
    });
  }
}
