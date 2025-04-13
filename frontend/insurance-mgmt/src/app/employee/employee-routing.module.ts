import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { EmployeeDashboardComponent } from './components/employee-dashboard/employee-dashboard.component';
import { AgentRegistrationComponent } from './components/agent-management/agent-registration/agent-registration.component';
import { PendingAgentApprovalComponent } from './components/agent-management/pending-agent-approval/pending-agent-approval.component';
import { CustomerManagementComponent } from './components/customer-managment/customer-management/customer-management.component';
import { CustomerListComponent } from './components/customer-managment/customer-list/customer-list.component';
import { DocumentVerificationComponent } from './components/customer-managment/document-verification/document-verification.component';
import { PolicyApprovalComponent } from './components/customer-managment/policy-approval/policy-approval.component';
import { ClaimsComponent } from './components/customer-managment/claims/claims.component';
import { CustomerQueriesComponent } from './components/customer-managment/customer-queries/customer-queries.component';
import { CommissionReportComponent } from './components/reports/commission-report/commission-report.component';
import { ProfileComponent } from './components/profile/profile/profile.component';
import { AgentManagementComponent } from './components/agent-management/agent-management/agent-management.component';
import { AllAgentsComponent } from './components/agent-management/all-agents/all-agents.component';


const routes: Routes = [
  { path: 'dashboard', component: EmployeeDashboardComponent },

  // AGENT MANAGEMENT ROUTES (flat, independent pages)
  { path: 'agent-management', component: AgentManagementComponent }, // dashboard
  { path: 'agent-management/register', component: AgentRegistrationComponent },
  { path: 'agent-management/pending-approvals', component: PendingAgentApprovalComponent },
  { path: 'agent-management/all', component: AllAgentsComponent },

  // CUSTOMER MANAGEMENT ROUTES (flat, independent pages)
  { path: 'customer-management', component: CustomerManagementComponent }, // dashboard
  { path: 'customer-management/view', component: CustomerListComponent },
  { path: 'customer-management/document-verification', component: DocumentVerificationComponent },
  { path: 'customer-management/policy-approvals', component: PolicyApprovalComponent },
  { path: 'customer-management/claims', component: ClaimsComponent },
  { path: 'customer-management/queries', component: CustomerQueriesComponent },

  { path: 'commission-report', component: CommissionReportComponent },

  { path: 'profile', component: ProfileComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class EmployeeRoutingModule {}
