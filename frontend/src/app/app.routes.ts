import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { DashboardAdminComponent } from './components/dashboard-admin/dashboard-admin.component';
import { DashboardTeacherComponent } from './components/dashboard-teacher/dashboard-teacher.component';
import { DashboardStudentComponent } from './components/dashboard-student/dashboard-student.component';
import { DashboardParentComponent } from './components/dashboard-parent/dashboard-parent.component';
import { authGuard } from './guards/auth.guard';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  {
    path: 'dashboard-admin',
    component: DashboardAdminComponent,
    canActivate: [authGuard],
    data: { role: 'ADMIN' }
  },
  {
    path: 'dashboard-teacher',
    component: DashboardTeacherComponent,
    canActivate: [authGuard],
    data: { role: 'TEACHER' }
  },
  {
    path: 'dashboard-student',
    component: DashboardStudentComponent,
    canActivate: [authGuard],
    data: { role: 'STUDENT' }
  },
  {
    path: 'dashboard-parent',
    component: DashboardParentComponent,
    canActivate: [authGuard],
    data: { role: 'PARENT' }
  },
  { path: '**', redirectTo: 'login' }
];
