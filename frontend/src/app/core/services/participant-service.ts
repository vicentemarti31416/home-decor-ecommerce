import { HttpClient } from '@angular/common/http';
import { Injectable, signal } from '@angular/core';
import { Participant } from '../../shared/models/Participant';

@Injectable({
  providedIn: 'root',
})
export class ParticipantService {

  participant = signal<Participant | null>(null);
  participants = signal<Participant[]>([]);

  constructor(private httpClient: HttpClient) { }

  private url: string = 'http://127.0.0.1:8090/api';


  // TODO: Sólo el ADMIN debería de recibir todos los users, el resto de los usuarios sólo se comunicarán con el ADMIN
  public getParticipants() {
    this.httpClient.get<Participant[]>(`${this.url}/participants`).subscribe(participants => {
      this.participants.set(participants);
    });
  }
}
