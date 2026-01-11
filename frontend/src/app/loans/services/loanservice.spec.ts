import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { Loanservice } from './loan.service';

describe('Loanservice', () => {
  let service: Loanservice;
  let httpMock: HttpTestingController;

  const baseUrl = 'http://localhost:9999/api/loans';

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Loanservice]
    });

    service = TestBed.inject(Loanservice);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify(); 
  });

 // GET LOANS
  it('should fetch loans', () => {
    const mockResponse = { content: [] };

    service.getLoans().subscribe(res => {
      expect(res).toEqual(mockResponse);
    });

    const req = httpMock.expectOne(`${baseUrl}?page=0&size=5`);
    expect(req.request.method).toBe('GET');

    req.flush(mockResponse);
  });

// create loan
  it('should create loan', () => {
    const payload = {
      clientName: 'ABC',
      loanType: 'Business',
      requestedAmount: 100000,
      proposedInterestRate: 10,
      tenureMonths: 12
    };

    service.createLoan(payload).subscribe(res => {
      expect(res).toBeTruthy();
    });

    const req = httpMock.expectOne(baseUrl);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(payload);

    req.flush({});
  });

 // submit loan
  it('should submit loan', () => {
    service.submitLoan('1').subscribe(res => {
      expect(res).toBeTruthy();
    });

    const req = httpMock.expectOne(`${baseUrl}/1/submit`);
    expect(req.request.method).toBe('PATCH');
    expect(req.request.body).toEqual({});

    req.flush({});
  });

// update loan status
  it('should update loan status', () => {
    service.updateStatus('1', 'APPROVED', 'ok').subscribe(res => {
      expect(res).toBeTruthy();
    });

    const req = httpMock.expectOne(`${baseUrl}/1/status`);
    expect(req.request.method).toBe('PATCH');
    expect(req.request.body).toEqual({
      status: 'APPROVED',
      comments: 'ok'
    });

    req.flush({});
  });

  it('should update loan status without comments', () => {
    service.updateStatus('1', 'REJECTED').subscribe(res => {
      expect(res).toBeTruthy();
    });

    const req = httpMock.expectOne(`${baseUrl}/1/status`);
    expect(req.request.method).toBe('PATCH');
    expect(req.request.body).toEqual({
      status: 'REJECTED',
      comments: undefined
    });

    req.flush({});
  });

// calculate loan price
it('should calculate loan price', () => {
  const pricingData = {
    baseRate: 8.5,
    creditRating: 'AA'
  };

  service.calculatePrice('1', pricingData).subscribe(res => {
    expect(res).toBeTruthy();
  });

  const req = httpMock.expectOne(`${baseUrl}/1/price`);
  expect(req.request.method).toBe('POST');
  expect(req.request.body).toEqual(pricingData);

  req.flush({});
});

// delete loan
it('should delete loan', () => {
  service.deleteLoan('1').subscribe(res => {
    expect(res).toBeTruthy();
  });

  const req = httpMock.expectOne(`${baseUrl}/1`);
  expect(req.request.method).toBe('DELETE');

  req.flush({});
});
});
