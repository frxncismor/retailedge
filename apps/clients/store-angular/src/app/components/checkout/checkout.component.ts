import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CartItem, CartService } from '../../services/cart.service';

@Component({
  selector: 'app-checkout',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  template: `
    <div class="checkout">
      <h1>Checkout</h1>

      <div *ngIf="cartItems.length > 0; else emptyCart">
        <form (ngSubmit)="onSubmit()" #checkoutForm="ngForm">
          <div class="checkout-container">
            <div class="checkout-form">
              <h2>Shipping Information</h2>

              <div class="form-group">
                <label for="firstName">First Name *</label>
                <input
                  type="text"
                  id="firstName"
                  name="firstName"
                  [(ngModel)]="shippingInfo.firstName"
                  required
                  class="form-control"
                />
              </div>

              <div class="form-group">
                <label for="lastName">Last Name *</label>
                <input
                  type="text"
                  id="lastName"
                  name="lastName"
                  [(ngModel)]="shippingInfo.lastName"
                  required
                  class="form-control"
                />
              </div>

              <div class="form-group">
                <label for="email">Email *</label>
                <input
                  type="email"
                  id="email"
                  name="email"
                  [(ngModel)]="shippingInfo.email"
                  required
                  class="form-control"
                />
              </div>

              <div class="form-group">
                <label for="address">Address *</label>
                <input
                  type="text"
                  id="address"
                  name="address"
                  [(ngModel)]="shippingInfo.address"
                  required
                  class="form-control"
                />
              </div>

              <div class="form-group">
                <label for="city">City *</label>
                <input
                  type="text"
                  id="city"
                  name="city"
                  [(ngModel)]="shippingInfo.city"
                  required
                  class="form-control"
                />
              </div>

              <div class="form-group">
                <label for="zipCode">ZIP Code *</label>
                <input
                  type="text"
                  id="zipCode"
                  name="zipCode"
                  [(ngModel)]="shippingInfo.zipCode"
                  required
                  class="form-control"
                />
              </div>

              <h2>Payment Information</h2>

              <div class="form-group">
                <label for="cardNumber">Card Number *</label>
                <input
                  type="text"
                  id="cardNumber"
                  name="cardNumber"
                  [(ngModel)]="paymentInfo.cardNumber"
                  required
                  placeholder="1234 5678 9012 3456"
                  class="form-control"
                />
              </div>

              <div class="form-row">
                <div class="form-group">
                  <label for="expiryDate">Expiry Date *</label>
                  <input
                    type="text"
                    id="expiryDate"
                    name="expiryDate"
                    [(ngModel)]="paymentInfo.expiryDate"
                    required
                    placeholder="MM/YY"
                    class="form-control"
                  />
                </div>

                <div class="form-group">
                  <label for="cvv">CVV *</label>
                  <input
                    type="text"
                    id="cvv"
                    name="cvv"
                    [(ngModel)]="paymentInfo.cvv"
                    required
                    placeholder="123"
                    class="form-control"
                  />
                </div>
              </div>
            </div>

            <div class="order-summary">
              <h2>Order Summary</h2>

              <div class="order-items">
                <div class="order-item" *ngFor="let item of cartItems">
                  <div class="item-info">
                    <span class="item-name">{{ item.product.name }}</span>
                    <span class="item-quantity">Qty: {{ item.quantity }}</span>
                  </div>
                  <div class="item-price">
                    {{
                      item.product.price * item.quantity
                        | currency: 'USD' : 'symbol' : '1.2-2'
                    }}
                  </div>
                </div>
              </div>

              <div class="order-totals">
                <div class="total-row">
                  <span>Subtotal:</span>
                  <span>{{
                    getSubtotal() | currency: 'USD' : 'symbol' : '1.2-2'
                  }}</span>
                </div>
                <div class="total-row">
                  <span>Tax (8%):</span>
                  <span>{{
                    getTax() | currency: 'USD' : 'symbol' : '1.2-2'
                  }}</span>
                </div>
                <div class="total-row final">
                  <span>Total:</span>
                  <span>{{
                    getTotal() | currency: 'USD' : 'symbol' : '1.2-2'
                  }}</span>
                </div>
              </div>

              <button
                type="submit"
                [disabled]="!checkoutForm.valid || isProcessing"
                class="place-order-btn"
              >
                {{ isProcessing ? 'Processing...' : 'Place Order' }}
              </button>
            </div>
          </div>
        </form>
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
      .checkout {
        padding: 20px;
        max-width: 1200px;
        margin: 0 auto;
      }

      .checkout h1 {
        margin-bottom: 30px;
        color: #333;
      }

      .checkout-container {
        display: grid;
        grid-template-columns: 1fr 400px;
        gap: 40px;
      }

      .checkout-form h2 {
        color: #333;
        margin: 30px 0 20px 0;
        border-bottom: 2px solid #2c5aa0;
        padding-bottom: 10px;
      }

      .form-group {
        margin-bottom: 20px;
      }

      .form-group label {
        display: block;
        margin-bottom: 5px;
        font-weight: bold;
        color: #333;
      }

      .form-control {
        width: 100%;
        padding: 10px;
        border: 1px solid #ddd;
        border-radius: 4px;
        font-size: 16px;
      }

      .form-control:focus {
        outline: none;
        border-color: #2c5aa0;
        box-shadow: 0 0 0 2px rgba(44, 90, 160, 0.2);
      }

      .form-row {
        display: grid;
        grid-template-columns: 1fr 1fr;
        gap: 15px;
      }

      .order-summary {
        background-color: #f8f9fa;
        padding: 20px;
        border-radius: 8px;
        height: fit-content;
      }

      .order-summary h2 {
        color: #333;
        margin: 0 0 20px 0;
        border-bottom: 2px solid #2c5aa0;
        padding-bottom: 10px;
      }

      .order-items {
        margin-bottom: 20px;
      }

      .order-item {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 10px 0;
        border-bottom: 1px solid #ddd;
      }

      .item-info {
        display: flex;
        flex-direction: column;
      }

      .item-name {
        font-weight: bold;
        color: #333;
      }

      .item-quantity {
        font-size: 0.9em;
        color: #666;
      }

      .item-price {
        font-weight: bold;
        color: #2c5aa0;
      }

      .order-totals {
        margin-bottom: 20px;
      }

      .total-row {
        display: flex;
        justify-content: space-between;
        margin-bottom: 10px;
      }

      .total-row.final {
        font-size: 1.2em;
        font-weight: bold;
        border-top: 2px solid #2c5aa0;
        padding-top: 10px;
        margin-top: 10px;
      }

      .place-order-btn {
        width: 100%;
        padding: 15px;
        background-color: #28a745;
        color: white;
        border: none;
        border-radius: 4px;
        font-size: 18px;
        font-weight: bold;
        cursor: pointer;
      }

      .place-order-btn:disabled {
        background-color: #ccc;
        cursor: not-allowed;
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

      .shop-btn {
        background-color: #2c5aa0;
        color: white;
        border: none;
        padding: 12px 24px;
        border-radius: 4px;
        cursor: pointer;
        font-size: 16px;
      }

      @media (max-width: 768px) {
        .checkout-container {
          grid-template-columns: 1fr;
          gap: 20px;
        }

        .form-row {
          grid-template-columns: 1fr;
        }
      }
    `,
  ],
})
export class CheckoutComponent implements OnInit {
  cartItems: CartItem[] = [];
  isProcessing = false;

  shippingInfo = {
    firstName: '',
    lastName: '',
    email: '',
    address: '',
    city: '',
    zipCode: '',
  };

  paymentInfo = {
    cardNumber: '',
    expiryDate: '',
    cvv: '',
  };

  constructor(private cartService: CartService) {}

  ngOnInit(): void {
    this.cartService.cartItems$.subscribe((items) => {
      this.cartItems = items;
    });
  }

  onSubmit(): void {
    if (this.cartItems.length === 0) {
      return;
    }

    this.isProcessing = true;

    // Simulate order processing
    setTimeout(() => {
      console.log('Order placed:', {
        shipping: this.shippingInfo,
        payment: this.paymentInfo,
        items: this.cartItems,
      });

      // Clear cart and redirect
      this.cartService.clearCart();
      this.isProcessing = false;
      alert('Order placed successfully!');
      // In a real app, redirect to order confirmation page
    }, 2000);
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
