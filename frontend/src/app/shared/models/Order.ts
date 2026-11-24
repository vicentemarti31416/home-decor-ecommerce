import { OrderStatus } from "../enums/OrderStatus";
import { Item } from "./Item";

export interface Order {
    id: string;
    items: Item[];
    status: OrderStatus;
}