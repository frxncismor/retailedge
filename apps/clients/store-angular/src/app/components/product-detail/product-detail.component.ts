import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { Product, ProductService } from '../../services/product.service';
import { CartService } from '../../services/cart.service';

@Component({
  selector: 'app-product-detail',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  template: `
    <div class="product-detail" *ngIf="product; else loading">
      <div class="product-container">
        <div class="product-image">
          <img
            [src]="'https://via.placeholder.com/500x400?text=' + product.name"
            [alt]="product.name"
          />
        </div>

        <div class="product-info">
          <h1>{{ product.name }}</h1>
          <div class="product-sku">SKU: {{ product.sku }}</div>
          <p class="product-description">{{ product.description }}</p>

          <div class="product-price">
            {{ product.price | currency: 'USD' : 'symbol' : '1.2-2' }}
          </div>

          <div class="product-stock" [class.low-stock]="product.stock < 10">
            {{ product.stock }} in stock
          </div>

          <div class="quantity-selector">
            <label for="quantity">Quantity:</label>
            <input
              type="number"
              id="quantity"
              [(ngModel)]="quantity"
              [max]="product.stock"
              min="1"
              class="quantity-input"
            />
          </div>

          <div class="product-actions">
            <button
              (click)="addToCart()"
              [disabled]="product.stock === 0 || quantity > product.stock"
              class="add-to-cart-btn"
            >
              {{ product.stock === 0 ? 'Out of Stock' : 'Add to Cart' }}
            </button>
            <button (click)="goBack()" class="back-btn">
              Back to Products
            </button>
          </div>
        </div>
      </div>
    </div>

    <ng-template #loading>
      <div class="loading">
        <p>Loading product...</p>
      </div>
    </ng-template>
  `,
  styles: [
    `
      .product-detail {
        padding: 20px;
        max-width: 1000px;
        margin: 0 auto;
      }

      .product-container {
        display: grid;
        grid-template-columns: 1fr 1fr;
        gap: 40px;
        align-items: start;
      }

      .product-image img {
        width: 100%;
        height: 400px;
        object-fit: cover;
        border-radius: 8px;
      }

      .product-info h1 {
        margin: 0 0 10px 0;
        color: #333;
      }

      .product-sku {
        color: #666;
        font-size: 0.9em;
        margin-bottom: 15px;
      }

      .product-description {
        color: #555;
        line-height: 1.6;
        margin-bottom: 20px;
      }

      .product-price {
        font-size: 2em;
        font-weight: bold;
        color: #2c5aa0;
        margin-bottom: 10px;
      }

      .product-stock {
        font-size: 1.1em;
        color: #666;
        margin-bottom: 20px;
      }

      .low-stock {
        color: #e74c3c;
        font-weight: bold;
      }

      .quantity-selector {
        margin-bottom: 20px;
      }

      .quantity-selector label {
        display: block;
        margin-bottom: 5px;
        font-weight: bold;
      }

      .quantity-input {
        width: 80px;
        padding: 8px;
        border: 1px solid #ddd;
        border-radius: 4px;
      }

      .product-actions {
        display: flex;
        gap: 15px;
      }

      .add-to-cart-btn,
      .back-btn {
        padding: 12px 24px;
        border: none;
        border-radius: 4px;
        cursor: pointer;
        font-size: 16px;
      }

      .add-to-cart-btn {
        background-color: #2c5aa0;
        color: white;
        flex: 1;
      }

      .add-to-cart-btn:disabled {
        background-color: #ccc;
        cursor: not-allowed;
      }

      .back-btn {
        background-color: #f8f9fa;
        color: #333;
        border: 1px solid #ddd;
      }

      .loading {
        text-align: center;
        padding: 40px;
      }

      @media (max-width: 768px) {
        .product-container {
          grid-template-columns: 1fr;
          gap: 20px;
        }
      }
    `,
  ],
})
export class ProductDetailComponent implements OnInit {
  product: Product | null = null;
  quantity: number = 1;

  constructor(
    private route: ActivatedRoute,
    private productService: ProductService,
    private cartService: CartService
  ) {}

  ngOnInit(): void {
    const productId = this.route.snapshot.paramMap.get('id');
    if (productId) {
      this.loadProduct(productId);
    }
  }

  loadProduct(id: string): void {
    this.productService.getProduct(id).subscribe({
      next: (product) => {
        this.product = product;
      },
      error: (error) => {
        console.error('Error loading product:', error);
      },
    });
  }

  addToCart(): void {
    if (this.product && this.quantity > 0) {
      this.cartService.addToCart(this.product, this.quantity);
    }
  }

  goBack(): void {
    window.history.back();
  }
}
