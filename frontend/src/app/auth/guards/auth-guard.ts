import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { TokenService } from '../../core/services/token.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(
    private tokenService: TokenService,
    private router: Router
  ) {}

  canActivate(): boolean {

    const token = this.tokenService.getToken();

    if (!token) {
      this.router.navigate(['/auth/login']);
      console.log('AuthGuard token:', this.tokenService.getToken());

      return false;
    }
   return true;
}
}