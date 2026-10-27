import { Component, OnInit, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { OrderResponse } from '../../shared/models/OrderResponse';
import { CurrencyPipe, DatePipe } from '@angular/common';
import { Router } from '@angular/router';
import { ProductResponse } from '../../shared/models/ProductResponse';
import { OrderStatus } from '../../shared/enums/OrderStatus';
import { UpbuttonComponent } from '../../layout/upbutton/upbutton.component';
import { Chat } from '../../layout/chat/chat';

@Component({
  selector: 'app-orders',
  imports: [
    UpbuttonComponent,
    DatePipe,
    Chat,
    CurrencyPipe
  ],
  templateUrl: './orders.html',
  styleUrl: './orders.css',
})
export class Orders implements OnInit {

  httpClient = inject(HttpClient);
  router = inject(Router);

  orders = signal<OrderResponse[]>([]);
  openIndex = signal<number | null>(0);

  OrderStatus = OrderStatus;

  ngOnInit(): void {
    this.httpClient.get<OrderResponse[]>('/api/orders/order-response').subscribe(response => {
      this.orders.set(response.sort((order1, order2) => new Date(order2.createdDate).getTime() - new Date(order1.createdDate).getTime()));
      if (this.orders()[0]?.status === 'CART' && this.orders()[0]?.products.length === 0) this.openIndex.set(1);
    });
  } 

  toggle(index: number) {
    this.openIndex.set(this.openIndex() === index ? null : index);
  }

  sendState(order: OrderResponse, product: ProductResponse) {
    this.router.navigate(['/rating'], {
      state: {
        order: order,
        product: product
      }
    });
  }

  goToDetail(id: number) {
    this.router.navigate(['/detail', id]);
  }
}




