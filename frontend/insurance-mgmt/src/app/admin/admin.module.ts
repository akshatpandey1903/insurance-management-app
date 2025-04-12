import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AdminRoutingModule } from './admin-routing.module';
import { AdmindashboardComponent } from './components/admindashboard/admindashboard.component';
import { ManageUsersComponent } from './components/manage-users/manage-users.component';
import { SettingsComponent } from './components/settings/settings.component';
import { ReportsComponent } from './components/reports/reports.component';
import { WithdrawlApprovalComponent } from './components/withdrawl-approval/withdrawl-approval.component';
import { InsurancePlansComponent } from './components/insurance-plans/insurance-plans.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { FormsModule } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { AddCustomerComponent } from './components/add-customer/add-customer.component';
import { AddEmployeeComponent } from './components/add-employee/add-employee.component';
import { AddAgentComponent } from './components/add-agent/add-agent.component';
import { CustomerReportComponent } from './components/reports/customer-report/customer-report.component';
import { AgentReportComponent } from './components/reports/agent-report/agent-report.component';
import { AgentCommisionReportComponent } from './components/reports/agent-commision-report/agent-commision-report.component';
import { PolicyPaymentsComponent } from './components/reports/policy-payments/policy-payments.component';
import { TransactionsReportComponent } from './components/reports/transactions-report/transactions-report.component';
import { ResolveQueriesComponent } from './components/resolve-queries/resolve-queries.component';

@NgModule({
  declarations: [
    AdmindashboardComponent,
    ManageUsersComponent,
    SettingsComponent,
    ReportsComponent,
    WithdrawlApprovalComponent,
    InsurancePlansComponent,
    NavbarComponent,
    AddCustomerComponent,
    AddEmployeeComponent,
    AddAgentComponent,
    CustomerReportComponent,
    AgentReportComponent,
    AgentCommisionReportComponent,
    PolicyPaymentsComponent,
    TransactionsReportComponent,
    ResolveQueriesComponent
  ],
  imports: [
    CommonModule,
    AdminRoutingModule,
    FormsModule,
    ReactiveFormsModule
  ]
})
export class AdminModule { }
