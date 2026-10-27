import { ApplicationConfig, provideBrowserGlobalErrorListeners, LOCALE_ID } from '@angular/core';
import { provideRouter } from '@angular/router';
import { registerLocaleData } from '@angular/common';
import localeEs from '@angular/common/locales/es';
import { routes } from './app.routes';
import { provideNgxStripe } from 'ngx-stripe';
import { provideHttpClient, withXsrfConfiguration } from '@angular/common/http';

registerLocaleData(localeEs);

export const appConfig: ApplicationConfig = {
  providers: [
    provideHttpClient(
      withXsrfConfiguration({
        cookieName: 'XSRF-TOKEN',
        headerName: 'X-XSRF-TOKEN',
      }),
    ),
    provideBrowserGlobalErrorListeners(),
    provideRouter(routes),
    provideHttpClient(),
    provideNgxStripe("pk_test_51TYmNQCbrYzecaQ1GxRKsT2HyuZuqnqvuLmlwzppq36x0r1GloZ4ElasPadMMId1Q5ALB82oEWboq4YQuDV5yAhN00KvthCRJF"),
    { provide: LOCALE_ID, useValue: 'es-ES' } // Sirve para que los precios y demás se muestren con estilo español
  ]
};
