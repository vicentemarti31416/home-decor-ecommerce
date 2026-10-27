import { Routes } from '@angular/router';
import { Gallery } from './features/gallery/gallery';
import { Upload } from './features/upload/upload';
import { Login } from './features/login/login';
import { Detail } from './features/detail/detail';
import { Cart } from './features/cart/cart';
import { Payment } from './features/payment/payment';
import { Success } from './features/success/success';
import { Register } from './features/register/register';
import { MainLayout } from './layouts/main-layout/main-layout';
import { AuthLayout } from './layouts/auth-layout/auth-layout';
import { Shipping } from './features/shipping/shipping';
import { authGuard } from './core/guards/auth-guard';
import { Orders } from './features/orders/orders';
import { Reset } from './features/reset/reset';
import { Email } from './features/email/email';
import { Rating } from './features/rating/rating';
import { Reviews } from './features/reviews/reviews';

export const routes: Routes = [
    {
        path: '',
        component: MainLayout,
        children: [
            { path: '', component: Gallery },
            { path: 'detail/:id', component: Detail },
            { path: 'upload', component: Upload },
            { path: 'orders', component: Orders, canActivate: [authGuard]},
            { path: 'cart', component: Cart, canActivate: [authGuard] },
            { path: 'shipping', component: Shipping, canActivate: [authGuard] },
            { path: 'payment', component: Payment, canActivate: [authGuard] },
            { path: 'success', component: Success, canActivate: [authGuard] },
            { path: 'rating', component: Rating, canActivate: [authGuard] },
            { path: 'reviews', component: Reviews },
            { path: 'signin', component: Login },
        ]
    }, {
        path: '',
        component: AuthLayout,
        children: [
            { path: 'register', component: Register },
            { path: 'email', component: Email },
            { path: 'reset', component: Reset }
        ]
    },
    { path: '**', redirectTo: '' }
];
