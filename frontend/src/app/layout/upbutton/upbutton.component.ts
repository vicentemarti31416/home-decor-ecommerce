import { Component } from '@angular/core';

@Component({
  selector: 'app-upbutton',
  standalone: true,
  imports: [],
  templateUrl: './upbutton.component.html',
  styleUrl: './upbutton.component.css',
})
export class UpbuttonComponent {
  
  scrollToTop(): void {
    window.scrollTo({
      top: 0,
      behavior: 'smooth',
    });
  }
}
