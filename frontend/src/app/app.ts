import { Component, inject, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { OrderStateService } from './core/services/order-state-service';
import { CustomerStateService } from './core/services/customer-state-service';
import { HttpClient } from '@angular/common/http';
import { Customer } from './shared/models/Customer';
import { catchError, of, tap } from 'rxjs';
import { Order } from './shared/models/Order';
import { ParticipantService } from './core/services/participant-service';
import { Participant } from './shared/models/Participant';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App implements OnInit {

  shoppingCartState = inject(OrderStateService);
  customerState = inject(CustomerStateService);
  httpClient = inject(HttpClient);
  participantService = inject(ParticipantService);

  shoppingCart = this.shoppingCartState.shoppingCart;
  customer = this.customerState.customer;

  /**
   * Comprueba si hay un Customer o lo busca en la base de datos.
   * Si es de tipo admin trae todos sus contactos, si es de tipo user fija el admin como único contacto.
   * Después busca una orden de tipo carrito de compra en la base de datos y si no la hay, la crea.
   */
  ngOnInit(): void {
    if (!this.customer()) this.httpClient.get<Customer>('/api/users/me').pipe(
      tap(response => {
        this.customerState.setCustomer(response);
        if(response?.roles[0] === 'ROLE_ADMIN') {
          this.participantService.getParticipants();
        } else if(response?.roles[0] === 'ROLE_USER') {
          const participant: Participant = {
            username: 'vicentemarti31416@gmail.com',
            photo: 'https://res.cloudinary.com/ddjupcyst/image/upload/fl_preserve_transparency/v1791892905/cwsnc78ic5f5pubsh6jj.jpg?_s=public-apps'
          }
          this.participantService.participant.set(participant);
        }
      }),
      catchError(() => {
        return of(false);
      })
    ).subscribe();
    if (!this.shoppingCart()) this.httpClient.get<Order>(`/api/orders/status/${'CART'}`).pipe(
      tap(response => this.shoppingCartState.setShoppingCart(response)),
      catchError(() => {
        return of(false);
      })
    ).subscribe();
  }
}
