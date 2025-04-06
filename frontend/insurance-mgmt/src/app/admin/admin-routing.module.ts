import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AdmindashboardComponent } from './components/admindashboard/admindashboard.component';
import { ManageUsersComponent } from './components/manage-users/manage-users.component';
import { SettingsComponent } from './components/settings/settings.component';
import { ReportsComponent } from './components/reports/reports.component';
import { WithdrawlApprovalComponent } from './components/withdrawl-approval/withdrawl-approval.component';
import { InsurancePlansComponent } from './components/insurance-plans/insurance-plans.component';

const routes: Routes = [
  {path: 'dashboard',component: AdmindashboardComponent},
  { path: 'manage-users', component: ManageUsersComponent }, 
  { path: 'settings', component: SettingsComponent },     
  { path: 'reports', component: ReportsComponent },      
  { path: 'withdrawal-approval', component: WithdrawlApprovalComponent },
  { path: 'insurance-plans', component: InsurancePlansComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AdminRoutingModule { }
