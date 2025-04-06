import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AdminRoutingModule } from './admin-routing.module';
import { AdmindashboardComponent } from './components/admindashboard/admindashboard.component';
import { ManageUsersComponent } from './components/manage-users/manage-users.component';
import { SettingsComponent } from './components/settings/settings.component';
import { ReportsComponent } from './components/reports/reports.component';
import { WithdrawlApprovalComponent } from './components/withdrawl-approval/withdrawl-approval.component';
import { InsurancePlansComponent } from './components/insurance-plans/insurance-plans.component';


@NgModule({
  declarations: [
    AdmindashboardComponent,
    ManageUsersComponent,
    SettingsComponent,
    ReportsComponent,
    WithdrawlApprovalComponent,
    InsurancePlansComponent
  ],
  imports: [
    CommonModule,
    AdminRoutingModule
  ]
})
export class AdminModule { }
