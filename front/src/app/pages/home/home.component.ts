import { Component, OnInit } from '@angular/core';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { AplazoButtonComponent } from '@apz/shared-ui/button';
import { AplazoLogoComponent } from '@apz/shared-ui/logo';
import { NgIf } from '@angular/common';
import { Router } from '@angular/router';
import { CreateLoanRequest, LoansService } from '../../services/loans.service';
import { AuthService } from '../../services/auth.service';
import { amountValidator, uuidValidator } from '../../utils/validators';

@Component({
  standalone: true,
  selector: 'app-home',
  templateUrl: './home.component.html',
  imports: [
    ReactiveFormsModule,
    AplazoButtonComponent,
    AplazoLogoComponent,
    NgIf,
  ],
})
export class HomeComponent implements OnInit {
  errorMessage: string | null = null;
  successMessage: string | null = null;

  constructor(
    private loansService: LoansService,
    private authService: AuthService
  ) {}

  readonly customerId = new FormControl<string>('', {
    nonNullable: true,
    validators: [Validators.required, uuidValidator],
  });

  readonly amount = new FormControl<number>(0, {
    nonNullable: true,
    validators: [Validators.required, amountValidator],
  });

  ngOnInit(): void {
    const userDetails = this.authService.getUserDetails()!;
    this.form.setValue({
      customerId: userDetails.id,
      amount: 5,
    });
  }

  readonly form = new FormGroup({
    customerId: this.customerId,
    amount: this.amount,
  });

  createLoan(): void {
    console.log('Creating loan:', this.form.value);
    if (this.form.invalid) {
      this.errorMessage = 'Please fill out all fields';
      return;
    }

    this.errorMessage = null;
    this.successMessage = null;

    const createLoanRequest: CreateLoanRequest = {
      customerId: this.customerId.value,
      amount: this.amount.value,
    };
    this.loansService.createLoan(createLoanRequest).subscribe({
      next: (customer) => {
        console.log('Loan created:', customer);
        // TODO: show a snackbar or something?
        this.successMessage = 'Loan created successfully';
      },
      error: (error) => {
        this.errorMessage = error.error?.message || 'Failed to create loan';
        console.log('Error creating loan:', this.errorMessage);
      },
    });
  }
}
