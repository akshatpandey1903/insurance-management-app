import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { EmployeeRoutingModule } from './employee-routing.module';
import { EmployeeDashboardComponent } from './components/employee-dashboard/employee-dashboard.component';
import { DocumentVerificationComponent } from './components/document-verification/document-verification.component';
import { AgentRegistrationComponent } from './components/agent-registration/agent-registration.component';
import { ProfileComponent } from './components/profile/profile.component';
import { CustomerManagementComponent } from './components/customer-management/customer-management.component';
import { AgentManagementComponent } from './components/agent-management/agent-management.component';
import { CommisionReportsComponent } from './components/commision-reports/commision-reports.component';
import { NavbarComponent } from './components/navbar/navbar.component';


@NgModule({
  declarations: [
    EmployeeDashboardComponent,
    DocumentVerificationComponent,
    AgentRegistrationComponent,
    ProfileComponent,
    CustomerManagementComponent,
    AgentManagementComponent,
    CommisionReportsComponent,
    NavbarComponent
  ],
  imports: [
    CommonModule,
    EmployeeRoutingModule
  ]
})
export class EmployeeModule { }
