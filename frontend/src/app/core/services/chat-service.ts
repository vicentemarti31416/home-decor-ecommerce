import { HttpClient } from '@angular/common/http';
import { effect, inject, Injectable, signal, untracked } from '@angular/core';
import { Stomp } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { Message } from '../../shared/models/Message';
import { Chat } from '../../shared/models/Chat';
import { CustomerStateService } from './customer-state-service';


@Injectable({
  providedIn: 'root',
})
export class ChatService {

  private url = 'http://127.0.0.1:8090';
  chatId = signal(0);
  private stompClient: any;
  messages = signal<Message[]>([]);
  connected = signal<boolean>(false);
  subscription: any;
  private httpClient = inject(HttpClient);
  customerState = inject(CustomerStateService);
  customer = this.customerState.customer;

  /**
   * Utiliza un effect para tratar de obtener el chat a fin de que el objeto customer esté totalmente disponible  
   * porque sino se saltaría la validación realizaría la llamada a getChat() y provocaría un error
   */
  constructor() {
    this.initConnenctionSocket();
    effect(() => {
      const rol = this.customer()?.roles[0];
      if (this.connected() && rol && rol !== 'ROLE_ADMIN' && this.chatId() === 0) {
        untracked(() => this.getChat('support')); // EVita que cualquier signal que lea esta función vuelva a disparar el effect
      }
    });
  }

  /**
   * Sirve para realizar la conexión websocket y para descargar los mensajes del chat principal público.
   * (1) 
   * - Petición de información (HTTP normal): GET /ws/info (El servidor responde con un JSON indicando si admite WebSocket, si necesita cookies, etc.)
   * - Elección de transporte: Con esa respuesta, SockJS genera un id de servidor y otro de sesión y prueba el mejor transporte disponible, empezando 
   * por WebSocket en /ws/{server-id}/{session-id}/websocket.
   * Si falla (proxy, red, navegador), recurre a xhr-streaming o xhr-polling sobre HTTP normal. Por eso existe SockJS.
   * - Handshake HTTP con Upgrade: La petición al endpoint /websocket es un GET con las cabeceras:
   * Connection: Upgrade
   * Upgrade: websocket
   * Sec-WebSocket-Key (valor aleatorio en base64)
   * Sec-WebSocket-Version: 13
   * - Aquí es donde actúa la seguridad: es una petición HTTP más, así que pasa por los filtros de Spring Security (y en la tienda, por el Gateway, que con 
   * TokenRelay le añade el Authorization: Bearer a partir de la cookie SESSION). Si no está autenticada, se rechaza aquí y nunca llega a ser WebSocket.
   * - Respuesta 101 Switching Protocols. El servidor responde 101 con Upgrade: websocket y Sec-WebSocket-Accept (hash calculado a partir de la key 
   * del cliente, para confirmar que entendió el handshake). A partir de este momento la conexión TCP deja de hablar HTTP y pasa a enviar frames
   * WebSocket en ambos sentidos. En DevTools la verás con estado 101 y la pestaña Messages.
   * (2)
   * Envuelve el socket con el cliente STOMP
   * (3)
   * Envía el primer frame STOMP: CONNECT con versión aceptada y heartbeats. El servidor responde CONNECTED
   */
  initConnenctionSocket() {
    const urlSocket = 'http://127.0.0.1:8090/ws';
    const socket = new SockJS(urlSocket); // (1) 
    this.stompClient = Stomp.over(socket); // (2)
    this.stompClient.connect({}, () => { // (3)
      this.connected.set(true);
    });
  }

  /**
   * Averigua si existe un chatId entre esos dos usuarios. Si lo hay lo devuelve y sino, lo crea y lo devuelve.
   * Después se une al chat devuelto
   */
  getChat(receiver: string) {
    this.httpClient.get<Chat>(`${this.url}/api/chat/${receiver}`).subscribe((chat) => {
      this.chatId.set(chat.id); // Obtiene el chat
      this.joinChat(this.chatId());
    });
  }

  /**
   * Sirve para unirse a un chat, indicando su id que se obtiene de la lista que hay en la vista, mediante el botón correspondiente.
   * Comprueba primero si ya nos hemos unido a otro chat mediante currentSubscription, en caso afirmativo cancela la suscripción para dejar 
   * de estar escuchando nuevos mensajes y vacía todos los mensajes del array que contiene los de ese chat para alojar a los del nuevo.
   * Posteriormente se suscribe al nuevo chat, quedando el listener a la escucha de los mensajes que vayan llegando y obtiene los mensajes,   
   * los guarda en el signal messages con un update y los ordena por fecha de forma que primero, es decir, arriba, se vean los más nuevos.
   * La lista de mensajes será accesible para el componente chat mediante un computed.
   */
  joinChat(chatId: number) {
    if (this.subscription) {
      this.subscription.unsubscribe();
      this.messages.set([]);
    }
    this.subscription = this.stompClient.subscribe(`/chat/${chatId}`, (msg: any) => {
      const message: Message = JSON.parse(msg.body);
      this.messages.update((list) =>
        [...list, message].sort(
          (a, b) => new Date(a.created).getTime() - new Date(b.created).getTime(),
        ),
      );
    });
    this.getMessages(chatId); // Obtiene los mensajes
  }

  /**
   * Obtiene todos los mensajes de un chat mediante su id
   */
  getMessages(chatId: number): void {
    this.httpClient
      .get<Message[]>(`${this.url}/api/messages/${chatId}`)
      .subscribe((messages) => this.messages.set(messages));
  }

  /**
   * Envía un mensaje al chat que se visualizará cuando lo reciba de vuelta el listener al que nos hemos suscrito
   */
  sendMessage(chatId: number, message: Message) {
    if (this.connected()) this.stompClient.send(`/app/chat/${chatId}`, {}, JSON.stringify(message));
  }

}
