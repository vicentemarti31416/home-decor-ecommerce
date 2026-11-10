import { HttpClient } from '@angular/common/http';
import { Component, inject, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from "@angular/router";

@Component({
  selector: 'app-register',
  imports: [
    ReactiveFormsModule
  ],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register {

  httpClient = inject(HttpClient);
  router = inject(Router);
  formBuilder = inject(FormBuilder);

  registerForm: FormGroup = this.initForm();

  isSamePassword = signal<boolean>(true);
  submitted = signal<boolean>(false);

  initForm(): FormGroup {
    return this.formBuilder.group({
      username: ['', Validators.required],
      password: ['', Validators.minLength(12)],
      repeatPassword: ['', Validators.minLength(12)]
    })
  }

  /**
   * Comprueba que las dos contraseñas indicadas por el usuario sean iguales
   */
  checkPassword(): boolean {
    this.registerForm.get('password').value !== this.registerForm.get('repeatPassword').value
      ? this.isSamePassword.set(false)
      : this.isSamePassword.set(true);
    return this.isSamePassword();
  }

  /**
   * Si pasa la validación pasa el username y el password al backend para crear la cuenta
   */
  createAccount() {
    this.submitted.set(true);
    if (this.checkPassword() && this.registerForm.valid) {
      this.httpClient.post<void>('/api/users/register', this.registerForm.value).subscribe(() => {
        this.router.navigate(['/']);
      });
    }
  }
}
