import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { Product } from '../../shared/models/Product';
import { ProductStateService } from '../../core/services/product-state-service';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { Order } from '../../shared/models/Order';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CurrencyPipe, DecimalPipe } from '@angular/common';
import { OrderStateService } from '../../core/services/order-state-service';
import { Item } from '../../shared/models/Item';
import { Customer } from '../../shared/models/Customer';
import { CustomerStateService } from '../../core/services/customer-state-service';
import { catchError, Observable, of, tap } from 'rxjs';
import { Chat } from '../../layout/chat/chat';
import { UpbuttonComponent } from '../../layout/upbutton/upbutton.component';

@Component({
  selector: 'app-detail',
  imports: [
    ReactiveFormsModule,
    CurrencyPipe,
    DecimalPipe,
    Chat,
    UpbuttonComponent
],
  templateUrl: './detail.html',
  styleUrl: './detail.css',
})
export class Detail implements OnInit {

  productState = inject(ProductStateService);
  route = inject(ActivatedRoute);
  httpClient = inject(HttpClient);
  formBuilder = inject(FormBuilder)
  shoppingCartState = inject(OrderStateService);
  customerState = inject(CustomerStateService);
  router = inject(Router);

  shoppingCart = this.shoppingCartState.shoppingCart;
  product = this.productState.product;
  photoSelected: string;
  productCached = this.productState.product;
  itemForm: FormGroup = this.initForm();
  isAddProductToast = signal<boolean>(false);
  isFavoriteToast = signal<boolean>(false);
  isMaxItems = signal<boolean>(false);
  isNotEnough = signal<boolean>(false);
  customer = this.customerState.customer;
  isFavorite = signal<boolean>(false);

  /**
   * Se usa computed porque stars deriva de ratings, no es un estado propio
   * La doble interrogación es el nullish coalescing operator (operador de fusión nula)
   * si this.product()?.rating no es null o undefined usa ese valor, sino usa 0
   * Evita que de error al pintar las estrellas si el producto no tiene rating
   * En caso de no tenerlo el valor será 0 y todas las estrellas irán vacías
   * El computed sirve para dibujar cada estrella, bien sea completa (100%) o sólo de forma parcial (50%, 40%, 30%...)
   * El rating es un double (Ej: 4.5)
   * En cada iteración comprueba si cada índice del array es un número mayor que uno (Ej: 4.5 - index 1 = 3.5)
   * Si lo es, todas esas estrellas estarán pintadas al 100%
   * Si no lo es, sólo pintarán el porcentaje real (4.5 - 4 = 0.5), es este caso sería un 50%
   * El resto, si los hay, serán un 0%, osea, las estrellas se verán vacías
   */
  stars = computed(() => {
    const rating = this.product()?.rating ?? 0;
    return Array.from({ length: 5 }, (_, index) =>
      (rating - index) >= 1
        ? 100
        : rating - index >= 0
          ? (rating - index) * 100
          : 0
    )
  })

  /**
   * Comprueba, mediante el id de la url, si el producto ya existe en el estado para no tener que 
   * buscarlo de nuevo a la base de datos.
   * En caso de que no sea el mismo, hace la petición a la base de datos y lo guarda en el estado.
   * Posteriormente comprueba si está añadido o no para mostrar el icono de corazón lleno o vacío
   */
  ngOnInit(): void {
    const id: number = Number(this.route.snapshot.paramMap.get('id'));
    if (this.product()?.id === id) {
      this.photoSelected = this.product()?.photos[0]?.url;
      this.isFavorite.set(this.customer()?.favoriteIds?.some(id => id === this.product()?.id));
    } else {
      this.httpClient.get<Product>(`/api/products/${id}`).subscribe(response => {
        this.productState.setProduct(response);
        this.photoSelected = this.product()?.photos[0]?.url;
        this.isFavorite.set(this.customer()?.favoriteIds?.some(id => id === this.product()?.id));
      });
    }
  }

  initForm(): FormGroup {
    return this.formBuilder.group({
      quantity: ['1', Validators.max(5)] // Muestra 1 por defecto en el selector del número de artículos del producto
    })
  }

  /**
   * Sirve para actualizar la cantidad en el selector del número de artículos de un producto que se desea comprar
   * Hace casting a Number porque, al venir de la vista, viene como string
   * También comprueba que el valor seleccionado no exceda del máximo de artículos por pedido, que en este caso es 5
   */
  updateQuantity(quantity: number) {
    const value: number = Number(this.itemForm.get('quantity').value) + quantity;
    if (value <= this.product().stock) this.itemForm.get('quantity').setValue(value <= 1 ? 1 : value >= 5 ? 5 : value);
  }

  /**
   * Añade nuevos artículos al carrito.
   * Primero comprueba que el usuario esté autenticado y que haya un carrito, si no lo hay lo crea.
   * Después busca si el artículo ya está en el carrito, si no lo está lo añade.
   * Si está, actualiza la cantidad de artículos hasta un límite de 5 y muestra el toast.
   * Si no está lo añade, setea la cantidad, actualiza el state con el nuevo artículo añadido y muestra el toast.
   * Posteriormente actualiza el carrito en el backend
   */
  addItemToCart(product: Product) {

    this.checkAuthentication();

    /**
     * Emite un observable con el Order del carrito si ya exite o con el recién creado
     * El problema que soluciona es que la llamada para crear un nuevo carrito es asíncrona
     * mientras que la asignación de un carrito existente se hace al instante
     * Usando un observable espera la respuesta para luego usar el Order para añadir el artículo
     */ 
    const cart$: Observable<Order> = this.shoppingCart()
      ? of(this.shoppingCart())
      : this.httpClient.get<Order>('/api/orders/status/CART').pipe(tap(response => this.shoppingCartState.setShoppingCart(response)));

    cart$.subscribe(() => {
      const itemFound: Item = this.shoppingCart()?.items.find(item => item.productId === product.id)
      if (itemFound) {
        const totalItems: number = Number(this.itemForm.get('quantity').value) + Number(itemFound.quantity);
        if (totalItems <= this.product().stock) {
          this.shoppingCart()?.items
            .filter(item => item.productId === product.id)
            .map(item => {
              const total = Number(item.quantity) + Number(this.itemForm.value.quantity);
              if (total > 5) {
                if (Number(item.quantity) < 5) this.showAddProductToast();
                this.isMaxItems.set(true);
              } else {
                this.isMaxItems.set(false);
                this.showAddProductToast();
              }
              const items: Item[] = this.shoppingCart().items.map(item => item.productId === product.id
                ? { ...item, quantity: total >= 5 ? 5 : total } : item);
              this.shoppingCartState.updateItems(items);
            }
            );
        } else {
          this.isNotEnough.set(true)
        }
      } else {
        const item: Item = {
          id: null,
          productId: product.id,
          quantity: this.itemForm.value.quantity
        };
        this.shoppingCartState?.addItem(item);
        this.showAddProductToast();
      }
      this.httpClient.put<Order>('/api/orders/update-cart', this.shoppingCart()).subscribe();
    });
  }

  /**
   * Sirve para seleccionar una foto del producto de las miniaturas para mostrarla como imagen principal de ese producto 
   */
  selectPhoto(url: string) {
    this.photoSelected = url;
  }

  /**
   * Muestra el toast cuando el usuario añade un producto a su carrito de compra
   */
  showAddProductToast() {
    this.isAddProductToast() ? this.isAddProductToast.set(false) : this.isAddProductToast.set(true);
    if (this.isAddProductToast()) {
      setTimeout(() => {
        this.isAddProductToast.set(false);
      }, 5000);
    }
  }

  /**
   * Muestra el toast cuando el usuario añade un producto a su lista de favoritos
   */
  showFavoriteToast() {
    this.isFavoriteToast() ? this.isFavoriteToast.set(false) : this.isFavoriteToast.set(true);
    if (this.isFavoriteToast()) {
      setTimeout(() => {
        this.isFavoriteToast.set(false);
      }, 5000);
    }
  }

  /**
   * Sirve para cerrar el toast que muestra que no hay stock suficiente de ese producto para la cantidad que ha pedido el usuario
   */
  showNotEnough() {
    this.isNotEnough() ? this.isNotEnough.set(false) : this.isNotEnough.set(true);
  }

  /**
   * Añade el producto a la lista de favoritos para ese usuario
   * El usuario lo obtiene en el backend mediante el token jwt
   */
  addFavorite() {
    this.checkAuthentication();
    this.httpClient.post<Customer>(`/api/users/favorite/${this.product()?.id}`, {})
      .subscribe(response => {
        this.isFavorite.set(true);
        this.customerState.setCustomer(response);
        this.showFavoriteToast();
      });
  }

  /**
   * Elimina el producto de la lista de favoritos para ese usuario
   * El usuario lo obtiene en el backend mediante el token jwt
   */
  deleteFavorite() {
    this.checkAuthentication();
    this.httpClient.delete<Customer>(`/api/users/favorite/${this.product()?.id}`, {})
      .subscribe(response => {
        this.isFavorite.set(false);
        this.customerState.setCustomer(response);
      });
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

  goToReviews() {
    if (this.product()?.statistics[0] > 0) {
      this.router.navigate(['/reviews'])
    }
  }

}
