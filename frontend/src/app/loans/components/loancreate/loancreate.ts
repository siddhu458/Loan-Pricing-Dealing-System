import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Loan } from '../../models/loan.model';
import { Loanservice } from '../../services/loan.service';

@Component({
  selector: 'app-loancreate',
  standalone: false,
  templateUrl: './loancreate.html',
  styleUrl: './loancreate.scss',
})
export class Loancreate {

  loan: Loan = {
    clientName: '',
    loanType: '',
    requestedAmount: 0,
    proposedInterestRate: 0,
    tenureMonths: 0
  };

  error = '';

  constructor(
    private loanService: Loanservice,
    private router: Router
  ) {}

  // FORM VALIDATION CHECK
  isFormValid(): boolean {
    return !!(
      this.loan.clientName &&
      this.loan.loanType &&
      this.loan.requestedAmount > 0 &&
      this.loan.proposedInterestRate > 0 &&
      this.loan.tenureMonths > 0
    );
  }

  submit(): void {
    if (!this.isFormValid()) {
      this.error = 'Please fill all fields correctly';
      return;
    }

    this.loanService.createLoan(this.loan).subscribe({
      next: () => {
        this.loan = {
          clientName: '',
          loanType: '',
          requestedAmount: 0,
          proposedInterestRate: 0,
          tenureMonths: 0
        };

        this.router.navigate(['/loans']);
      },
      error: () => {
        this.error = 'Failed to create loan';
      }
    });
  }
}
