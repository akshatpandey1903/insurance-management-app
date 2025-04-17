import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AdmindashboardComponent } from './components/admindashboard/admindashboard.component';
import { ManageUsersComponent } from './components/manage-users/manage-users.component';
import { SettingsComponent } from './components/settings/settings.component';
import { ReportsComponent } from './components/reports/reports.component';
import { WithdrawlApprovalComponent } from './components/withdrawl-approval/withdrawl-approval.component';
import { InsurancePlansComponent } from './components/insurance-plans/insurance-plans.component';
import { AddCustomerComponent } from './components/add-customer/add-customer.component';
import { AddEmployeeComponent } from './components/add-employee/add-employee.component';
import { AddAgentComponent } from './components/add-agent/add-agent.component';
import { CustomerReportComponent } from './components/reports/customer-report/customer-report.component';
import { AgentReportComponent } from './components/reports/agent-report/agent-report.component';
import { AgentCommisionReportComponent } from './components/reports/agent-commision-report/agent-commision-report.component';
import { PolicyPaymentsComponent } from './components/reports/policy-payments/policy-payments.component';
import { TransactionsReportComponent } from './components/reports/transactions-report/transactions-report.component';
import { ResolveQueriesComponent } from './components/resolve-queries/resolve-queries.component';
import { AdminProfileComponent } from './components/admin-profile/admin-profile.component';

const routes: Routes = [
  {path: 'dashboard',component: AdmindashboardComponent},
  { path: 'manage-users', component: ManageUsersComponent }, 
  { path: 'settings', component: SettingsComponent },     
  { path: 'reports', component: ReportsComponent },      
  { path: 'withdrawal-approval', component: WithdrawlApprovalComponent },
  { path: 'insurance-plans', component: InsurancePlansComponent },
  {path: 'manage-users/add-customer', component: AddCustomerComponent},
  {path: 'manage-users/add-employee', component: AddEmployeeComponent},
  {path: 'manage-users/add-agent', component: AddAgentComponent},
  { path: 'reports/customers', component: CustomerReportComponent},
  { path: 'reports/agents', component: AgentReportComponent},
  { path: 'reports/commissions', component: AgentCommisionReportComponent},
  { path: 'reports/policy-payments', component: PolicyPaymentsComponent},
  { path: 'reports/transactions', component: TransactionsReportComponent},
  {path: 'resolve-queries', component: ResolveQueriesComponent},
  {path: 'admin-profile' , component: AdminProfileComponent}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AdminRoutingModule { }
