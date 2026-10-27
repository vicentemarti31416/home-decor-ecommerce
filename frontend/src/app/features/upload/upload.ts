import { HttpClient } from '@angular/common/http';
import { Component, inject } from '@angular/core';
import { Photo } from '../../shared/models/Photo';
import { Product } from '../../shared/models/Product';
import { FormArray, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { PhotoStateService } from '../../core/services/photo-state-service';

@Component({
  selector: 'app-upload',
  imports: [
    ReactiveFormsModule
  ],
  templateUrl: './upload.html',
  styleUrl: './upload.css',
})
export class Upload {
  httpClient = inject(HttpClient);
  formBuilder = inject(FormBuilder);
  photoState = inject(PhotoStateService);

  productForm: FormGroup = this.initForm();
  product: Product;
  photos = this.photoState.photos;

  /**
   * Inicia el formulario con un UUID pseudoaleatorio como referencia del producto
   */
  initForm(): FormGroup {
    return this.formBuilder.group({
      name: ['', [Validators.required]],
      description: [''],
      price: ['', [Validators.required]],
      category: ['', [Validators.required]],
      reference: [crypto.randomUUID()],
      photos: this.formBuilder.array([]),
    });
  }

  /**
   * Cada vez que se selecciona una nueva imagen se lanza un evento que se recoge para obtener dicha imagen y añadirla
   */
  onFileChange(event: any) {
    this.addPhoto(event.target.files[0]);
    event.target.value = null; // Borra el nombre de la imagen en input para seleccionar los archivos
  }

  /**
   * Aquí se añade la nueva foto seleccionada por el usuario en el input file
   * Para ello, lo que se hace es añadir dicha imagen a un FormData y enviarlo al backend
   * Allí se subirá a Cloudinary, que devolverá un publicId y la url donde se podrá acceder a la imagen
   * Estos dos datos se guardarán en un objeto Photo que se añadirá al state y al array photos del formBuilder
   * Se hace en cada foto porque subirlas todas a la vez añade una latencia demasiado grande.
   * Así, cada foto se sube inmediatamente y el usuario no nota la espera porque está buscando más fotos que añadir
   * y luego sólo hay que subir un array de objetos Photo que apenas contienen dos string
   */
  addPhoto(file: File) {
    const formData = new FormData();
    formData.append('file', file);
    this.httpClient.post<Photo>('/api/products/upload', formData).subscribe((response) => {
      this.photoState.addPhoto(response);
      (this.productForm.get('photos') as FormArray).push(this.formBuilder.control(response));
    });
  }

  /**
   * Elimina una imagen que se había seleccionado previamente.
   * Para ello, envía el objeto Photo, que contiene el publicId, al backend para eliminar la imagen de Cloudinary
   * Después se elimina la imagen del state y del array del formulario dinámico
   */
  deletePhoto(photo: Photo) {
    this.httpClient.delete<void>(`/api/products/${photo.publicId}`).subscribe(() => {
      this.photoState.deletePhoto(photo);
      const photoFormArray = this.productForm.get('photos') as FormArray;
      const index = photoFormArray.value.findIndex((photoArray) => photoArray.publicId === photo.publicId);
      if (index !== -1) photoFormArray.removeAt(index);
    });
  }

  /**
   * Si el formulario dinámico es válido, envía su contenido al backend para que guarde tanto los datos del producto
   * como los de las imágenes, es decir, cada publicId y cada url. Esto es mucho más rápido que tratar de subir todas
   * las imágenes de una cuando se trata de guardar el producto.
   * Después se asigna el producto guardado por si se hacen cambios detectar que no es un save sino un update
   */
  saveProduct() {
    if (this.productForm.valid) {
      this.httpClient.post<Product>('/api/products', this.productForm.value).subscribe(response => {
        this.product = response;
      });
    }
  }

  /**
   * Actualiza un producto que acabamos de subir
   */
  updateProduct() {
    if (this.productForm.valid) {
      this.httpClient.put<Product>(`/api/products/${this.product.id}`, this.productForm.value).subscribe(response => {
        this.product = response;
      });
    }
  }
}
