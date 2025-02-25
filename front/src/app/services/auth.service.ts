import { Injectable } from '@angular/core';

const TOKEN_KEY = 'token';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  saveToken(token: string) {
    localStorage.setItem(TOKEN_KEY, token || '');
  }

  getToken(): string {
    return localStorage.getItem(TOKEN_KEY) || '';
  }

  removeToken() {
    localStorage.removeItem(TOKEN_KEY);
  }

  isLoggedIn(): boolean {
    // TODO: check if token is expired
    return !!this.getToken();
  }
}
