import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AgentRoutingModule } from './agent-routing.module';
import { AgentDashboardComponent } from './components/agent-dashboard/agent-dashboard.component';
import { ProfileComponent } from './components/profile/profile.component';
import { PolicyRegistrationComponent } from './components/policy-registration/policy-registration.component';
import { CommissionComponent } from './components/commission/commission.component';
import { ReportsComponent } from './components/reports/reports.component';
import { WithdrawlsComponent } from './components/withdrawls/withdrawls.component';
import { NavbarComponent } from './components/navbar/navbar.component';


@NgModule({
  declarations: [
    AgentDashboardComponent,
    ProfileComponent,
    PolicyRegistrationComponent,
    CommissionComponent,
    ReportsComponent,
    WithdrawlsComponent,
    NavbarComponent
  ],
  imports: [
    CommonModule,
    AgentRoutingModule
  ]
})
export class AgentModule { }
