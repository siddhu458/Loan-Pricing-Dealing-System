import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Loancreate } from './loancreate';
import { Router } from '@angular/router';
import { of } from 'rxjs';
import { FormsModule } from '@angular/forms';
import { Loanservice } from '../../services/loan.service';

describe('Loancreate Component', () => {
  let component: Loancreate;
  let fixture: ComponentFixture<Loancreate>;
  let loanServiceSpy: jasmine.SpyObj<Loanservice>;
  let routerSpy: jasmine.SpyObj<Router>;

  beforeEach(async () => {
    loanServiceSpy = jasmine.createSpyObj('Loanservice', ['createLoan']);
    routerSpy = jasmine.createSpyObj('Router', ['navigate']);

    await TestBed.configureTestingModule({
      declarations: [Loancreate],
      imports: [FormsModule],
      providers: [
        { provide: Loanservice, useValue: loanServiceSpy },
        { provide: Router, useValue: routerSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(Loancreate);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should call createLoan and navigate on success', () => {
    // GIVEN (form filled like real user)
    component.loan = {
      clientName: 'ABC Corp',
      loanType: 'Business',
      requestedAmount: 100000,
      proposedInterestRate: 10,
      tenureMonths: 12
    };

    loanServiceSpy.createLoan.and.returnValue(of({}));

    // WHEN
    component.submit();

    // THEN
    expect(loanServiceSpy.createLoan).toHaveBeenCalledWith({
      clientName: 'ABC Corp',
      loanType: 'Business',
      requestedAmount: 100000,
      proposedInterestRate: 10,
      tenureMonths: 12
    });

    expect(routerSpy.navigate).toHaveBeenCalledWith(['/loans']);
  });
});
