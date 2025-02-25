import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { BASE_URL } from './constants';

@Injectable({
  providedIn: 'root',
})
export class LoansService {
  private apiUrl = `${BASE_URL}/loans`;

  constructor(private http: HttpClient) {}

  getLoanById(id: number): Observable<LoanResponse> {
    return this.http.get<LoanResponse>(`${this.apiUrl}/${id}`);
  }

  createLoan(loan: CreateLoanRequest): Observable<LoanResponse> {
    return this.http.post<LoanResponse>(this.apiUrl, loan);
  }
}

export interface CreateLoanRequest {
  customerId: string;
  amount: number;
}

export interface LoanResponse {}
