import { Component, OnInit } from '@angular/core';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { AplazoButtonComponent } from '@apz/shared-ui/button';
import { AplazoLogoComponent } from '@apz/shared-ui/logo';
import {
  CreateCustomerRequest,
  CustomersService,
} from '../../services/customers.service';
import { NgIf } from '@angular/common';

@Component({
  standalone: true,
  selector: 'app-register',
  templateUrl: './register.component.html',
  imports: [
    ReactiveFormsModule,
    AplazoButtonComponent,
    AplazoLogoComponent,
    NgIf,
  ],
})
export class RegisterComponent implements OnInit {
  errorMessage: string | null = null;

  constructor(private customersService: CustomersService) {}

  readonly firstName = new FormControl<string>('', {
    nonNullable: true,
    validators: [Validators.required],
  });

  readonly lastName = new FormControl<string>('', {
    nonNullable: true,
    validators: [Validators.required],
  });

  readonly secondLastName = new FormControl<string>('', {
    nonNullable: true,
    validators: [Validators.required],
  });

  readonly dateOfBirth = new FormControl<Date>(new Date(), {
    nonNullable: true,
    validators: [Validators.required],
  });

  ngOnInit(): void {
    // TODO: remove this
    this.form.setValue({
      firstName: 'John',
      lastName: 'Doe',
      secondLastName: 'Doe',
      dateOfBirth: new Date('2000-01-01'),
    });
  }

  readonly form = new FormGroup({
    firstName: this.firstName,
    lastName: this.lastName,
    secondLastName: this.secondLastName,
    dateOfBirth: this.dateOfBirth,
  });

  register(): void {
    console.log('Registering customer:', this.form.value);
    if (this.form.invalid) {
      console.log('Invalid form');
      return;
    }

    this.errorMessage = null;

    const createCustomerRequest: CreateCustomerRequest = {
      firstName: this.form.value.firstName || '',
      lastName: this.form.value.lastName || '',
      secondLastName: this.form.value.secondLastName || '',
      dateOfBirth: this.form.value.dateOfBirth || new Date(),
    };
    this.customersService.createCustomer(createCustomerRequest).subscribe({
      next: (customer) => {
        console.log('Customer created:', customer);
      },
      error: (error) => {
        console.error('Error creating customer:', error);
        this.errorMessage = error.error?.message || 'Failed to create customer';
        console.log('Error creating customer:', this.errorMessage);
      },
    });
  }
}
