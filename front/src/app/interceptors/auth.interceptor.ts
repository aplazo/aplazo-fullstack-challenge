import { HttpInterceptorFn } from '@angular/common/http';
import { AuthService } from '../services/auth.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = new AuthService();
  const userDetails = authService.getUserDetails();

  if (!userDetails) return next(req);

  const requestWithAuthorization = req.clone({
    headers: req.headers.set('Authorization', 'Bearer ' + userDetails.token),
  });
  return next(requestWithAuthorization);
};
