import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';

import { Login } from './login';
import { AuthService } from '../../services/auth.service';
import { TokenService } from '../../../core/services/token.service';

describe('Login Component', () => {
  let component: Login;
  let fixture: ComponentFixture<Login>;
  let authServiceSpy: jasmine.SpyObj<AuthService>;
  let tokenServiceSpy: jasmine.SpyObj<TokenService>;
  let routerSpy: jasmine.SpyObj<Router>;

  beforeEach(async () => {
    authServiceSpy = jasmine.createSpyObj('AuthService', ['login']);
    tokenServiceSpy = jasmine.createSpyObj('TokenService', ['getUserRole']);
    routerSpy = jasmine.createSpyObj('Router', ['navigate']);

    await TestBed.configureTestingModule({
      declarations: [Login],
      imports: [ReactiveFormsModule],
      providers: [
        { provide: AuthService, useValue: authServiceSpy },
        { provide: TokenService, useValue: tokenServiceSpy },
        { provide: Router, useValue: routerSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(Login);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  //Component creation
  it('should create', () => {
    expect(component).toBeTruthy();
  });

  //Should NOT call service if form is invalid
  it('should not call login service if form is invalid', () => {
    component.loginForm.setValue({
      email: '',
      password: ''
    });

    component.login();

    expect(authServiceSpy.login).not.toHaveBeenCalled();
  });

  //Admin login → /admin
  it('should login and navigate to admin for admin', () => {
    component.loginForm.setValue({
      email: 'admin@test.com',
      password: 'admin123'
    });

    authServiceSpy.login.and.returnValue(of({ token: 'mock-token' }));
    tokenServiceSpy.getUserRole.and.returnValue('ROLE_ADMIN');

    component.login();

    expect(authServiceSpy.login).toHaveBeenCalledWith({
      email: 'admin@test.com',
      password: 'admin123'
    });
    expect(routerSpy.navigate).toHaveBeenCalledWith(['/admin']);
  });

  // User login → /loans
  it('should login and navigate to loans for user', () => {
    component.loginForm.setValue({
      email: 'user@test.com',
      password: 'user123'
    });

    authServiceSpy.login.and.returnValue(of({ token: 'mock-token' }));
    tokenServiceSpy.getUserRole.and.returnValue('ROLE_USER');

    component.login();

    expect(routerSpy.navigate).toHaveBeenCalledWith(['/loans']);
  });

  // Login failure → show error message
  it('should show error message on login failure', () => {
    component.loginForm.setValue({
      email: 'wrong@test.com',
      password: 'wrong123'
    });

    authServiceSpy.login.and.returnValue(
      throwError(() => new Error('Unauthorized'))
    );

    component.login();
    fixture.detectChanges();

    expect(component.errorMessage).toBe('Invalid email or password');
  });
});
