import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CartService } from '../../services/cart.service';

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

    setTimeout(() => {
      console.log('Order placed:', {
        shipping: this.shippingInfo,
        payment: this.paymentInfo,
        items: this.cartItems,
      });

      this.cartService.clearCart();
      this.isProcessing = false;
      alert('Order placed successfully!');
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
