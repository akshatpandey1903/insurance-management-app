import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LoadingService {
  private isLoadingSubject = new BehaviorSubject<boolean>(false);
  
  get isLoading$(): Observable<boolean> {
    return this.isLoadingSubject.asObservable();
  }
  
  showLoading(): void {
    this.isLoadingSubject.next(true);
  }
  
  hideLoading(): void {
    this.isLoadingSubject.next(false);
  }
}