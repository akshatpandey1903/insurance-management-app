import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup} from '@angular/forms';
import { CustomerProfileDTO } from '../../model';

@Component({
  selector: 'app-profile',
  standalone: false,
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  profileForm!: FormGroup;
  isLoading: boolean = false;
  statusMessage: string | null = null;
  statusType: 'success' | 'error' | null = null;

  constructor(private fb: FormBuilder, private http: HttpClient) {}

  ngOnInit(): void {
    this.initForm();
    this.loadProfile();
  }

  initForm(): void {
    this.profileForm = this.fb.group({
      firstName: [''],
      lastName: [''],
      phone: [''],
      address: [''],
      email: [''],
      currentPassword: [''],
      newPassword: [''] // Optional new password field
    });
  }

  loadProfile(): void {
    this.isLoading = true;
    this.http.get<CustomerProfileDTO>('http://localhost:8080/app/customer/profile').subscribe({
      next: (data) => {
        this.profileForm.patchValue({
          firstName: data.firstName || '',
          lastName: data.lastName || '',
          phone: data.phone || '',
          address: data.address || '',
          email: data.email || ''
        });
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error fetching profile:', err);
        alert('Failed to load profile');
        this.isLoading = false;
      }
    });
  }

  updateProfile(): void {
    const formData = this.profileForm.value;

    const payload: any = {
      firstName: formData.firstName,
      lastName: formData.lastName,
      phone: formData.phone,
      address: formData.address,
      currentPassword: formData.currentPassword
    };

    if (formData.newPassword?.trim()) {
      payload.newPassword = formData.newPassword;
    }

    this.http.put(
      'http://localhost:8080/app/customer/profile',
      this.profileForm.value,
      {
        responseType: 'text'
      }
    ).subscribe({
      next: (response) => {
        console.log('Update success:', response);
        // alert(response);
        this.statusMessage = 'Profile updated successfully!';
        this.statusType = 'success';
      },
      error: (err) => {
        console.error('Update error:', err);
        // alert(err.error);
        this.statusMessage = err.error?.message || 'Profile update failed.';
        this.statusType = 'error';
      }
    });
  }

}
