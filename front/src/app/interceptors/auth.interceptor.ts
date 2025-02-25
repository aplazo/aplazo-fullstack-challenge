import {
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpInterceptorFn,
  HttpRequest,
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AuthService } from '../services/auth.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = new AuthService();
  const token = authService.getToken();

  if (!token) return next(req);

  const requestWithAuthorization = req.clone({
    headers: req.headers.set('Authorization', 'Bearer ' + token),
  });
  return next(requestWithAuthorization);
};
