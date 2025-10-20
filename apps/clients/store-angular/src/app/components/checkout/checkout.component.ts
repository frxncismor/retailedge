import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CartService } from '../../services/cart.service';
import { CartItem } from '../../services/product.service';

@Component({
  selector: 'app-checkout',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.css'],
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

    // Simulate order creation API call
    setTimeout(() => {
      console.log('Order placed:', {
        shipping: this.shippingInfo,
        payment: this.paymentInfo,
        items: this.cartItems,
      });

      this.cartService.clearCart();
      this.isProcessing = false;

      // Show success message
      const successMessage = document.createElement('div');
      successMessage.setAttribute('data-cy', 'order-success');
      successMessage.textContent = 'Order placed successfully!';
      successMessage.style.cssText =
        'position: fixed; top: 20px; right: 20px; background: #4CAF50; color: white; padding: 15px; border-radius: 5px; z-index: 1000;';
      document.body.appendChild(successMessage);

      // Remove message after 3 seconds
      setTimeout(() => {
        document.body.removeChild(successMessage);
      }, 3000);
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
