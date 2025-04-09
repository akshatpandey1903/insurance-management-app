import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-manage-users',
  standalone: false,
  templateUrl: './manage-users.component.html',
  styleUrl: './manage-users.component.css'
})
export class ManageUsersComponent {

  constructor(private router: Router) {}

  navigateToAddCustomer() {
    this.router.navigate(['admin/manage-users/add-customer']);
  }

  navigateToAddEmployee() {
    this.router.navigate(['admin/manage-users/add-employee']);
  }

  navigateToAddAgent() {
    this.router.navigate(['admin/manage-users/add-agent']);
  }

}
