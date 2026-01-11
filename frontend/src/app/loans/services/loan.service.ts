import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class Loanservice {

  private baseUrl = `${environment.apiUrl}/api/loans`;

  constructor(private http: HttpClient) {}

  // LIST WITH PAGINATION
  getLoans(page: number = 0, size: number = 5) {
    return this.http.get<any>(`${this.baseUrl}?page=${page}&size=${size}`);
  }

  // CREATE (DRAFT)
  createLoan(payload: any) {
    return this.http.post(this.baseUrl, payload);
  }

  // SUBMIT (USER)
  submitLoan(id: string) {
    return this.http.patch(`${this.baseUrl}/${id}/submit`, {});
  }

  // ADMIN STATUS UPDATE
  updateStatus(id: string, status: string, comments?: string) {
    return this.http.patch(
      `${this.baseUrl}/${id}/status`,
      { status, comments }
    );
  }

  // PRICING 
  calculatePrice(loanId: string, data: any) {
    return this.http.post(
      `${this.baseUrl}/${loanId}/price`,
      data
    );
  }

  // DELETE 
  deleteLoan(id: string) {
    return this.http.delete(
      `${this.baseUrl}/${id}`
    );
  }
}
