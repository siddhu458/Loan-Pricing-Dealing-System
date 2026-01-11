import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, map } from 'rxjs';
import { Loan } from '../../../loans/models/loan.model';
import { Loanservice } from '../../../loans/services/loan.service';
import { TokenService } from '../../../core/services/token.service';

@Component({
  selector: 'app-admin-loan-management',
  standalone: false,
  template: `
    <div class="admin-loan-management">
      <h2>Loan Management</h2>
      <p>Review and manage loans submitted by users</p>
      
      <!-- Loading -->
      <div class="loading" *ngIf="!(loans$ | async)">
        Loading loan data...
      </div>

      <!-- Table -->
      <div class="table-card" *ngIf="(loans$ | async) as loans">

        <div *ngIf="loans.length === 0" class="empty">
          No loan requests found
        </div>

        <table *ngIf="loans.length > 0">
          <thead>
            <tr>
              <th>Client</th>
              <th>Type</th>
              <th>Amount</th>
              <th>Rate (%)</th>
              <th>Tenure</th>
              <th>Status</th>
              <th>Actions</th>
            </tr>
          </thead>

          <tbody>
            <tr *ngFor="let loan of loans">
              <td>{{ loan.clientName }}</td>
              <td>{{ loan.loanType }}</td>
              <td>₹ {{ loan.requestedAmount | number }}</td>
              <td>{{ loan.proposedInterestRate }}</td>
              <td>{{ loan.tenureMonths }}</td>

              <!-- Status Badge -->
              <td>
                <span class="status" [ngClass]="loan.status?.toLowerCase()">
                  {{ loan.status }}
                </span>
              </td>

              <!-- Admin Actions -->
              <td class="actions">
                <button
                  class="btn review"
                  *ngIf="loan.status === 'SUBMITTED'"
                  (click)="reviewLoan(loan.id!)">
                  Review
                </button>

                <button
                  class="btn approve"
                  *ngIf="loan.status === 'UNDER_REVIEW'"
                  (click)="approveLoan(loan.id!)">
                  Approve
                </button>

                <button
                  class="btn reject"
                  *ngIf="loan.status === 'UNDER_REVIEW'"
                  (click)="rejectLoan(loan.id!)">
                  Reject
                </button>

                <button
                  class="btn warning"
                  *ngIf="loan.status === 'DRAFT'"
                  (click)="deleteLoan(loan.id)">
                  Delete
                </button>
              </td>
            </tr>
          </tbody>
        </table>

        <!-- Pagination Controls -->
        <div class="pagination" *ngIf="totalPages > 1">
          <div class="pagination-info">
            Showing {{ currentPage * pageSize + 1 }} to 
            {{ Math.min((currentPage + 1) * pageSize, totalElements) }} 
            of {{ totalElements }} loans
          </div>
          
          <div class="pagination-controls">
            <button 
              class="pagination-btn" 
              (click)="previousPage()" 
              [disabled]="currentPage === 0">
              ← Previous
            </button>
            
            <div class="page-numbers">
              <button 
                *ngFor="let page of getPageNumbers()" 
                class="page-btn"
                [class.active]="page === currentPage"
                (click)="goToPage(page)">
                {{ page + 1 }}
              </button>
            </div>
            
            <button 
              class="pagination-btn" 
              (click)="nextPage()" 
              [disabled]="currentPage === totalPages - 1">
              Next →
            </button>
          </div>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .admin-loan-management {
      padding: 20px;
    }
    
    .loading {
      text-align: center;
      padding: 40px;
      color: #6c757d;
    }
    
    .table-card {
      background: white;
      border-radius: 8px;
      box-shadow: 0 2px 4px rgba(0,0,0,0.1);
      overflow: hidden;
    }
    
    .empty {
      text-align: center;
      padding: 40px;
      color: #6c757d;
    }
    
    table {
      width: 100%;
      border-collapse: collapse;
    }
    
    th, td {
      padding: 12px;
      text-align: left;
      border-bottom: 1px solid #ddd;
    }
    
    th {
      background: #f8f9fa;
      font-weight: 600;
    }
    
    .status {
      padding: 4px 8px;
      border-radius: 12px;
      font-size: 12px;
      font-weight: 500;
    }
    
    .status.draft { background: #6c757d; color: white; }
    .status.submitted { background: #17a2b8; color: white; }
    .status.under_review { background: #ffc107; color: #212529; }
    .status.approved { background: #28a745; color: white; }
    .status.rejected { background: #dc3545; color: white; }
    
    .actions {
      display: flex;
      gap: 8px;
    }
    
    .btn {
      padding: 6px 12px;
      border: none;
      border-radius: 4px;
      cursor: pointer;
      font-size: 12px;
    }
    
    .btn.review { background: #17a2b8; color: white; }
    .btn.approve { background: #28a745; color: white; }
    .btn.reject { background: #dc3545; color: white; }
    .btn.warning { background: #ffc107; color: #212529; }

    /* Pagination */
    .pagination {
      margin-top: 20px;
      padding: 16px;
      border-top: 1px solid #eee;
      display: flex;
      justify-content: space-between;
      align-items: center;
      flex-wrap: wrap;
      gap: 16px;
    }

    .pagination-info {
      color: #666;
      font-size: 14px;
    }

    .pagination-controls {
      display: flex;
      align-items: center;
      gap: 8px;
    }

    .pagination-btn {
      padding: 8px 12px;
      border: 1px solid #ddd;
      background: #fff;
      border-radius: 4px;
      cursor: pointer;
      font-size: 14px;
      transition: all 0.2s ease;
    }

    .pagination-btn:hover:not(:disabled) {
      background: #f5f5f5;
      border-color: #999;
    }

    .pagination-btn:disabled {
      opacity: 0.5;
      cursor: not-allowed;
      background: #f9f9f9;
    }

    .page-numbers {
      display: flex;
      gap: 4px;
    }

    .page-btn {
      min-width: 32px;
      height: 32px;
      border: 1px solid #ddd;
      background: #fff;
      border-radius: 4px;
      cursor: pointer;
      font-size: 14px;
      transition: all 0.2s ease;
    }

    .page-btn:hover {
      background: #f5f5f5;
      border-color: #999;
    }

    .page-btn.active {
      background: #007bff;
      color: #fff;
      border-color: #007bff;
    }
  `]
})
export class AdminLoanManagementComponent implements OnInit {

  loans$!: Observable<any>;
  currentPage = 0;
  pageSize = 5;
  totalPages = 0;
  totalElements = 0;

  
  Math = Math;

  constructor(
    private loanService: Loanservice,
    private tokenService: TokenService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.refreshLoans();
  }

  refreshLoans(page: number = this.currentPage): void {
    this.loans$ = this.loanService.getLoans(page, this.pageSize).pipe(
      map((response: any) => {
        this.totalPages = response.totalPages;
        this.totalElements = response.totalElements;
        this.currentPage = response.number;
        return response.content || [];
      })
    );
  }

  
  goToPage(page: number): void {
    if (page >= 0 && page < this.totalPages) {
      this.refreshLoans(page);
    }
  }

  nextPage(): void {
    if (this.currentPage < this.totalPages - 1) {
      this.goToPage(this.currentPage + 1);
    }
  }

  previousPage(): void {
    if (this.currentPage > 0) {
      this.goToPage(this.currentPage - 1);
    }
  }

  getPageNumbers(): number[] {
    const pages: number[] = [];
    const maxVisiblePages = 5;
    
    let startPage = Math.max(0, this.currentPage - Math.floor(maxVisiblePages / 2));
    let endPage = Math.min(this.totalPages - 1, startPage + maxVisiblePages - 1);
    
    if (endPage - startPage < maxVisiblePages - 1) {
      startPage = Math.max(0, endPage - maxVisiblePages + 1);
    }
    
    for (let i = startPage; i <= endPage; i++) {
      pages.push(i);
    }
    
    return pages;
  }

  reviewLoan(id: string): void {
    this.loanService.updateStatus(id, 'UNDER_REVIEW')
      .subscribe(() => this.refreshLoans());
  }

  approveLoan(id: string): void {
    this.loanService.updateStatus(id, 'APPROVED')
      .subscribe(() => this.refreshLoans());
  }

  rejectLoan(id: string): void {
    this.loanService.updateStatus(id, 'REJECTED')
      .subscribe(() => this.refreshLoans());
  }

  deleteLoan(id: string | undefined): void {
    if (!id) return;
    
    const confirmed = confirm('Are you sure you want to delete this loan?');
    if (!confirmed) return;

    this.loanService.deleteLoan(id).subscribe({
      next: () => {
        alert('Loan deleted successfully');
        this.refreshLoans();
      },
      error: () => {
        alert('Failed to delete loan');
      }
    });
  }
}
