import { HttpClient } from '@angular/common/http';
import { Component, inject } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Customer } from '../../shared/models/Customer';
import { CustomerStateService } from '../../core/services/customer-state-service';
import { OrderStateService } from '../../core/services/order-state-service';
import { Chat } from '../../layout/chat/chat';

@Component({
  selector: 'app-shipping',
  imports: [
    ReactiveFormsModule,
    Chat
  ],
  templateUrl: './shipping.html',
  styleUrl: './shipping.css',
})
export class Shipping {

  shoppingCartState = inject(OrderStateService);
  router = inject(Router);
  formBuilder = inject(FormBuilder);
  httpClient = inject(HttpClient);
  customerState = inject(CustomerStateService);

  customer = this.customerState.customer;
  shoppingCart = this.shoppingCartState.shoppingCart;

  addressForm = this.initForm();


  /**
   * Utiliza nullish coalescing operator para usar el valor en caso de que lo haya o deje el campo vacío
   * en caso de que el resultado de la operación sea null o undefined
   */
  initForm(): FormGroup {
    return this.formBuilder.group({
      address: [this.customer()?.address ?? '', Validators.required],
      city: [this.customer()?.city ?? '', Validators.required],
      postalCode: [this.customer()?.postalCode ?? ''],
      phone: [this.customer()?.phone ?? '']
    })
  }

  /**
   * Actualiza la información de envío, en caso de ser necesario y setea los cambios en el state
   * Después hace una llamada al backend para crear un PaymentIntent, que devuelve el clientSecret
   * Setea ese clientSecret en el state y redirige al cliente a la pasarela de pagos
   */
  pay() {
    if (this.addressForm.valid) {
      this.httpClient.patch<Customer>('/api/users/shipmentDetails', this.addressForm.value).subscribe(response => {
        this.customerState.setCustomer(response);
        this.httpClient.post<any>('/api/orders/payment', this.shoppingCart()).subscribe(response => {
          this.shoppingCartState.setClientSecret(response.clientSecret as string);
          this.router.navigate(['/payment']);
        })
      });
    }
  }

  goToCart() {
    this.router.navigate(['/cart']);
  }
}
