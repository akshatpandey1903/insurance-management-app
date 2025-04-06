import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AgentRoutingModule } from './agent-routing.module';
import { AgentDashboardComponent } from './components/agent-dashboard/agent-dashboard.component';
import { ProfileComponent } from './components/profile/profile.component';
import { PolicyRegistrationComponent } from './components/policy-registration/policy-registration.component';
import { CommissionComponent } from './components/commission/commission.component';
import { ReportsComponent } from './components/reports/reports.component';
import { WithdrawlsComponent } from './components/withdrawls/withdrawls.component';


@NgModule({
  declarations: [
    AgentDashboardComponent,
    ProfileComponent,
    PolicyRegistrationComponent,
    CommissionComponent,
    ReportsComponent,
    WithdrawlsComponent
  ],
  imports: [
    CommonModule,
    AgentRoutingModule
  ]
})
export class AgentModule { }
