import { NgModule } from '@angular/core';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { CommonModule } from '@angular/common';
import { NgxPaginationModule } from 'ngx-pagination';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { EmployeeDashboardComponent } from './components/employee-dashboard/employee-dashboard.component';
import { PendingAgentApprovalComponent } from './components/agent-management/pending-agent-approval/pending-agent-approval.component';
import { AllAgentsComponent } from './components/agent-management/all-agents/all-agents.component';
import { CustomerListComponent } from './components/customer-managment/customer-list/customer-list.component';
import { PolicyApprovalComponent } from './components/customer-managment/policy-approval/policy-approval.component';
import { ClaimsComponent } from './components/customer-managment/claims/claims.component';
import { CustomerQueriesComponent } from './components/customer-managment/customer-queries/customer-queries.component';
import { AgentRegistrationComponent } from './components/agent-management/agent-registration/agent-registration.component';
import { CustomerManagementComponent } from './components/customer-managment/customer-management/customer-management.component';
import { DocumentVerificationComponent } from './components/customer-managment/document-verification/document-verification.component';
import { ProfileComponent } from './components/profile/profile/profile.component';
import { CommissionReportComponent } from './components/reports/commission-report/commission-report.component';
import { EmployeeRoutingModule } from './employee-routing.module';
import { PaginationModule } from 'ngx-bootstrap/pagination';
import { NavbarComponent } from './components/navbar/navbar.component';
import { AgentManagementComponent } from './components/agent-management/agent-management/agent-management.component';


@NgModule({
  declarations: [
    EmployeeDashboardComponent,
    NavbarComponent,
    PendingAgentApprovalComponent,
    AllAgentsComponent,
    CustomerListComponent,
    PolicyApprovalComponent,
    ClaimsComponent,
    CustomerQueriesComponent,
    AgentManagementComponent,
    AgentRegistrationComponent,
    CustomerManagementComponent,
    DocumentVerificationComponent,
    ProfileComponent,
    CommissionReportComponent
  ],
  imports: [
    CommonModule,
    EmployeeRoutingModule,
    ReactiveFormsModule,
    NgxPaginationModule,
    FormsModule,
    PaginationModule.forRoot(),
    NgbModule
  ],
  exports: [
    NavbarComponent,
    PendingAgentApprovalComponent,
  ]
})
export class EmployeeModule { }
