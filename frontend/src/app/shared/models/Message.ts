import { MessageType } from "./MessageType";

/**
 * Define los datos de cada mensaje que se envía
 */
export interface Message {

  id?: number;  
  sender: string;
  message: string;
  chatId?: number;
  messageType: MessageType;
  created: Date;
  isRead?: boolean;
}