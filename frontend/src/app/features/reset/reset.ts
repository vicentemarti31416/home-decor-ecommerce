import { HttpClient } from '@angular/common/http';
import { Component, inject, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from "@angular/router";

@Component({
  selector: 'app-reset',
  imports: [
    ReactiveFormsModule
  ],
  templateUrl: './reset.html',
  styleUrl: './reset.css',
})
export class Reset {
  httpClient = inject(HttpClient);
  router = inject(Router);
  formBuilder = inject(FormBuilder);

  registerForm: FormGroup = this.initForm();

  isSamePassword = signal<boolean>(true);
  submitted = signal<boolean>(false);

  initForm(): FormGroup {
    return this.formBuilder.group({
      code: ['', Validators.required],
      password: ['', Validators.minLength(12)],
      repeatPassword: ['', Validators.minLength(12)]
    })
  }

  /**
   * Comprueba si los dos passwords indicados son iguales, en caso contrario se mostrará un aviso en la vista
   */
  checkPassword(): boolean {
    this.registerForm.get('password').value !== this.registerForm.get('repeatPassword').value
      ? this.isSamePassword.set(false)
      : this.isSamePassword.set(true);
    return this.isSamePassword();
  }

  /**
   * Si pasa la validación envía el nuevo password y el código para resetearlo al backend 
   */
  resetPassword() {
    this.submitted.set(true);
    if (this.checkPassword() && this.registerForm.valid) {
      this.httpClient.post<void>('/api/users/reset-password', this.registerForm.value).subscribe(() => {
        this.router.navigate(['/']);
      });
    }
  }
}
