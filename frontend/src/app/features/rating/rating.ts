import { Component, inject, OnInit, signal } from '@angular/core';
import { Router } from '@angular/router';
import { OrderResponse } from '../../shared/models/OrderResponse';
import { RatingRequest } from '../../shared/models/RatingRequest';
import { CurrencyPipe, DatePipe } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ProductResponse } from '../../shared/models/ProductResponse';
import { HttpClient } from '@angular/common/http';
import { ProductStateService } from '../../core/services/product-state-service';
import { RatingResponse } from '../../shared/models/RatingResponse';
import { Toast } from '../../shared/models/Toast';
import { OrderStatus } from '../../shared/enums/OrderStatus';
import { Chat } from '../../layout/chat/chat';

@Component({
  selector: 'app-rating',
  imports: [
    DatePipe,
    CurrencyPipe,
    ReactiveFormsModule,
    Chat
  ],
  templateUrl: './rating.html',
  styleUrl: './rating.css',
})
export class Rating implements OnInit {

  router = inject(Router);
  formBuilder = inject(FormBuilder);
  httpClient = inject(HttpClient);
  productState = inject(ProductStateService);
  OrderStatus = OrderStatus;

  order = signal<OrderResponse | null>(null);
  product = signal<ProductResponse | null>(null);

  ratingForm: FormGroup = this.initForm();

  stars = signal<string[]>(Array(5).fill('transparent'));
  rating = signal<number>(0);
  review = signal<RatingResponse | null>(null);
  isAddReview = signal<boolean>(false);
  submitted = signal<boolean>(false);
  isSending = signal<boolean>(false);
  toastTitle = signal<string>('');
  toastBody = signal<string>('');

  toastSuccess: Toast = {
    toastTitle: 'Reseña añadida',
    toastBody: 'Se ha añadido la nueva reseña',
    toastRoute: '/'
  }

  toastError: Toast = {
    toastTitle: 'No se ha podido añadir',
    toastBody: 'Inténtelo de nuevo más tarde',
    toastRoute: '/rating'
  }

  /**
   * Recibe la información mediante el state de la petición 
   * Obtiene la reseña del producto del usuario actual obtenido del jwt en el backend
   * Define los puntos que el producto tiene de valoración
   * En caso de que haya una valoración previa fija el comentario anterior y las estrellas 
   * Define cómo se verán las estrellas según los puntos de valoración que tiene el producto
   */
  ngOnInit(): void {
    this.order.set(history.state.order);
    this.product.set(history.state.product);
    this.httpClient.get<RatingResponse>(`/api/ratings/${this.product().id}`).subscribe(response => {
      this.review.set(response);
      this.rating.set(this.review().stars);
      this.ratingForm.patchValue({ comment: response.comment, stars: response.stars });
      this.paintStars(this.review().stars);
    });
  }

  /**
   * Si el usuario había dejado un comentario previamente se muestra dicho comentario en el área de texto.
   * Para ello utiliza el patchValue del ngOnInit.
   * Lo hace de esta manera porque los valores vienen de una respuesta http y podrían no estar disponibles 
   * cuando se crea el formulario. Así se asegura de que siempre van a aparecer aunque la llamada para
   * obtener dichos datos se hagan de forma asíncrona
   */ 

  initForm(): FormGroup {
    return this.formBuilder.group({
      comment: [''],
      stars: ['', [Validators.required, Validators.min(1), Validators.max(5)]]
    })
  }

  /**
   * Pinta las estrellas según la valoración que le hemos dado (5, 4, 3...)
   * Para ello setea un array en el que va contando las estrellas de valoración que le hemos dado
   * y en cada índice del array va poniendo como valor el color con el que se rellenarán las estrellas
   * o el valor transparent en aquellos índices en los que la valoración no llegue
   */
  paintStars(count: number) {
    this.stars.set(Array.from({ length: 5 }, (_, i) => i < count ? '#645FEC' : 'transparent'));
  }

  /**
   * Pinta temporalmente las estrellas cuando pasa por encima de ellas el ratón en la vista
   */
  fillColor(index: number) {
    this.paintStars(index + 1);
  }

  /**
   * Vacía o limpia de color las estrellas una vez que el ratón deja de estar por encima de ellas en la vista
   */
  emptyColor() {
    this.paintStars(this.rating());
  }

  /**
   * Fija la valoración de las estrellas cuando hacemos click en una de ellas en la vista
   * También llama a paintStars para que se queden pintadas fijas para que visualmente el 
   * usuario vea que han quedado seleccionadas
   */
  setRating(index: number) {
    this.ratingForm.get('stars').setValue(index + 1);
    this.rating.set(index + 1);
    this.paintStars(index + 1);
  }

  /**
   * Envía la valoración al backend para que quede guardada
   * Primero comprueba que el formulario sea válido, para no enviar una valoración sin estrellas
   * También comprueba que no se esté enviando ya la valoración para evitar que el usuario pueda hacer 
   * click varias veces y haya varios comentarios iguales que acaben guardados en la base de datos
   * Además, comprueba si ya había una valoración previa para diferenciar entre hacer put o post
   * Posteriormente muestra un toast de error o success según la valoracion se haya hecho correctamente
   */
  sendValoration() {
    this.submitted.set(true);
    if (this.ratingForm.valid && !this.isSending()) {
      this.isSending.set(true);
      const ratingRequest: RatingRequest = {
        stars: this.rating(),
        comment: this.ratingForm.get('comment').value,
        productId: this.product().id
      }
      if (this.review()?.id) {
        this.httpClient.put<any>(`/api/products/rating/${this.review().id}`, ratingRequest).subscribe({
          next: response => {
            this.productState.setProduct(response);
            this.showAddProductToast(this.toastSuccess);
          },
          error: error => {
            this.isSending.set(false);
            this.showAddProductToast(this.toastError);
          }
        });
      } else {
        this.httpClient.post<any>('/api/products/rating', ratingRequest).subscribe({
          next: response => {
            this.productState.setProduct(response);
            this.showAddProductToast(this.toastSuccess);
          },
          error: error => {
            this.isSending.set(false);
            this.showAddProductToast(this.toastError);
          }
        });
      }
    }
  }

  goToDetail(id: number) {
    this.router.navigate(['/detail', id]);
  }

  /**
   * Muestra el toast cuando se envía la reseña. 
   * Utiliza un Toast según el envío se realice con éxito o dé algún error
   */
  showAddProductToast(toast: Toast) {
    this.isAddReview() ? this.isAddReview.set(false) : this.isAddReview.set(true);
    if (this.isAddReview()) {
      this.toastTitle.set(toast.toastTitle);
      this.toastBody.set(toast.toastBody);
      setTimeout(() => {
        this.isAddReview.set(false);
        this.router.navigate([toast.toastRoute]);
      }, 5000);
    }
  }
}