import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AgentRoutingModule } from './agent-routing.module';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { AgentNavbarComponent } from './components/agent-navbar/agent-navbar.component';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { PaginationModule } from 'ngx-bootstrap/pagination';
import { PolicyRegistrationComponent } from './components/policy-registration/policy-registration.component';
import { EarningsComponent } from './components/earnings/earnings.component';
import { ProfileComponent } from './components/profile/profile.component';


@NgModule({
  declarations: [
    DashboardComponent,
    AgentNavbarComponent,
    PolicyRegistrationComponent,
    EarningsComponent,
    ProfileComponent
  ],
  imports: [
    CommonModule,
    AgentRoutingModule,
    ReactiveFormsModule,
    RouterModule,
    PaginationModule.forRoot()
  ]
})
export class AgentModule { }
