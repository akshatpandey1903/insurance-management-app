import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { IntroComponent } from './components/intro/intro.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';

const routes: Routes = [
  { path: '', component: IntroComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { 
    path: 'admin', 
    loadChildren: () => import('./admin/admin.module').then(m => m.AdminModule) 
  },
  { 
    path: 'customer', 
    loadChildren: () => import('./customer/customer.module').then(m => m.CustomerModule) 
  },
  { 
    path: 'employee', 
    loadChildren: () => import('./employee/employee.module').then(m => m.EmployeeModule) 
  },
  { 
    path: 'agent', 
    loadChildren: () => import('./agent/agent.module').then(m => m.AgentModule) 
  },
  { path: '**', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
