import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { BASE_URL } from './constants';

@Injectable({
  providedIn: 'root',
})
export class CustomersService {
  private apiUrl = `${BASE_URL}/customers`;

  constructor(private http: HttpClient) {}

  getCustomerById(id: number): Observable<CustomerResponse> {
    return this.http.get<CustomerResponse>(`${this.apiUrl}/${id}`);
  }

  createCustomer(
    customer: CreateCustomerRequest
  ): Observable<HttpResponse<CustomerResponse>> {
    console.log(customer);
    return this.http.post<CustomerResponse>(this.apiUrl, customer, {
      observe: 'response',
    });
  }
}

export interface CreateCustomerRequest {
  firstName: string;
  lastName: string;
  secondLastName: string;
  dateOfBirth: Date;
}

export interface CustomerResponse {
  id: string;
  creditLineAmount: number;
  availableCreditLineAmount: number;
  createdAt: string;
}
