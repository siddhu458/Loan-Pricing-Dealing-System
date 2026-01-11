import { TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { RoleGuard } from './role-guard';
import { TokenService } from '../../core/services/token.service';


describe('RoleGuard', () => {
  let guard: RoleGuard;
  let tokenServiceSpy: jasmine.SpyObj<TokenService>;
  let routerSpy: jasmine.SpyObj<Router>;

  beforeEach(() => {
    tokenServiceSpy = jasmine.createSpyObj('TokenService', ['getUserRole']);
    routerSpy = jasmine.createSpyObj('Router', ['navigate']);

    TestBed.configureTestingModule({
      providers: [
        RoleGuard,
        { provide: TokenService, useValue: tokenServiceSpy },
        { provide: Router, useValue: routerSpy }
      ]
    });

    guard = TestBed.inject(RoleGuard);
  });

  it('should allow access when role matches', () => {
    tokenServiceSpy.getUserRole.and.returnValue('ROLE_ADMIN');

    const result = guard.canActivate({
      data: { role: 'ROLE_ADMIN' }
    } as any);

    expect(result).toBeTrue();
  });

  it('should deny access when role does not match', () => {
    tokenServiceSpy.getUserRole.and.returnValue('ROLE_USER');

    const result = guard.canActivate({
      data: { role: 'ROLE_ADMIN' }
    } as any);

    expect(result).toBeFalse();
    expect(routerSpy.navigate).toHaveBeenCalledWith(['/loans']);
  });

  it('should deny access when role is missing', () => {
    tokenServiceSpy.getUserRole.and.returnValue('');

    const result = guard.canActivate({
      data: { role: 'ROLE_ADMIN' }
    } as any);

    expect(result).toBeFalse();
    expect(routerSpy.navigate).toHaveBeenCalledWith(['/auth/login']);
  });
});
