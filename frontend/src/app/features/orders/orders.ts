import { Component, effect, inject, signal, viewChild } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { OrderResponse } from '../../shared/models/OrderResponse';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { ProductResponse } from '../../shared/models/ProductResponse';
import { OrderStatus } from '../../shared/enums/OrderStatus';
import { UpbuttonComponent } from '../../layout/upbutton/upbutton.component';
import { Chat } from '../../layout/chat/chat';
import { Dateselector } from '../../layout/dateselector/dateselector';
import { RangeSearch } from '../../shared/models/RangeSearch';
import { Page } from '../../shared/models/Page';

@Component({
  selector: 'app-orders',
  imports: [
    Dateselector,
    UpbuttonComponent,
    CommonModule,
    Chat
  ],
  templateUrl: './orders.html',
  styleUrl: './orders.css',
})
export class Orders {

  dateSelector = viewChild.required(Dateselector);

  httpClient = inject(HttpClient);
  router = inject(Router);

  orders = signal<OrderResponse[] | null>(null);
  openIndex = signal<number | null>(0);

  OrderStatus = OrderStatus;

  numberOfPage = signal<number | null>(0);
  totalPages = signal<number | null>(null);
  range = signal<number[]>([]);
  totalElements = signal<number | null>(null);
  rangeSearch: RangeSearch = {
    start: '',
    end: ''
  }

  constructor() {
    effect(() => {
      const [start, end] = this.dateSelector().dates();
      this.rangeSearch.start = start;
      this.rangeSearch.end = end;
      let params = new HttpParams()
        .set('page', 0)
        .set('size', 5)
        .set('sort', 'updatedDate,desc');
      if (start) params = params.set('start', start);
      if (end) params = params.set('end', end);
      this.httpClient.get<Page<OrderResponse>>('/api/orders/order-response', { params }).subscribe(response => {
        this.assignData(response);
        this.calculatePageRange();
        this.orders.set(response.content);
      });
    })
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

  /** 
   * Sirve para buscar los pedidos según el número de página de los resultados del paginador
   */
  findOrders(page: number, size: number) {
    const pageCalculated = this.calculateLimits(page);
    let params = new HttpParams()
      .set('page', pageCalculated)
      .set('size', size)
      .set('sort', 'createdDate,desc');
    if (this.rangeSearch.start) params = params.set('start', this.rangeSearch.start)
    if (this.rangeSearch.end) params = params.set('end', this.rangeSearch.end);
    this.httpClient.get<Page<OrderResponse>>('/api/orders/order-response', { params }).subscribe(response => {
      this.orders.set(response.content);
      this.assignData(response);
      this.calculatePageRange();
    })
  }

  /** 
   * Setea en el signal numberOfPage el número de página actual de las reseñas
   * Math.max devuelve el valor máximo, y Math.min el mínimo, es decir, en caso de que se indique un número menor que 0
   * o mayor que el número máximo de páginas siempre devolverá un número entre o y el número máximo de páginas
   * para no pedir una página que no existe
   */
  calculateLimits(page: number): number {
    page = Math.max(0, Math.min(page, this.totalPages() - 1));
    this.numberOfPage.set(page);
    return page;
  }

  assignData(response: Page<OrderResponse>) {
    this.numberOfPage.set(response.number);
    this.totalPages.set(response.totalPages);
    this.totalElements.set(response.totalElements);
  }

  /**
   * Calcula los números que se mostrarán en los botones del paginador para mostrar los números de página 
   * El primer if es por si hay menos páginas que 5, que es el máximo de botones que se mostrarán.
   * Evita que se muestren botones de páginas que no existen
   * El primer else if es para cuando se han seleccionado las primeras páginas
   * El segundo else if es para cuando se ha seleccionado una de las últimas páginas
   * El último, el else, es para cuando se haya elegido una página intermedia
   */
  calculatePageRange() {
    if (this.totalPages() <= 5) {
      this.range.set(Array.from({ length: this.totalPages() }, (_, i) => i + 1));
    } else if (
      this.numberOfPage() == 0
      || this.numberOfPage() == 1
      || this.numberOfPage() == 2) {
      this.range.set([1, 2, 3, 4, 5]);
    } else if (
      this.numberOfPage() == this.totalPages() - 1
      || this.numberOfPage() == this.totalPages() - 2
      || this.numberOfPage() == this.totalPages() - 3
    ) {
      this.range.set([
        this.totalPages() - 4,
        this.totalPages() - 3,
        this.totalPages() - 2,
        this.totalPages() - 1,
        this.totalPages(),
      ]);
    } else {
      this.range.set([
        this.numberOfPage() - 1,
        this.numberOfPage(),
        this.numberOfPage() + 1,
        this.numberOfPage() + 2,
        this.numberOfPage() + 3,
      ]);
    }
  }

  goToDetail(id: number) {
    this.router.navigate(['/detail', id]);
  }
}




