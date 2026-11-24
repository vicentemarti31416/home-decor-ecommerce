import { Injectable, signal } from '@angular/core';
import { Photo } from '../../shared/models/Photo';

@Injectable({
  providedIn: 'root',
})
export class PhotoStateService {

  private _photo = signal<Photo | null>(null);
  private _photos = signal<Photo[]>([]);

  photo = this._photo.asReadonly();
  photos = this._photos.asReadonly();

  addPhoto(photo: Photo) {
    this._photos.update(photos => photos ? [...photos, photo] : [photo]);
  }

  deletePhoto(photo: Photo) {
    this._photos.update(photos => photos ? photos.filter(photoFilter => photoFilter.publicId !== photo.publicId) : photos);
  }
}
