import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class TokenService {

  private readonly KEY = 'auth_token';

  getToken(): string | null {
    return localStorage.getItem(this.KEY);
  }

  saveToken(token: string): void {
    localStorage.setItem(this.KEY, token);
  }

  clearToken(): void {
    localStorage.removeItem(this.KEY);
  }

  getUserRole(): string | null {
    const token = this.getToken();
    if (!token) return null;

    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      console.log('Token payload:', payload); // Debug log
      return payload.role || payload.authorities || null;
    } catch (error) {
      console.error('Error parsing token:', error);
      return null;
    }
  }

  getUserEmail(): string | null {
    const token = this.getToken();
    if (!token) return null;

    const payload = JSON.parse(atob(token.split('.')[1]));
    return payload.sub;
  }

  isAdmin(): boolean {
    const role = this.getUserRole();
    return role === 'ADMIN' || role === 'ROLE_ADMIN';
  }

  isUser(): boolean {
    const role = this.getUserRole();
    return role === 'USER' || role === 'ROLE_USER';
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }

}

