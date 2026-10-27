import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { RatingResponse } from '../../shared/models/RatingResponse';
import { CurrencyPipe, DecimalPipe, NgClass } from '@angular/common';
import { ProductStateService } from '../../core/services/product-state-service';
import { Chat } from '../../layout/chat/chat';

@Component({
  selector: 'app-reviews',
  imports: [
    CurrencyPipe,
    DecimalPipe,
    NgClass,
    Chat
  ],
  templateUrl: './reviews.html',
  styleUrl: './reviews.css',
})
export class Reviews implements OnInit {

  productState = inject(ProductStateService);
  route = inject(ActivatedRoute);
  router = inject(Router);
  httpClient = inject(HttpClient);

  product = this.productState.product;;
  ratingResponses = signal<RatingResponse[]>([]);
  selectedStars = signal<number | null>(0);

  numberOfPage = signal<number | null>(0);
  totalPages = signal<number | null>(null);
  range = signal<number[]>([]);
  totalElements: number;
  min: number;
  max: number;

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
   * Calcula los porcentajes que se verán en cada una de las barras que representan el número de estrellas de las reseñas (5, 4, 3...)
   * Se crea un nuevo array a través del array statistics, que contiene las valoraciones totales recibidas por el producto statistics[0] 
   * y la cantidad de valorciones de cada estrella recibidas, ordenadas en el array por su índice 
   * statistics[1] (1 estrella), statistics[2] (2 strellas) y así sucesivamente
   * Después calcula y guarda en el array percentages el tanto por ciento de cada estrella calculado según el total y el número 
   * de valoraciones que ha recibido cada estrella
   * stats[i + 1] es para evitar añadir el total en el array, ya que se usa en la vista para mostrar las barras y, al ser stats[0] el que
   * muestra el total de votos de todas las valoraciones no quedaría bien porque aparecería una barra más en la vista con esa información
   */
  percentages = computed(() => {
    const stats: number[] = this.product()?.statistics;
    const total: number = stats[0];
    return Array.from({ length: 5 }, (_, i) => (stats[i + 1] / total) * 100).reverse();
  });

  /**
   * Compone el array reviewsFiltered, que guarda todas las reseñas que se van a mostra en la vista
   * También compone y guarda el array starColors, donde las estrellas las cinco estrellas de cada reseña 
   * irán pintadas o vacías en función de la valoración que haya recibido cada reseña
   */
  reviewsFiltered = computed(() => {
    return this.ratingResponses()
      .map(review => ({
        ...review,
        starColors: Array.from(
          { length: 5 },
          (_, i) => i < review.stars
            ? '#645FEC'
            : 'transparent')
      }
      ))
  });

  /**
   * Recibe el product mediante el state de la vista anterior (detail) para no tener que hacer la búsqueda otra vez a la base de datos
   */
  ngOnInit(): void {
    this.getAllReviews(0, 5);
  }

  /**
   * Sirve para buscar únicamente las reseñas de una determinada cantidad de estrellas (5, 4, 3...)
   * Si se clica dos veces sobre la misma barra de estadísticas se resetea a cero y la búsqueda pasa a ser de todo tipo de stars 
   * En la primera linea de la función setea el signal selectedStars() para realizar la búsqueda filtrada o sin filtrar
   * En la segunda y última linea busca las reseñas de la primera página de los resultados ya sea con filtro o sin él
   */
  setSelectedStars(selectedStars: number) {
    selectedStars === this.selectedStars() ? this.selectedStars.set(0) : this.selectedStars.set(selectedStars);
    this.findAllReviewsPageable(0, 5);
  }

  /**
   * Realiza la búsqueda de reseñas filtradas o no, según el signal selectedStars() sea 0 o un número entre 1 y 5
   * También realiza la búsqueda según la página
   */
  findAllReviewsPageable(page: number, size: number) {
    page = this.calculateLimits(page);
    this.selectedStars() === 0
      ? this.getAllReviews(page, size)
      : this.getReviewsFilteredByStars(page, size);
  }

  /**
   * Obtiene las reseñas sin filtrar por número de estrellas y número de página
   */
  getAllReviews(page: number, size: number) {
    this.httpClient.get<any>(`/api/ratings/reviews/${this.product()?.id}?page=${page}&size=${size}`).subscribe(response => {
      this.assignData(response);
      this.calculateElementsRange();
      this.calculatePageRange();
    })
  }

  /**
   * Obtiene las reseñas filtradas por número de estrellas con selectedStars() y por número de página
   */
  getReviewsFilteredByStars(page: number, size: number) {
    this.httpClient.get<any>(`/api/ratings/reviews/${this.product()?.id}/${this.selectedStars()}?page=${page}&size=${size}`).subscribe(response => {
      this.assignData(response);
      this.calculateElementsRange();
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

  assignData(response) {
    this.ratingResponses.set(response.content);
    this.numberOfPage.set(response.number);
    this.totalPages.set(response.totalPages);
    this.totalElements = response.totalElements;
  }

  /**
   * Calcula los rangos del número de elementos mostrados en la tabla donde dice "Mostrando... elementos de..."
   * Ej: Si hay 10 elementos, estamos en la primera página y se muestran 5 elementos por página mostrará
   * "Mostrando 1-5 elementos de 10" (min, max, totalElements)
   */
  calculateElementsRange() {
    this.min = this.totalElements === 0 ? 0 : this.numberOfPage() * 5 + 1;
    (this.numberOfPage() + 1) * 5 > this.totalElements
      ? (this.max = this.totalElements)
      : (this.max = (this.numberOfPage() + 1) * 5);
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

  goToDetail(productId: number) {
    this.router.navigate(['/detail', productId]);
  }

}
