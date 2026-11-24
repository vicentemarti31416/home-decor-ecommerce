import { Component, inject, OnInit } from '@angular/core';
import { OrderStateService } from '../../core/services/order-state-service';
import { Router } from '@angular/router';
import { Chat } from '../../layout/chat/chat';

@Component({
  selector: 'app-success',
  imports: [
    Chat
  ],
  templateUrl: './success.html',
  styleUrl: './success.css',
})
export class Success implements OnInit {

  shoppingCartState = inject(OrderStateService);
  router = inject(Router);

  /**
   * Resetea el carrito de la compra después del pago a null para que se cree uno nuevo
   * Espera 7 segundos para redirigir a la galería principal mientras se muestra el mensaje de éxito
   */
  ngOnInit(): void {
    this.shoppingCartState.resetShoppingCart();
    setTimeout(() => {
      this.router.navigate(['/'])
    }, 7000);
  }

}
