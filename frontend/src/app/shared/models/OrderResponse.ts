import { OrderStatus } from "../enums/OrderStatus";
import { ProductResponse } from "./ProductResponse";

export interface OrderResponse {
    id: string;
    amount: number;
    status: keyof typeof OrderStatus; // Tipado como string podría dar problemas al intentar usarlo como índice del enum
    createdDate: Date;
    updatedDate: Date;
    products: ProductResponse[];
}