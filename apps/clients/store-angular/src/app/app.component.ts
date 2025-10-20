import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { CartService } from './services/cart.service';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterModule],
  template: `
    <div class="app">
      <header class="header">
        <div class="container">
          <h1 class="logo">
            <a routerLink="/">RetailEdge</a>
          </h1>
          <nav class="nav">
            <a
              routerLink="/"
              routerLinkActive="active"
              [routerLinkActiveOptions]="{ exact: true }"
            >
              Products
            </a>
            <a routerLink="/cart" routerLinkActive="active">
              Cart ({{ cartItemCount$ | async }})
            </a>
          </nav>
        </div>
      </header>

      <main class="main">
        <router-outlet></router-outlet>
      </main>

      <footer class="footer">
        <div class="container">
          <p>&copy; 2024 RetailEdge. All rights reserved.</p>
        </div>
      </footer>
    </div>
  `,
  styles: [
    `
      .app {
        min-height: 100vh;
        display: flex;
        flex-direction: column;
      }

      .header {
        background-color: #2c5aa0;
        color: white;
        padding: 1rem 0;
        box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
      }

      .container {
        max-width: 1200px;
        margin: 0 auto;
        padding: 0 20px;
        display: flex;
        justify-content: space-between;
        align-items: center;
      }

      .logo a {
        color: white;
        text-decoration: none;
        font-size: 1.5rem;
        font-weight: bold;
      }

      .nav {
        display: flex;
        gap: 2rem;
      }

      .nav a {
        color: white;
        text-decoration: none;
        padding: 0.5rem 1rem;
        border-radius: 4px;
        transition: background-color 0.3s;
      }

      .nav a:hover,
      .nav a.active {
        background-color: rgba(255, 255, 255, 0.1);
      }

      .main {
        flex: 1;
        padding: 2rem 0;
      }

      .footer {
        background-color: #f8f9fa;
        padding: 1rem 0;
        text-align: center;
        color: #666;
        border-top: 1px solid #ddd;
      }
    `,
  ],
})
export class AppComponent {
  cartItemCount$: Observable<number>;

  constructor(private cartService: CartService) {
    this.cartItemCount$ = this.cartService.cartItems$.pipe(
      map((items) => items.reduce((total, item) => total + item.quantity, 0))
    );
  }
}
