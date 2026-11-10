import { Component, inject } from '@angular/core';
import { RouterLink } from "@angular/router";
import { OrderStateService } from '../../core/services/order-state-service';
import { HttpClient } from '@angular/common/http';
import { CustomerStateService } from '../../core/services/customer-state-service';

@Component({
  selector: 'app-header',
  imports: [RouterLink],
  templateUrl: './header.html',
  styleUrl: './header.css',
})
export class Header {

  shoppingCartState = inject(OrderStateService);
  httpClient = inject(HttpClient);
  customerState = inject(CustomerStateService);

  shoppingCart = this.shoppingCartState.shoppingCart;
  customer = this.customerState.customer;

  /*
   * Cuando se intenta añadir un producto al cart o se intenta acceder al cart se comprueba si está logeado y sino se envía a login
   * Una vez logueado se comprueba si hay una Order con el status CART
   * Si no existiera en la base de datos, el backend crearía una nueva y la devolvería
   */

  login() {
    window.location.href = '/oauth2/authorization/gateway';
  }

  logout() {
    this.customerState.deleteCustomer();
    this.shoppingCartState.resetShoppingCart();
    const form = document.createElement('form');
    form.method = 'POST';
    form.action = '/logout';
    document.body.appendChild(form);
    form.submit();
  }

}
