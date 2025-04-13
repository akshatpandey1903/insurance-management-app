import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ProfileService } from '../../services/profile.service';
import { UserResponseDTO } from '../../../../models/employee.model';
import { ToastrService } from 'ngx-toastr';


@Component({
  selector: 'app-profile',
  standalone: false,
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  profileForm!: FormGroup;
  user!: UserResponseDTO;
  loading = true;

  constructor(
    private fb: FormBuilder,
    private profileService: ProfileService,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    this.profileService.getProfile().subscribe({
      next: (res) => {
        this.user = res;
        this.initForm(); 
        this.loading = false;
      },
      error: () => {
        this.toastr.error('Failed to load profile');
        this.loading = false;
      }
    });
  }

  private initForm(): void {
    this.profileForm = this.fb.group({
      username: ['', [Validators.required]],  // Start with empty value
      password: [''], 
      firstName: ['', [Validators.required]], // Start with empty value
      lastName: ['', [Validators.required]]   // Start with empty value
    });
  }

  onUpdate(): void {
    if (this.profileForm.invalid) return;

    const updatedData = {
      ...this.user,
      ...this.profileForm.value
    };

    this.profileService.updateProfile(updatedData).subscribe({
      next: () => {
        this.toastr.success("Profile updated successfully");
        this.profileForm.reset(); // Optional: reset after update
      },
      error: () => {
        this.toastr.error("Failed to update profile");
      }
    });
  }
}

