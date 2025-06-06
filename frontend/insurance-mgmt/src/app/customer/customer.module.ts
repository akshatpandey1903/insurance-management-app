import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { CustomerRoutingModule } from './customer-routing.module';
import { CustomerDashboardComponent } from './components/customer-dashboard/customer-dashboard.component';
import { ProfileComponent } from './components/profile/profile.component';
import { PoliciesComponent } from './components/policies/policies.component';
import { PaymentsComponent } from './components/payments/payments.component';
import { WithdrawlsComponent } from './components/withdrawls/withdrawls.component';
import { ContactComponent } from './components/contact/contact.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { ReactiveFormsModule } from '@angular/forms';
import { FormsModule } from '@angular/forms';
import { PolicyClaimComponent } from './components/policy-claim/policy-claim.component';
import { PolicyCancelComponent } from './components/policy-cancel/policy-cancel.component';


@NgModule({
  declarations: [
    CustomerDashboardComponent,
    ProfileComponent,
    PoliciesComponent,
    PaymentsComponent,
    WithdrawlsComponent,
    NavbarComponent,
    ContactComponent,
    PolicyClaimComponent,
    PolicyCancelComponent
  ],
  imports: [
    CommonModule,
    CustomerRoutingModule,
    ReactiveFormsModule,
    FormsModule
  ]
})
export class CustomerModule { }
