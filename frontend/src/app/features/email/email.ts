import { HttpClient } from '@angular/common/http';
import { Component, inject, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from "@angular/router";

@Component({
  selector: 'app-email',
  imports: [
    ReactiveFormsModule
  ],
  templateUrl: './email.html',
  styleUrl: './email.css',
})
export class Email {
  httpClient = inject(HttpClient);
  router = inject(Router);
  formBuilder = inject(FormBuilder);

  registerForm: FormGroup = this.initForm();

  isSamePassword = signal<boolean>(true);
  submitted = signal<boolean>(false);

  initForm(): FormGroup {
    return this.formBuilder.group({
      email: ['', Validators.required],
    })
  }

  // Sirve para enviar el código para resetear el password al email introducido por el usuario
  sendEmail() {
    this.submitted.set(true);
    this.httpClient.post<String>('/api/users/send-code', this.registerForm.get('email').value).subscribe(response => {
      this.router.navigate(['/reset']);
    });
  }
}





