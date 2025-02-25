import { Injectable } from '@angular/core';
import { CustomerResponse } from './customers.service';
import { HttpResponse } from '@angular/common/http';

const TOKEN_KEY = 'token';
const USER_ID_KEY = 'userId';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  saveCustomerDetails(response: HttpResponse<CustomerResponse>) {
    const customerResponse = response.body!;
    localStorage.setItem(USER_ID_KEY, customerResponse.id);

    const token = response.headers.get('X-Auth-Token')!;
    localStorage.setItem(TOKEN_KEY, token);
  }

  getUserDetails(): UserDetails | null {
    const id = localStorage.getItem(USER_ID_KEY);
    const token = localStorage.getItem(TOKEN_KEY);
    if (!id || !token) return null;
    return { id, token };
  }

  deleteUserDetails() {
    localStorage.removeItem(USER_ID_KEY);
    localStorage.removeItem(TOKEN_KEY);
  }

  isLoggedIn(): boolean {
    const userDetails = this.getUserDetails();
    return userDetails !== null;
  }
}

interface UserDetails {
  id: string;
  token: string;
}
