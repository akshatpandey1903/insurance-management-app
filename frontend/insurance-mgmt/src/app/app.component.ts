import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { LoadingService } from './services/loading.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  standalone: false,
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'insurance-mgmt';
  isLoading = false;
  private loadingSubscription: Subscription = new Subscription();
  constructor(private router: Router, private loadingService: LoadingService) {}
  // Getter for checking authentication status
 isAuthenticated() {
    // return !!localStorage.getItem('authToken'); // Check if authToken exists
  }
  
  ngOnInit() {
    this.loadingSubscription = this.loadingService.isLoading$.subscribe(
      (isLoading) => {
        this.isLoading = isLoading;
      }
    );
  }

  ngOnDestroy() {
    if (this.loadingSubscription) {
      this.loadingSubscription.unsubscribe();
    }
  }
}
