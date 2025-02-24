import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class CustomersService {
  private apiUrl = 'http://localhost:8080/v1/customers';

  constructor(private http: HttpClient) {}

  // Fetch user by ID
  getCustomerById(id: number): Observable<CustomerResponse> {
    return this.http.get<CustomerResponse>(`${this.apiUrl}/${id}`);
  }

  // Create a new user
  createCustomer(
    customer: CreateCustomerRequest
  ): Observable<CustomerResponse> {
    console.log(customer);
    return this.http.post<CustomerResponse>(this.apiUrl, customer);
  }
}

export interface CreateCustomerRequest {
  firstName: string;
  lastName: string;
  secondLastName: string;
  dateOfBirth: string;
}

export interface CustomerResponse {
  id: string;
  creditLineAmount: number;
  availableCreditLineAmount: number;
  createdAt: string;
}
