import { Component, inject, OnInit, signal, ViewChild } from '@angular/core';
import { StripeElementsOptions } from '@stripe/stripe-js';
import { injectStripe, StripeElementsDirective, StripePaymentElementComponent } from 'ngx-stripe';
import { OrderStateService } from '../../core/services/order-state-service';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { Chat } from '../../layout/chat/chat';

@Component({
  selector: 'app-payments',
  imports: [
    StripeElementsDirective,
    StripePaymentElementComponent,
    Chat
  ],
  templateUrl: './payment.html',
  styleUrl: './payment.css',
})
export class Payment implements OnInit {

  ngOnInit(): void {
    if(!this.clientSecret()) this.router.navigate(['/cart']); 
  }

  router = inject(Router);
  httpClient = inject(HttpClient);
  shoppingCartState = inject(OrderStateService);
  stripe = injectStripe();

  // Sirve para indicar al confirmPayment cual es el elemento stripe que usaremos para realizar el pago
  @ViewChild(StripePaymentElementComponent) paymentElement!: StripePaymentElementComponent;

  clientSecret = this.shoppingCartState.clientSecret;
  shoppingCart = this.shoppingCartState.shoppingCart;

  paying = signal(false);

  //Con este elemento se modifica la apariencia de la pasarela de pagos con stripe
  elementsOptions = signal<StripeElementsOptions>({
    locale: 'es',
    clientSecret: this.clientSecret(),
    appearance: {
      theme: 'stripe', // 'stripe' | 'night' | 'flat' | 'none'
      variables: {
        // Colores
        colorPrimary: '#6366f1',
        colorBackground: '#ffffff',
        colorText: '#1f2937',
        colorDanger: '#ef4444',

        // Tipografía
        fontFamily: 'Inter, sans-serif',
        fontSizeBase: '16px',

        // Bordes
        borderRadius: '8px',
        spacingUnit: '4px',
      },
      rules: {
        '.Input': {
          border: '1px solid #e5e7eb',
          boxShadow: 'none',
          padding: '12px',
        },
        '.Input:focus': {
          border: '1px solid #6366f1',
          boxShadow: '0 0 0 3px rgba(99, 102, 241, 0.1)',
        },
        '.Input--invalid': {
          border: '1px solid #ef4444',
        },
        '.Label': {
          fontWeight: '500',
          marginBottom: '6px',
        },
        '.Error': {
          color: '#ef4444',
          fontSize: '14px',
        }
      }
    }
  });

  /**
   * Realiza en pago con stripe.
   * Utiliza paying para que no se pueda volver a hacer click en el botón de pago mientras se está realizando el mismo
   */
  pay() {
    if (this.paying()) return;
    this.paying.set(true);
    this.stripe.confirmPayment({
      elements: this.paymentElement.elements,
      confirmParams: {
        return_url: 'http://127.0.0.1:8090/success'
      },
      redirect: 'if_required'
    }).subscribe({
      next: ({ paymentIntent, error }) => {
        if (error) {
          this.paying.set(false);
          if (error.code === 'payment_intent_unexpected_state' &&
            error.payment_intent?.status === 'succeeded') {
            this.paying.set(false);
            this.shoppingCartState.deleteClientSecret();
            this.router.navigate(['/success']);
            return;
          }
          console.error('Error real:', error.message);
        } else if (paymentIntent?.status === 'succeeded') {
          this.paying.set(false);
          this.shoppingCartState.deleteClientSecret();
          this.router.navigate(['/success']);
        }
      }
    });
  }

  goToCart() {
    this.router.navigate(['/cart']);
  }

  goToShipping() {
    this.router.navigate(['/shipping']);
  }
}


