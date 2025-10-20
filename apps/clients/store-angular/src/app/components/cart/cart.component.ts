import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CartItem, CartService } from '../../services/cart.service';

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  template: `
    <div class="cart">
      <h1>Shopping Cart</h1>

      <div *ngIf="cartItems.length > 0; else emptyCart">
        <div class="cart-items">
          <div class="cart-item" *ngFor="let item of cartItems">
            <div class="item-image">
              <img
                [src]="
                  'https://via.placeholder.com/100x100?text=' +
                  item.product.name
                "
                [alt]="item.product.name"
              />
            </div>

            <div class="item-details">
              <h3>{{ item.product.name }}</h3>
              <p class="item-sku">SKU: {{ item.product.sku }}</p>
              <p class="item-price">
                {{
                  item.product.price | currency: 'USD' : 'symbol' : '1.2-2'
                }}
                each
              </p>
            </div>

            <div class="item-quantity">
              <label>Qty:</label>
              <input
                type="number"
                [value]="item.quantity"
                (change)="updateQuantity(item.product.id, $event)"
                min="1"
                [max]="item.product.stock"
                class="quantity-input"
              />
            </div>

            <div class="item-total">
              {{
                item.product.price * item.quantity
                  | currency: 'USD' : 'symbol' : '1.2-2'
              }}
            </div>

            <div class="item-actions">
              <button (click)="removeItem(item.product.id)" class="remove-btn">
                Remove
              </button>
            </div>
          </div>
        </div>

        <div class="cart-summary">
          <div class="summary-row">
            <span>Subtotal:</span>
            <span>{{
              getSubtotal() | currency: 'USD' : 'symbol' : '1.2-2'
            }}</span>
          </div>
          <div class="summary-row">
            <span>Tax (8%):</span>
            <span>{{ getTax() | currency: 'USD' : 'symbol' : '1.2-2' }}</span>
          </div>
          <div class="summary-row total">
            <span>Total:</span>
            <span>{{ getTotal() | currency: 'USD' : 'symbol' : '1.2-2' }}</span>
          </div>
        </div>

        <div class="cart-actions">
          <button (click)="clearCart()" class="clear-cart-btn">
            Clear Cart
          </button>
          <button [routerLink]="['/checkout']" class="checkout-btn">
            Proceed to Checkout
          </button>
        </div>
      </div>

      <ng-template #emptyCart>
        <div class="empty-cart">
          <h2>Your cart is empty</h2>
          <p>Add some products to get started!</p>
          <button [routerLink]="['/']" class="shop-btn">
            Continue Shopping
          </button>
        </div>
      </ng-template>
    </div>
  `,
  styles: [
    `
      .cart {
        padding: 20px;
        max-width: 1000px;
        margin: 0 auto;
      }

      .cart h1 {
        margin-bottom: 30px;
        color: #333;
      }

      .cart-items {
        margin-bottom: 30px;
      }

      .cart-item {
        display: grid;
        grid-template-columns: 100px 1fr auto auto auto;
        gap: 20px;
        align-items: center;
        padding: 20px;
        border: 1px solid #ddd;
        border-radius: 8px;
        margin-bottom: 15px;
      }

      .item-image img {
        width: 100px;
        height: 100px;
        object-fit: cover;
        border-radius: 4px;
      }

      .item-details h3 {
        margin: 0 0 5px 0;
        color: #333;
      }

      .item-sku {
        color: #666;
        font-size: 0.9em;
        margin: 0 0 5px 0;
      }

      .item-price {
        color: #2c5aa0;
        font-weight: bold;
        margin: 0;
      }

      .item-quantity {
        display: flex;
        align-items: center;
        gap: 10px;
      }

      .quantity-input {
        width: 60px;
        padding: 5px;
        border: 1px solid #ddd;
        border-radius: 4px;
      }

      .item-total {
        font-size: 1.2em;
        font-weight: bold;
        color: #333;
      }

      .remove-btn {
        background-color: #e74c3c;
        color: white;
        border: none;
        padding: 8px 12px;
        border-radius: 4px;
        cursor: pointer;
      }

      .cart-summary {
        background-color: #f8f9fa;
        padding: 20px;
        border-radius: 8px;
        margin-bottom: 20px;
      }

      .summary-row {
        display: flex;
        justify-content: space-between;
        margin-bottom: 10px;
      }

      .summary-row.total {
        font-size: 1.2em;
        font-weight: bold;
        border-top: 1px solid #ddd;
        padding-top: 10px;
        margin-top: 10px;
      }

      .cart-actions {
        display: flex;
        gap: 15px;
        justify-content: flex-end;
      }

      .clear-cart-btn,
      .checkout-btn,
      .shop-btn {
        padding: 12px 24px;
        border: none;
        border-radius: 4px;
        cursor: pointer;
        font-size: 16px;
      }

      .clear-cart-btn {
        background-color: #6c757d;
        color: white;
      }

      .checkout-btn {
        background-color: #28a745;
        color: white;
      }

      .shop-btn {
        background-color: #2c5aa0;
        color: white;
      }

      .empty-cart {
        text-align: center;
        padding: 60px 20px;
      }

      .empty-cart h2 {
        color: #666;
        margin-bottom: 10px;
      }

      .empty-cart p {
        color: #999;
        margin-bottom: 30px;
      }

      @media (max-width: 768px) {
        .cart-item {
          grid-template-columns: 1fr;
          text-align: center;
        }

        .cart-actions {
          flex-direction: column;
        }
      }
    `,
  ],
})
export class CartComponent implements OnInit {
  cartItems: CartItem[] = [];

  constructor(private cartService: CartService) {}

  ngOnInit(): void {
    this.cartService.cartItems$.subscribe((items) => {
      this.cartItems = items;
    });
  }

  updateQuantity(productId: string, event: Event): void {
    const target = event.target as HTMLInputElement;
    const quantity = parseInt(target.value, 10);
    this.cartService.updateQuantity(productId, quantity);
  }

  removeItem(productId: string): void {
    this.cartService.removeFromCart(productId);
  }

  clearCart(): void {
    this.cartService.clearCart();
  }

  getSubtotal(): number {
    return this.cartService.getCartTotal();
  }

  getTax(): number {
    return this.getSubtotal() * 0.08;
  }

  getTotal(): number {
    return this.getSubtotal() + this.getTax();
  }
}
