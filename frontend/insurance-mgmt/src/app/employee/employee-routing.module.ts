import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { EmployeeDashboardComponent } from './components/employee-dashboard/employee-dashboard.component';
import { DocumentVerificationComponent } from './components/document-verification/document-verification.component';
import { AgentRegistrationComponent } from './components/agent-registration/agent-registration.component';
import { ProfileComponent } from './components/profile/profile.component';
import { CustomerManagementComponent } from './components/customer-management/customer-management.component';
import { AgentManagementComponent } from './components/agent-management/agent-management.component';
import { CommisionReportsComponent } from './components/commision-reports/commision-reports.component';

const routes: Routes = [
  {path: 'dashboard', component: EmployeeDashboardComponent},
  { path: 'document-verification', component: DocumentVerificationComponent }, 
  { path: 'agent-registration', component: AgentRegistrationComponent },    
  { path: 'profile', component: ProfileComponent },              
  { path: 'customer-management', component: CustomerManagementComponent },  
  { path: 'agent-management', component: AgentManagementComponent },     
  { path: 'commission-reports', component: CommisionReportsComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class EmployeeRoutingModule { }
