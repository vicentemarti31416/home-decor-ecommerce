import { Injectable, signal } from '@angular/core';
import { Customer } from '../../shared/models/Customer';

@Injectable({
  providedIn: 'root',
})
export class CustomerStateService {

  private _customer = signal<Customer | null>(null);

  customer = this._customer.asReadonly();

  setCustomer(customer: Customer) {
    this._customer.set(customer);
  }

  deleteCustomer() {
    this._customer.set(null);
  }
}
