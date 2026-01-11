import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { User, UserCreate, AuditTrail } from '../models/user.model';

@Injectable({
  providedIn: 'root'
})
export class AdminService {
  private baseUrl = `${environment.apiUrl}/api/admin`;

  constructor(private http: HttpClient) {}

  // User Management
  getUsers(): Observable<User[]> {
    return this.http.get<User[]>(`${this.baseUrl}/users`);
  }

  createUser(user: UserCreate): Observable<User> {
    return this.http.post<User>(`${this.baseUrl}/users`, user);
  }

  updateUserStatus(userId: string, active: boolean): Observable<User> {
    return this.http.put<User>(`${this.baseUrl}/users/${userId}/status`, null, {
      params: { active: active.toString() }
    });
  }

  // Audit Trail
  getAuditTrail(): Observable<AuditTrail[]> {
    return this.http.get<AuditTrail[]>(`${this.baseUrl}/audit-trail`);
  }

  getAuditTrailForLoan(loanId: string): Observable<AuditTrail[]> {
    return this.http.get<AuditTrail[]>(`${this.baseUrl}/audit-trail/${loanId}`);
  }
}
