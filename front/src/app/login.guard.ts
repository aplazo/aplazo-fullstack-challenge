import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from './services/auth.service';
import { inject } from '@angular/core';
import { ROUTE_CONFIG } from './config/routes.config';

export const loginGuard: CanActivateFn = (route, state) => {
  const router = inject(Router);
  const authService = inject(AuthService);
  const isLoggedIn = authService.isLoggedIn();

  if (isLoggedIn) {
    router.navigate([ROUTE_CONFIG.app, ROUTE_CONFIG.home]);
    return false;
  }

  return true;
};
