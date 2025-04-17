import { Injectable } from '@angular/core';
import { CanActivate, CanLoad, ActivatedRouteSnapshot, Route, UrlSegment, Router, RouterStateSnapshot } from '@angular/router';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate, CanLoad {

  constructor(private router: Router) {}

  private checkAccess(expectedRole?: string): boolean {
    const token = localStorage.getItem('accessToken');
    const role = localStorage.getItem('userRole'); 

    if (!token) {
      this.router.navigate(['/login']);
      return false;
    }

    if (expectedRole && role !== expectedRole) {
      this.router.navigate(['/unauthorized']);
      return false;
    }

    return true;
  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    const expectedRole = route.data['expectedRole'];
    return this.checkAccess(expectedRole);
  }

  canLoad(route: Route, segments: UrlSegment[]): boolean {
    const expectedRole = route.data?.['expectedRole'];
    return this.checkAccess(expectedRole);
  }
}


