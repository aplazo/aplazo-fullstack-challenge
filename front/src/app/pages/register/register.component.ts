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

@Component({
  standalone: true,
  selector: 'app-register',
  templateUrl: './register.component.html',
  imports: [ReactiveFormsModule, AplazoButtonComponent, AplazoLogoComponent],
})
export class RegisterComponent implements OnInit {
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
      dateOfBirth: new Date('1990-01-01'),
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

    const dateOfBirth = this.form.value.dateOfBirth?.toISOString() || '';
    const createCustomerRequest: CreateCustomerRequest = {
      firstName: this.form.value.firstName || '',
      lastName: this.form.value.lastName || '',
      secondLastName: this.form.value.secondLastName || '',
      dateOfBirth,
    };
    this.customersService.createCustomer(createCustomerRequest).subscribe({
      next: (customer) => {
        console.log('Customer created:', customer);
      },
      error: (error) => {
        console.error('Error creating customer:', error);
      },
    });
  }
}
