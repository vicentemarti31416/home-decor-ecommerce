import { Injectable, signal } from '@angular/core';
import { Product } from '../../shared/models/Product';

@Injectable({
  providedIn: 'root',
})
export class ProductStateService {

  private _product = signal<Product | null>(null);
  private _products = signal<Product[]>([]);

  product = this._product.asReadonly();
  products = this._products.asReadonly();

  setProduct(product: Product) {
    this._product.set(product);
  }

  setProducts(products: Product[]) {
    this._products.set(products);
  }
}
