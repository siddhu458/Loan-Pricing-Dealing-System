import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { tap } from 'rxjs/operators';
import { environment } from '../../../environments/environment';

import { TokenService } from '../../core/services/token.service';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private readonly API_URL = `${environment.apiUrl}/api/auth`;

  constructor(
    private http: HttpClient,
    private tokenService: TokenService,
    private router: Router
  ) {}

  // OPTIONAL (Admin creates users)
  register(email: string, password: string, role: string) {
    return this.http.post(
      `${this.API_URL}/register`,
      { email, password, role }
    );
  }

  
  login(credentials: { email: string; password: string }) {
    return this.http.post<{ token: string }>(
      `${this.API_URL}/login`,
      credentials
    ).pipe(
      tap(res => {
        this.tokenService.saveToken(res.token);
      })
    );
  }

  logout(): void {
    this.tokenService.clearToken();
    this.router.navigate(['/auth/login']);
  }
}
