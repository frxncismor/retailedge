import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { Product, ProductService } from '../../services/product.service';
import { CartService } from '../../services/cart.service';

@Component({
  selector: 'app-product-list',
  standalone: true,
  imports: [CommonModule, RouterModule],
  template: `
    <div class="product-list">
      <div class="header">
        <h1>Our Products</h1>
        <div class="search-bar">
          <input
            type="text"
            placeholder="Search products..."
            [(ngModel)]="searchQuery"
            (input)="onSearch()"
            class="search-input"
          />
        </div>
      </div>

      <div class="products-grid" *ngIf="products.length > 0; else noProducts">
        <div class="product-card" *ngFor="let product of filteredProducts">
          <div class="product-image">
            <img
              [src]="'https://via.placeholder.com/300x200?text=' + product.name"
              [alt]="product.name"
            />
          </div>
          <div class="product-info">
            <h3>{{ product.name }}</h3>
            <p class="product-description">{{ product.description }}</p>
            <div class="product-price">
              {{ product.price | currency: 'USD' : 'symbol' : '1.2-2' }}
            </div>
            <div class="product-stock" [class.low-stock]="product.stock < 10">
              {{ product.stock }} in stock
            </div>
            <div class="product-actions">
              <button
                (click)="addToCart(product)"
                [disabled]="product.stock === 0"
                class="add-to-cart-btn"
              >
                {{ product.stock === 0 ? 'Out of Stock' : 'Add to Cart' }}
              </button>
              <button
                [routerLink]="['/product', product.id]"
                class="view-details-btn"
              >
                View Details
              </button>
            </div>
          </div>
        </div>
      </div>

      <ng-template #noProducts>
        <div class="no-products">
          <p>No products found.</p>
        </div>
      </ng-template>
    </div>
  `,
  styles: [
    `
      .product-list {
        padding: 20px;
        max-width: 1200px;
        margin: 0 auto;
      }

      .header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 30px;
      }

      .search-bar {
        width: 300px;
      }

      .search-input {
        width: 100%;
        padding: 10px;
        border: 1px solid #ddd;
        border-radius: 4px;
      }

      .products-grid {
        display: grid;
        grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
        gap: 20px;
      }

      .product-card {
        border: 1px solid #ddd;
        border-radius: 8px;
        overflow: hidden;
        transition: box-shadow 0.3s;
      }

      .product-card:hover {
        box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
      }

      .product-image img {
        width: 100%;
        height: 200px;
        object-fit: cover;
      }

      .product-info {
        padding: 15px;
      }

      .product-info h3 {
        margin: 0 0 10px 0;
        color: #333;
      }

      .product-description {
        color: #666;
        margin-bottom: 10px;
      }

      .product-price {
        font-size: 1.2em;
        font-weight: bold;
        color: #2c5aa0;
        margin-bottom: 5px;
      }

      .product-stock {
        font-size: 0.9em;
        color: #666;
        margin-bottom: 15px;
      }

      .low-stock {
        color: #e74c3c;
        font-weight: bold;
      }

      .product-actions {
        display: flex;
        gap: 10px;
      }

      .add-to-cart-btn,
      .view-details-btn {
        flex: 1;
        padding: 8px 12px;
        border: none;
        border-radius: 4px;
        cursor: pointer;
        font-size: 14px;
      }

      .add-to-cart-btn {
        background-color: #2c5aa0;
        color: white;
      }

      .add-to-cart-btn:disabled {
        background-color: #ccc;
        cursor: not-allowed;
      }

      .view-details-btn {
        background-color: #f8f9fa;
        color: #333;
        border: 1px solid #ddd;
      }

      .no-products {
        text-align: center;
        padding: 40px;
        color: #666;
      }
    `,
  ],
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
