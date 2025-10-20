import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { Product } from '@retailedge/api-types';
import { CartItem } from './product.service';

@Injectable({
  providedIn: 'root',
})
export class CartService {
  private readonly cartItems = new BehaviorSubject<CartItem[]>([]);
  public cartItems$ = this.cartItems.asObservable();

  constructor() {
    this.loadCartFromStorage();
  }

  addToCart(product: Product, quantity: number = 1): void {
    const currentItems = this.cartItems.value;
    const existingItem = currentItems.find(
      (item) => item.product.id === product.id
    );

    if (existingItem) {
      existingItem.quantity += quantity;
    } else {
      currentItems.push({ product, quantity });
    }

    this.cartItems.next([...currentItems]);
    this.saveCartToStorage();
  }

  removeFromCart(productId: string): void {
    const currentItems = this.cartItems.value.filter(
      (item) => item.product.id !== productId
    );
    this.cartItems.next(currentItems);
    this.saveCartToStorage();
  }

  updateQuantity(productId: string, quantity: number): void {
    const currentItems = this.cartItems.value;
    const item = currentItems.find((item) => item.product.id === productId);

    if (item) {
      if (quantity <= 0) {
        this.removeFromCart(productId);
      } else {
        item.quantity = quantity;
        this.cartItems.next([...currentItems]);
        this.saveCartToStorage();
      }
    }
  }

  clearCart(): void {
    this.cartItems.next([]);
    this.saveCartToStorage();
  }

  getCartTotal(): number {
    return this.cartItems.value.reduce(
      (total, item) => total + item.product.price * item.quantity,
      0
    );
  }

  getCartItemCount(): number {
    return this.cartItems.value.reduce(
      (total, item) => total + item.quantity,
      0
    );
  }

  private saveCartToStorage(): void {
    localStorage.setItem(
      'retailedge_cart',
      JSON.stringify(this.cartItems.value)
    );
  }

  private loadCartFromStorage(): void {
    const savedCart = localStorage.getItem('retailedge_cart');
    if (savedCart) {
      try {
        const cartItems = JSON.parse(savedCart);
        this.cartItems.next(cartItems);
      } catch (error) {
        console.error('Error loading cart from storage:', error);
      }
    }
  }
}
