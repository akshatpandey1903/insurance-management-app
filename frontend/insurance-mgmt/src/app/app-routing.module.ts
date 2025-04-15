import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { IntroComponent } from './components/intro/intro.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { ForgotPasswordComponent } from './components/login/forgot-password/forgot-password.component';
import { AuthGuard } from './auth/auth.guard';

const routes: Routes = [
  { path: '', component: IntroComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'forgot-password', component: ForgotPasswordComponent },
  { 
    path: 'admin', 
    loadChildren: () => import('./admin/admin.module').then(m => m.AdminModule),
    // canActivate: [AuthGuard],
    // data: { role: 'ROLE_ADMIN' }
  },
  { 
    path: 'customer', 
    loadChildren: () => import('./customer/customer.module').then(m => m.CustomerModule),
    // canActivate: [AuthGuard],
    // data: { role: 'ROLE_CUSTOMER' } 
  },
  { 
    path: 'employee', 
    loadChildren: () => import('./employee/employee.module').then(m => m.EmployeeModule),
    // canActivate: [AuthGuard],
    // data: { role: 'ROLE_EMPLOYEE' }  
  },
  { 
    path: 'agent', 
    loadChildren: () => import('./agent/agent.module').then(m => m.AgentModule),
    // canActivate: [AuthGuard],
    // data: { role: 'ROLE_AGENT' }  
  },
  { path: '**', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
