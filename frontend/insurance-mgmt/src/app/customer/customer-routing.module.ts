import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CustomerDashboardComponent } from './components/customer-dashboard/customer-dashboard.component';
import { ProfileComponent } from './components/profile/profile.component';
import { PoliciesComponent } from './components/policies/policies.component';
import { PaymentsComponent } from './components/payments/payments.component';
import { WithdrawlsComponent } from './components/withdrawls/withdrawls.component';
import { ContactComponent } from './components/contact/contact.component';
import { PolicyClaimComponent } from './components/policy-claim/policy-claim.component';
import { PolicyCancelComponent } from './components/policy-cancel/policy-cancel.component';

const routes: Routes = [
  {path: 'dashboard' , component: CustomerDashboardComponent},
  { path: 'profile', component: ProfileComponent },      
  { path: 'policies', component: PoliciesComponent },     
  { path: 'payments', component: PaymentsComponent },     
  { path: 'withdrawals', component: WithdrawlsComponent },  
  { path: 'contact', component: ContactComponent },
  {path: 'policy-claim' , component: PolicyClaimComponent},
  {path: 'policy-cancel' , component: PolicyCancelComponent}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CustomerRoutingModule { }
