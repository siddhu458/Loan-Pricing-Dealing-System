import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { AuthService } from './auth.service';
import { TokenService } from '../../core/services/token.service';
import { Router } from '@angular/router';

describe('AuthService', () => {
  let service: AuthService;
  let httpMock: HttpTestingController;
  let tokenServiceSpy: jasmine.SpyObj<TokenService>;
  let routerSpy: jasmine.SpyObj<Router>;

  const API_URL = 'http://localhost:9999/api/auth';

  beforeEach(() => {
    tokenServiceSpy = jasmine.createSpyObj('TokenService', [
      'saveToken',
      'clearToken'
    ]);

    routerSpy = jasmine.createSpyObj('Router', ['navigate']);

    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        AuthService,
        { provide: TokenService, useValue: tokenServiceSpy },
        { provide: Router, useValue: routerSpy }
      ]
    });

    service = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify(); 
  });

  // Register
  it('should register user', () => {
    const payload = {
      email: 'admin@test.com',
      password: 'password',
      role: 'ROLE_ADMIN'
    };

    service.register(payload.email, payload.password, payload.role)
      .subscribe(res => {
        expect(res).toBeTruthy();
      });

    const req = httpMock.expectOne(`${API_URL}/register`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(payload);

    req.flush({});
  });

  // login
  it('should login and save token', () => {
    const credentials = {
      email: 'user@test.com',
      password: 'password'
    };

    const mockResponse = {
      token: 'mock-jwt-token'
    };

    service.login(credentials).subscribe(res => {
      expect(res.token).toBe('mock-jwt-token');
    });

    const req = httpMock.expectOne(`${API_URL}/login`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(credentials);

    req.flush(mockResponse);

    
    expect(tokenServiceSpy.saveToken).toHaveBeenCalledWith('mock-jwt-token');
  });

  // logout
  it('should clear token and navigate to login on logout', () => {
    service.logout();

    expect(tokenServiceSpy.clearToken).toHaveBeenCalled();
    expect(routerSpy.navigate).toHaveBeenCalledWith(['/auth/login']);
  });
});
