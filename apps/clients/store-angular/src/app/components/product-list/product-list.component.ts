import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { Product } from '@retailedge/api-types';
import { ProductService } from '../../services/product.service';
import { CartService } from '../../services/cart.service';

@Component({
  selector: 'app-product-list',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.css'],
})
export class ProductListComponent implements OnInit {
  products: Product[] = [];
  filteredProducts: Product[] = [];
  searchQuery: string = '';

  constructor(
    private productService: ProductService,
    private cartService: CartService
  ) {}

  ngOnInit(): void {
    this.loadProducts();
  }

  loadProducts(): void {
    this.productService.getProducts().subscribe({
      next: (products) => {
        this.products = products;
        this.filteredProducts = products;
      },
      error: (error) => {
        console.error('Error loading products:', error);
      },
    });
  }

  onSearch(): void {
    if (this.searchQuery.trim()) {
      this.productService.searchProducts(this.searchQuery).subscribe({
        next: (products) => {
          this.filteredProducts = products;
        },
        error: (error) => {
          console.error('Error searching products:', error);
          this.filteredProducts = this.products;
        },
      });
    } else {
      this.filteredProducts = this.products;
    }
  }

  addToCart(product: Product): void {
    this.cartService.addToCart(product);
  }
}
