import { AfterViewInit, Component, computed, effect, ElementRef, inject, OnInit, Signal, signal, ViewChild, WritableSignal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { initFlowbite } from 'flowbite';
import { CustomerStateService } from '../../core/services/customer-state-service';
import { ChatService } from '../../core/services/chat-service';
import { ParticipantService } from '../../core/services/participant-service';
import { MessageType } from '../../shared/models/MessageType';
import { Message } from '../../shared/models/Message';
import { Customer } from '../../shared/models/Customer';
import { Participant } from '../../shared/models/Participant';

@Component({
  selector: 'app-chat',
  imports: [CommonModule, FormsModule],
  templateUrl: './chat.html',
  styleUrl: './chat.css',
})
export class Chat implements OnInit, AfterViewInit {

  @ViewChild('chatContainer') chatContainer!: ElementRef;

  customerState = inject(CustomerStateService);
  customer: Signal<Customer> = this.customerState.customer;
  chatService = inject(ChatService);
  messages = computed(() => this.chatService.messages());
  participantService = inject(ParticipantService);
  participant: WritableSignal<Participant> = this.participantService.participant;
  participants: WritableSignal<Participant[]> = this.participantService.participants;

  sender = signal<string>('');
  receiver = signal<string>('');
  message = signal<string>('');
  chatId = computed(() => this.chatService.chatId());
  isChatClosed = signal<boolean>(true);

  /**
   * Registra un effect que mueve el scroll hacia abajo cada vez que llegan nuevos mensajes para que siempre se vean los últimos en llegar.
   * Utiliza un timeout para esperar a que la plantilla se haya actualizado con los nuevos mensajes antes de mover el scroll
   */
  // Valorar validar que sólo busque los mensajes si chatId es distinto que cero
  constructor() {
    effect(() => {
      const messages = this.chatService.messages();
      if (messages.length > 0) {
        setTimeout(() => this.scrollToBottom());
      }
    });
  }

  ngOnInit(): void {
    initFlowbite();
  }

  /**
   * Sirve para que move el scroll y que se vean los nuevos mensajes que van llegando
   */
  private scrollToBottom(): void {
    if (this.chatContainer) {
      const element = this.chatContainer.nativeElement;
      element.scrollTop = element.scrollHeight;
    };
  }

  ngAfterViewInit(): void {
    initFlowbite();
  }

  /**
   * El toggle no solo abre el chat sino que también lo busca en la base de datos y trae sus mensajes.
   * Evita añadir los mensajes al customer y solo los busca cuando realmente se usa el chat.
   * Por defecto busca comenzar el chat con el admin solicitando un chat genérico (support).
   */
  toggle() {
    this.isChatClosed.set(this.isChatClosed() ? false : true);
    if(this.customer()?.roles[0] !== 'ROLE_ADMIN') this.getChat('support');
  }

  /**
   * Obtiene un chat con sus mensajes o lo manda crear en caso de que no exista.
   * Si el customer es de tipo user la petición al backend siempre se hará, 
   * en este caso concreto el backend obtendrá el chat del customer con el admin por defecto.
   * En caso de ser el admin no se realizará la petición al backend hasta que no haya seleccionado un usuario concreto,
   * así se evita una llamada para obtener un chat consigo mismo, lo cual no tendría sentido y no devolvería nada.
   * El chat obtenido se almacenará en el chatId en el chat-service y se obtendrá aquí en chat.ts mediante un computed
   */
  getChat(receiver: string) {
    if (receiver !== 'support') this.participant.set(this.participants()?.find(participant => participant.username === receiver));
    this.chatService.getChat(receiver);
  }

  sendMessage() {
    if (this.participant()) {
      const message = {
        sender: this.customer()?.username,
        message: this.message(),
        chatId: this.chatId(),
        messageType: MessageType.CHAT,
      } as Message;
      this.chatService.sendMessage(this.chatId(), message);
      this.message.set('');
    }
  }
}
