import { inject } from '@angular/core';
import { CanActivateFn } from '@angular/router';
import { CustomerStateService } from '../services/customer-state-service';
import { HttpClient } from '@angular/common/http';
import { Customer } from '../../shared/models/Customer';
import { catchError, map, of } from 'rxjs';


export const authGuard: CanActivateFn = () => {
  const customerState = inject(CustomerStateService);
  const httpClient = inject(HttpClient);

  if (customerState.customer()) {
    return true;
  }

  return httpClient.get<Customer>('/api/users/me').pipe(
    map(customer => {
      customerState.setCustomer(customer);
      return true;
    }),
    catchError(() => {
      window.location.href = '/oauth2/authorization/gateway';
      return of(false);
    })
  );
};
