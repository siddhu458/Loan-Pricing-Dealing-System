import { TestBed } from '@angular/core/testing';
import { TokenService } from './token.service';

describe('TokenService', () => {
  let service: TokenService;

  const mockToken =
    'header.' +
    btoa(JSON.stringify({ role: 'ROLE_ADMIN' })) +
    '.signature';

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [TokenService]
    });

    service = TestBed.inject(TokenService);
    localStorage.clear();
  });

  afterEach(() => {
    localStorage.clear();
  });

 // token storage
  it('should save token in localStorage', () => {
    service.saveToken('test-token');
    expect(localStorage.getItem('auth_token')).toBe('test-token');
  });

  it('should get token from localStorage', () => {
    localStorage.setItem('auth_token', 'test-token');
    expect(service.getToken()).toBe('test-token');
  });

  it('should clear token from localStorage', () => {
    localStorage.setItem('auth_token', 'test-token');
    service.clearToken();
    expect(localStorage.getItem('auth_token')).toBeNull();
  });

  // role extraction
  it('should return null role if token is missing', () => {
    expect(service.getUserRole()).toBeNull();
  });

  it('should extract role from JWT token', () => {
    service.saveToken(mockToken);
    expect(service.getUserRole()).toBe('ROLE_ADMIN');
  });

  // role checks
  it('should return true for isAdmin when role is ROLE_ADMIN', () => {
    service.saveToken(mockToken);
    expect(service.isAdmin()).toBeTrue();
  });

  it('should return false for isAdmin when role is not admin', () => {
    const userToken =
      'header.' +
      btoa(JSON.stringify({ role: 'ROLE_USER' })) +
      '.signature';

    service.saveToken(userToken);
    expect(service.isAdmin()).toBeFalse();
  });

  it('should return true for isUser when role is ROLE_USER', () => {
    const userToken =
      'header.' +
      btoa(JSON.stringify({ role: 'ROLE_USER' })) +
      '.signature';

    service.saveToken(userToken);
    expect(service.isUser()).toBeTrue();
  });

  it('should return false for isUser when role is not user', () => {
    service.saveToken(mockToken);
    expect(service.isUser()).toBeFalse();
  });

  // login status
  it('should return true when logged in', () => {
    service.saveToken('some-token');
    expect(service.isLoggedIn()).toBeTrue();
  });

  it('should return false when not logged in', () => {
    expect(service.isLoggedIn()).toBeFalse();
  });
});
