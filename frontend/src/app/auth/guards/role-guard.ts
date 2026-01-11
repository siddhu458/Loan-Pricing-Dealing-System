import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, Router } from '@angular/router';
import { TokenService } from '../../core/services/token.service';

@Injectable({
  providedIn: 'root'
})
export class RoleGuard implements CanActivate {

  constructor(
    private tokenService: TokenService,
    private router: Router
  ) {}

  canActivate(route: ActivatedRouteSnapshot): boolean {

    const expectedRoles = route.data['roles'] || route.data['role']; // Support both array and single
    const userRole = this.tokenService.getUserRole();

    console.log('ROLE GUARD:', { expectedRoles, userRole });

    if (!userRole) {
      this.router.navigate(['/auth/login']);
      return false;
    }

    // Normalize role
    const normalizedRole =
      userRole.startsWith('ROLE_') ? userRole : `ROLE_${userRole}`;

    // Check if user role is in expected roles (array or single)
    if (Array.isArray(expectedRoles)) {
      const hasRole = expectedRoles.some(role => {
        const normalizedExpected = role.startsWith('ROLE_') ? role : `ROLE_${role}`;
        return normalizedRole === normalizedExpected;
      });
      
      if (hasRole) {
        return true;
      }
    } else {
      const normalizedExpected = expectedRoles.startsWith('ROLE_') ? expectedRoles : `ROLE_${expectedRoles}`;
      if (normalizedRole === normalizedExpected) {
        return true;
      }
    }

    this.router.navigate(['/loans']);
    return false;
  }
}
