import { ComponentFixture, TestBed } from '@angular/core/testing';
import { LoanList } from './loanlist';

import { TokenService } from '../../../core/services/token.service';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { of, throwError } from 'rxjs';
import { CommonModule } from '@angular/common';
import { Loanservice } from '../../services/loan.service';

describe('LoanList Component', () => {
  let component: LoanList;
  let fixture: ComponentFixture<LoanList>;
  let loanServiceSpy: jasmine.SpyObj<Loanservice>;
  let tokenServiceSpy: jasmine.SpyObj<TokenService>;
  let routerSpy: jasmine.SpyObj<Router>;

  const mockLoans = [
    {
      id: '1',
      clientName: 'ABC Corp',
      loanType: 'Business',
      requestedAmount: 100000,
      proposedInterestRate: 10,
      tenureMonths: 12,
      status: 'DRAFT'
    }
  ];

  beforeEach(async () => {
    loanServiceSpy = jasmine.createSpyObj('Loanservice', [
      'getLoans',
      'submitLoan',
      'updateStatus',
      'calculatePrice',
      'deleteLoan'
    ]);

    tokenServiceSpy = jasmine.createSpyObj('TokenService', [
      'isAdmin',
      'isUser'
    ]);

    routerSpy = jasmine.createSpyObj('Router', ['navigate']);

    loanServiceSpy.getLoans.and.returnValue(of({ content: mockLoans }));
    tokenServiceSpy.isAdmin.and.returnValue(false);
    tokenServiceSpy.isUser.and.returnValue(true);

    await TestBed.configureTestingModule({
      declarations: [LoanList],
      imports: [CommonModule, FormsModule],
      providers: [
        { provide: Loanservice, useValue: loanServiceSpy },
        { provide: TokenService, useValue: tokenServiceSpy },
        { provide: Router, useValue: routerSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(LoanList);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

 // basic test
  it('should create component', () => {
    expect(component).toBeTruthy();
  });

  it('should set role flags on init', () => {
    expect(component.isUser).toBeTrue();
    expect(component.isAdmin).toBeFalse();
  });

 // loan loading test
  it('should load loans on init', (done) => {
    component.loans$.subscribe(loans => {
      expect(loans.length).toBe(1);
      expect(loans[0].clientName).toBe('ABC Corp');
      done();
    });
  });

  it('should handle empty response safely', (done) => {
    loanServiceSpy.getLoans.and.returnValue(of({}));

    component.refreshLoans();

    component.loans$.subscribe(loans => {
      expect(loans).toEqual([]);
      done();
    });
  });

  // navigation test
  it('should navigate to create loan page', () => {
    component.goToCreate();
    expect(routerSpy.navigate).toHaveBeenCalledWith(['/loans/create']);
  });

 // user actions test
  it('should submit loan and refresh list', () => {
    loanServiceSpy.submitLoan.and.returnValue(of({}));
    spyOn(component, 'refreshLoans');

    component.submitLoan('1');

    expect(loanServiceSpy.submitLoan).toHaveBeenCalledWith('1');
    expect(component.refreshLoans).toHaveBeenCalled();
  });

  // admin actions test
  it('should approve loan', () => {
    loanServiceSpy.updateStatus.and.returnValue(of({}));
    spyOn(component, 'refreshLoans');

    component.approveLoan('1');

    expect(loanServiceSpy.updateStatus)
      .toHaveBeenCalledWith('1', 'APPROVED');
    expect(component.refreshLoans).toHaveBeenCalled();
  });

  it('should reject loan', () => {
    loanServiceSpy.updateStatus.and.returnValue(of({}));
    spyOn(component, 'refreshLoans');

    component.rejectLoan('1');

    expect(loanServiceSpy.updateStatus)
      .toHaveBeenCalledWith('1', 'REJECTED');
    expect(component.refreshLoans).toHaveBeenCalled();
  });

  it('should review loan', () => {
    loanServiceSpy.updateStatus.and.returnValue(of({}));
    spyOn(component, 'refreshLoans');

    component.reviewLoan('1');

    expect(loanServiceSpy.updateStatus)
      .toHaveBeenCalledWith('1', 'UNDER_REVIEW');
    expect(component.refreshLoans).toHaveBeenCalled();
  });

  // pricing actions test
  it('should open pricing panel', () => {
    component.openPricing(mockLoans[0]);

    expect(component.pricingLoan).toEqual(mockLoans[0]);
    expect(component.baseRate).toBe(8.5);
    expect(component.creditRating).toBe('AA');
  });

  it('should cancel pricing', () => {
    component.pricingLoan = mockLoans[0];
    component.cancelPricing();

    expect(component.pricingLoan).toBeNull();
  });

  it('should alert if pricing loan is invalid', () => {
    spyOn(window, 'alert');

    component.pricingLoan = null;
    component.calculatePrice();

    expect(window.alert).toHaveBeenCalledWith('Invalid loan selected');
  });

  it('should calculate price successfully', () => {
    spyOn(window, 'alert');
    spyOn(component, 'refreshLoans');

    component.pricingLoan = mockLoans[0];
    loanServiceSpy.calculatePrice.and.returnValue(of({}));

    component.calculatePrice();

    expect(loanServiceSpy.calculatePrice).toHaveBeenCalled();
    expect(window.alert).toHaveBeenCalledWith('Pricing calculated successfully');
    expect(component.pricingLoan).toBeNull();
    expect(component.refreshLoans).toHaveBeenCalled();
  });

  it('should handle pricing failure', () => {
    spyOn(window, 'alert');

    component.pricingLoan = mockLoans[0];
    loanServiceSpy.calculatePrice.and.returnValue(
      throwError(() => new Error())
    );

    component.calculatePrice();

    expect(window.alert).toHaveBeenCalledWith('Pricing failed');
  });

  // delete test
  it('should not delete loan if id is undefined', () => {
    component.deleteLoan(undefined);
    expect(loanServiceSpy.deleteLoan).not.toHaveBeenCalled();
  });

  it('should cancel delete if not confirmed', () => {
    spyOn(window, 'confirm').and.returnValue(false);

    component.deleteLoan('1');

    expect(loanServiceSpy.deleteLoan).not.toHaveBeenCalled();
  });

  it('should delete loan when confirmed', () => {
    spyOn(window, 'confirm').and.returnValue(true);
    spyOn(window, 'alert');
    spyOn(component, 'refreshLoans');

    loanServiceSpy.deleteLoan.and.returnValue(of({}));

    component.deleteLoan('1');

    expect(loanServiceSpy.deleteLoan).toHaveBeenCalledWith('1');
    expect(window.alert).toHaveBeenCalledWith('Loan deleted successfully');
    expect(component.refreshLoans).toHaveBeenCalled();
  });

  it('should handle delete failure', () => {
    spyOn(window, 'confirm').and.returnValue(true);
    spyOn(window, 'alert');

    loanServiceSpy.deleteLoan.and.returnValue(
      throwError(() => new Error())
    );

    component.deleteLoan('1');

    expect(window.alert).toHaveBeenCalledWith('Failed to delete loan');
  });
});
