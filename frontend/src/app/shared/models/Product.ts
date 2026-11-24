import { Photo } from "./Photo";
import { RatingRequest } from "./RatingRequest";

export interface Product {
    id: number;
    name: string;
    description: string;
    price: number;
    category: string;
    reference: string;
    photos: Photo[];
    createdDate: Date;
    stock: number;
    rating: number;
    ratings: RatingRequest[];
    statistics: number[];
}