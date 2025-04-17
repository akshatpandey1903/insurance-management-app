import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AdminService } from '../../services/admin.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-admin-profile',
  standalone: false,
  templateUrl: './admin-profile.component.html',
  styleUrl: './admin-profile.component.css'
})
export class AdminProfileComponent {
  profileForm!: FormGroup;
  loading = false;
  statusMessage: string | null = null;
  statusType: 'success' | 'error' | null = null;

  constructor(
    private fb: FormBuilder,
    private adminService: AdminService,
    private toastr: ToastrService
  ) { }

  ngOnInit(): void {
    this.profileForm = this.fb.group({
      username: ['' , Validators.required],
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: [{ value: '', disabled: true }, Validators.required],
      role: [{ value: '', disabled: true } , Validators.required],
      currentPassword: ['', Validators.required],
      newPassword: ['']
    });

    this.loadProfile();
  }

  loadProfile() {
    this.loading = true;
    this.adminService.getAdminProfile().subscribe({
      next: (res) => {
        if (res) {
          this.profileForm.patchValue({
            username: res.username,
            firstName: res.firstName,
            lastName: res.lastName,
            email: res.email,
            role: res.role.split('_')[1]?.toLowerCase().replace(/^\w/, c => c.toUpperCase()) || ''
          });
        } else {
          this.statusMessage = 'Profile data not found';
        }
        this.loading = false;
        console.log(res);
      }
    });
  }

  onSubmit() {
    if (this.profileForm.invalid) {
      this.statusMessage = 'Please fill required details correctly';
      this.statusType = 'error';
      return;
    }

    const payload = {
      firstName: this.profileForm.get('firstName')?.value,
      lastName: this.profileForm.get('lastName')?.value,
      phone: this.profileForm.get('phone')?.value,
      currentPassword: this.profileForm.get('currentPassword')?.value,
      newPassword: this.profileForm.get('newPassword')?.value || null
    };

    this.adminService.updateAdminProfile(payload).subscribe({
      next: (res) => {
        this.statusMessage = 'Profile updated successfully!';
        this.statusType = 'success';
        this.profileForm.get('currentPassword')?.reset();
        this.profileForm.get('newPassword')?.reset();
      },
      error: (err) => {
        console.log('Error response:', err);
        this.statusMessage = err.error?.message || 'Profile update failed.';
        this.statusType = 'error';
      }
    });
  }
}
