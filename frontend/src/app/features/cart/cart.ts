import { Component, computed, effect, inject, OnInit, signal } from '@angular/core';
import { OrderStateService } from '../../core/services/order-state-service';
import { Product } from '../../shared/models/Product';
import { HttpClient } from '@angular/common/http';
import { CurrencyPipe } from '@angular/common';
import { CartProduct } from '../../shared/models/CartProduct';
import { Item } from '../../shared/models/Item';
import { Router } from "@angular/router";
import { Order } from '../../shared/models/Order';
import { Customer } from '../../shared/models/Customer';
import { CustomerStateService } from '../../core/services/customer-state-service';
import { catchError, of, tap } from 'rxjs';
import { Chat } from '../../layout/chat/chat';

@Component({
  selector: 'app-cart',
  imports: [
    CurrencyPipe,
    Chat
  ],
  templateUrl: './cart.html',
  styleUrl: './cart.css',
})
export class Cart implements OnInit {

  shoppingCartState = inject(OrderStateService);
  customerState = inject(CustomerStateService);
  httpClient = inject(HttpClient);
  router = inject(Router);

  shoppingCart = this.shoppingCartState.shoppingCart;
  productsCart = signal<CartProduct[]>([]);
  total = computed(() => this.productsCart().reduce((sum, product) => sum + product.price * product.quantity, 0));
  isNotEnough = signal<boolean>(false);
  productIdWithoutStock: number;
  customer = this.customerState.customer;

  /**
   * Comprueba si el usuario está autenticado
   * Después obtiene el carrito de la base de datos y lo añade al estado
   * El backend comprobará si hay carrito y lo devolverá
   * En caso de no haberlo, lo creará y lo devolverá
   */
  ngOnInit(): void {
    this.checkAuthentication();
    if (!this.shoppingCart()) this.httpClient.get<Order>(`/api/orders/status/${'CART'}`).subscribe(response => {
      this.shoppingCartState.setShoppingCart(response);
    });
  }

  /**
   * Cada vez que el carrito cambia se comprueba en el backend que hay stock disponible y el precio real antes de añadirlo al carrito
   * También se trae la información de los productos para disponer de datos que no tienen los items como la imagen y demás
   */
  cartEffect = effect(() => {
    const ids: number[] = this.shoppingCart()?.items.map(item => item?.productId);
    if (ids?.length) {
      this.httpClient.post<Product[]>('/api/products/ids', ids).subscribe(response => {
        this.productsCart.set(response.map((product: Product) => {
          const item: Item = this.shoppingCart()?.items.find(item => item.productId === product.id);
          return { ...product, quantity: item.quantity ?? 1 }
        }))
      });
    } else {
      this.productsCart.set([]);
    }
  });

  /**
   * Actualiza la cantidad de artículos que se desean pedir de un producto, tanto en el frontend como en el backend,
   * por si el usuario deja la compra a medias, para que no se pierda la información
   * También comprueba que haya suficiente stock del producto y que no se pidan más de 5 artículos por pedido
   */
  updateQuantity(quantity: number, product: CartProduct) {
    const value = Number(product.quantity) + Number(quantity);
    if (value <= product.stock) {
      this.isNotEnough.set(false);
      const newQuantity = value <= 1 ? 1 : value >= 5 ? 5 : value;
      const items: Item[] = this.shoppingCart()?.items.map(item =>
        item.productId === product.id ? { ...item, quantity: newQuantity } : item);
      const updated = { ...this.shoppingCart(), items };
      this.httpClient.put<Order>('/api/orders/update-cart', updated).subscribe(response => this.shoppingCartState.setShoppingCart(response));
    } else {
      this.isNotEnough.set(true);
      this.productIdWithoutStock = product.id;
    }
  }

  /**
   * Elimina un producto del carrito de compra
   * Filtra los productos del carrito para eliminar el pasado como CartProduct, actualiza en carrito en updated y 
   * modifica la Order en el backend con un put
   */
  removeItem(cartProduct: CartProduct) {
    const items = this.shoppingCart()?.items.filter(item => item.productId !== cartProduct.id);
    const updated = { ...this.shoppingCart(), items };
    this.httpClient.put<Order>('/api/orders/update-cart', updated)
      .subscribe(response => this.shoppingCartState.setShoppingCart(response));
  }

  goToDetail(id: number) {
    this.router.navigate(['/detail', id]);
  }

  buy() {
    this.router.navigate(['/shipping']);
  }

  /**
   * Comprueba si el usuario está autenticado
   */
  checkAuthentication() {
    if (!this.customer()) this.httpClient.get<Customer>('/api/users/me').pipe(
      tap(response => this.customerState.setCustomer(response)),
      catchError(() => {
        window.location.href = '/oauth2/authorization/gateway';
        return of(false);
      })
    ).subscribe();
  }
}
