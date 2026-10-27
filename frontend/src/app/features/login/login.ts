import { Component, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { FlowbiteService } from '../../core/services/flowbite-service';
import { catchError, of } from 'rxjs';

@Component({
  selector: 'app-login',
  imports: [],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
  protected readonly title = signal('store');

  message01 = signal('Api has not been called yet');
  message02 = signal('Api has not been called yet');
  userinfo01 = signal('');
  isLogin = signal<boolean>(true);

  private httpClient = inject(HttpClient);
  private flowbiteService = inject(FlowbiteService);

  private handleError = () => of('Error al obtener los datos');

  ngAfterViewInit(): void {
    this.flowbiteService.loadFlowbite((flowbite) => {
      setTimeout(() => {
        flowbite.initFlowbite();
      }, 0);
    });
  }

  button01() {
    this.httpClient
      .get('/resource1', { responseType: 'text', withCredentials: true })
      .pipe(catchError(this.handleError))
      .subscribe((response) => this.message01.set(response));
  }

  button02() {
    this.httpClient
      .get('/resource2', { responseType: 'text', withCredentials: true })
      .pipe(catchError(this.handleError))
      .subscribe((response) => this.message02.set(response));
  }

  login() {
    window.location.href = '/oauth2/authorization/gateway';
  }

  userinfo() {
    this.httpClient
      .get('/me', { responseType: 'text' })
      .pipe(catchError(this.handleError))
      .subscribe((response) => this.userinfo01.set(response));
  }

  switchLogin() {
    this.isLogin() ? this.isLogin.set(false) : this.isLogin.set(true);
  }
}
