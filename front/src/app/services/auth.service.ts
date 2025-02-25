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

  getUserDetails(): UserDetails {
    const id = localStorage.getItem(USER_ID_KEY) || '';
    const token = localStorage.getItem(TOKEN_KEY) || '';
    return { id, token };
  }

  deleteUserDetails() {
    localStorage.removeItem(USER_ID_KEY);
    localStorage.removeItem(TOKEN_KEY);
  }

  isLoggedIn(): boolean {
    // TODO: check if token is expired
    // return !!this.getToken();
    return true;
  }
}

interface UserDetails {
  id: string;
  token: string;
}
