import { Injectable, signal } from '@angular/core';
import { Order } from '../../shared/models/Order';
import { Item } from '../../shared/models/Item';
import { OrderResponse } from '../../shared/models/OrderResponse';

@Injectable({
  providedIn: 'root',
})
export class OrderStateService {

  private _shoppingCart = signal<Order | null>(null);
  private _order = signal<Order | null>(null);
  private _orders = signal<Order[]>([]);
  private _clientSecret = signal<string | null>(null);

  shoppingCart = this._shoppingCart.asReadonly();
  order = this._order.asReadonly();
  orders = this._orders.asReadonly();
  clientSecret = this._clientSecret.asReadonly();

  /** 
   * Los dos interrogantes ?? son el operador nullish coalescing
   * Usa order.items, pero si es null o undefined, usa []
   */
  setShoppingCart(order: Order) {
    this._shoppingCart.set({ ...order, items: order.items ?? [] });
  }

  resetShoppingCart() {
    this._shoppingCart.set(null);
  }

  /**
   * Es quivalente a:
   * const cart = this._shoppingCart();
   * this._shoppingCart.set(cart ? { ...cart, items: [...cart.items, item] } : cart);
   */
  addItem(item: Item) {
    this._shoppingCart.update(cart => {
      if (!cart) return cart; // Si null o undefined devuelve el cart sin más y sale 
      return { ...cart, items: [...cart.items, item] } // Crea un nuevo cart con spread operator, también un nuevo array de items y añade el item
    })
  }

  updateItems(items: Item[]) {
    this._shoppingCart.update(cart => {
      return { ...cart, items: items } // Crea un nuevo cart para no modificar el actual. Añade los items que se le pasa
    })
  }

  setClientSecret(secret: string) {
    this._clientSecret.set(secret);
  }

  deleteClientSecret() {
    this._clientSecret.set(null);
  }
}
