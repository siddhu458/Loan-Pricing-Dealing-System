import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, map } from 'rxjs';
import { Loan } from '../../models/loan.model';
import { Loanservice } from '../../services/loan.service';
import { TokenService } from '../../../core/services/token.service';

@Component({
  selector: 'app-loanlist',
  templateUrl: './loanlist.html',
  styleUrls: ['./loanlist.scss'],
  standalone: false
})
export class LoanList implements OnInit {

  // Pricing state
  pricingLoan: Loan | null = null;
  baseRate = 8.5;
  creditRating = 'AA';

  // Pagination state
  loans$!: Observable<any>;
  currentPage = 0;
  pageSize = 5;
  totalPages = 0;
  totalElements = 0;

  // Helper methods for template access
  Math = Math;

  // Role flags
  isAdmin = false;
  isUser = false;

  constructor(
    private loanService: Loanservice,
    private tokenService: TokenService,
    private router: Router
  ) {}

  // INIT
  ngOnInit(): void {
    this.isAdmin = this.tokenService.isAdmin();
    this.isUser = this.tokenService.isUser();
    this.refreshLoans();
  }

  // LOAD / REFRESH LOANS WITH PAGINATION
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

  // PAGINATION METHODS
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

  // NAVIGATION
  goToCreate(): void {
    this.router.navigate(['/loans/create']);
  }

  // USER ACTIONS
  submitLoan(id: string): void {
    this.loanService.submitLoan(id)
      .subscribe(() => this.refreshLoans());
  }

  // ADMIN ACTIONS
  approveLoan(id: string): void {
    this.loanService.updateStatus(id, 'APPROVED')
      .subscribe(() => this.refreshLoans());
  }

  rejectLoan(id: string): void {
    this.loanService.updateStatus(id, 'REJECTED')
      .subscribe(() => this.refreshLoans());
  }

  reviewLoan(id: string): void {
    this.loanService.updateStatus(id, 'UNDER_REVIEW')
      .subscribe(() => this.refreshLoans());
  }

  // PRICING UI
  openPricing(loan: Loan): void {
    this.pricingLoan = loan;
    this.baseRate = 8.5;
    this.creditRating = 'AA';
  }

  cancelPricing(): void {
    this.pricingLoan = null;
  }

  // PRICING ACTION
  calculatePrice(): void {
  if (!this.pricingLoan || !this.pricingLoan.id) {
    alert('Invalid loan selected');
    return;
  }

  this.loanService.calculatePrice(
    this.pricingLoan.id,   
    {
      baseRate: this.baseRate,
      creditRating: this.creditRating
    }
  ).subscribe({
    next: () => {
      alert('Pricing calculated successfully');
      this.pricingLoan = null;
      this.refreshLoans();
    },
    error: () => {
      alert('Pricing failed');
    }
  });
  }

  deleteLoan(id: string | undefined): void {
  if (!id) {
    return;
  }

  const confirmed = confirm('Are you sure you want to delete this loan?');
  if (!confirmed) {
    return;
  }

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