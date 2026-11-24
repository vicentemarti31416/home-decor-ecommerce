import { Message } from "./Message";

export interface Customer {

    name: string;
    lastName: string;
    username: string;
    roles: string[];
    address: string;
    city: string;
    postalCode: string;
    phone: string;
    email: string;
    photo: string; // La foto será estática hasta que se haga un dashboard para los customers
    favoriteIds: number[];
}