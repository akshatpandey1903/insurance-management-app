import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AgentDashboardComponent } from './components/agent-dashboard/agent-dashboard.component';
import { ProfileComponent } from './components/profile/profile.component';
import { PolicyRegistrationComponent } from './components/policy-registration/policy-registration.component';
import { CommissionComponent } from './components/commission/commission.component';
import { ReportsComponent } from './components/reports/reports.component';
import { WithdrawlsComponent } from './components/withdrawls/withdrawls.component';

const routes: Routes = [
  {path: 'dashboard' , component: AgentDashboardComponent},
  { path: 'profile', component: ProfileComponent },         
  { path: 'policy-registration', component: PolicyRegistrationComponent },
  { path: 'commission', component: CommissionComponent },      
  { path: 'reports', component: ReportsComponent },         
  { path: 'withdrawals', component: WithdrawlsComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AgentRoutingModule { }
