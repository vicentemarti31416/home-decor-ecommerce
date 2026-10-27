import { ChatRoomType } from "./ChatRoomType";
import { Participant } from "./Participant";

/**
 * Es la interface que define los usuarios que forman parte de un chat y la constante que define si es público o privado 
 */
export interface Chat {

    id: number;
    chatRoomType: ChatRoomType;
    created: Date;
    participants: Participant[]
}