import { AfterViewInit, Component, inject, OnInit, signal } from '@angular/core';
import { Product } from '../../shared/models/Product';
import { HttpClient } from '@angular/common/http';
import { FlowbiteService } from '../../core/services/flowbite-service';
import { Router } from "@angular/router";
import { ProductStateService } from '../../core/services/product-state-service';
import { Chat } from '../../layout/chat/chat';
import { UpbuttonComponent } from '../../layout/upbutton/upbutton.component';

@Component({
  selector: 'app-gallery',
  imports: [
    Chat,
    UpbuttonComponent],
  templateUrl: './gallery.html',
  styleUrl: './gallery.css',
})
export class Gallery implements OnInit, AfterViewInit {

  private httpClient = inject(HttpClient);
  private flowbiteService = inject(FlowbiteService);
  private router = inject(Router);
  private productState = inject(ProductStateService);

  products = this.productState.products;

  ngOnInit(): void {
    if (this.products().length === 0) {
      this.httpClient.get<Product[]>('/api/products').subscribe((response) => {
        this.productState.setProducts(response);
      });
    }
  }

  ngAfterViewInit(): void {
    this.flowbiteService.loadFlowbite((flowbite) => {
      setTimeout(() => {
        flowbite.initFlowbite();
      }, 0);
    });
  }

  detail(product: Product) {
    this.productState.setProduct(product);
    this.router.navigate(['/detail', product.id]);
  }
}
