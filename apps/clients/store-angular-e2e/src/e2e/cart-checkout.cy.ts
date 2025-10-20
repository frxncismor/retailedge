describe('Cart to Checkout Flow', () => {
  beforeEach(() => {
    // Mock API responses
    cy.intercept('GET', '**/api/catalog/products', {
      statusCode: 200,
      body: [
        {
          id: '1',
          sku: 'TEST-001',
          name: 'Test Product 1',
          description: 'Test Description 1',
          price: 29.99,
          stock: 10,
          imageUrl: 'https://via.placeholder.com/300x200?text=Test+Product+1',
          createdAt: '2024-01-01T00:00:00Z',
          updatedAt: '2024-01-01T00:00:00Z',
        },
        {
          id: '2',
          sku: 'TEST-002',
          name: 'Test Product 2',
          description: 'Test Description 2',
          price: 39.99,
          stock: 5,
          imageUrl: 'https://via.placeholder.com/300x200?text=Test+Product+2',
          createdAt: '2024-01-02T00:00:00Z',
          updatedAt: '2024-01-02T00:00:00Z',
        },
      ],
    }).as('getProducts');

    cy.intercept('GET', '**/api/catalog/products/1', {
      statusCode: 200,
      body: {
        id: '1',
        sku: 'TEST-001',
        name: 'Test Product 1',
        description: 'Test Description 1',
        price: 29.99,
        stock: 10,
        imageUrl: 'https://via.placeholder.com/300x200?text=Test+Product+1',
        createdAt: '2024-01-01T00:00:00Z',
        updatedAt: '2024-01-01T00:00:00Z',
      },
    }).as('getProduct');

    cy.intercept('POST', '**/api/orders', {
      statusCode: 201,
      body: {
        id: 'order-123',
        customerId: 'customer-456',
        orderDate: '2024-01-15T10:30:00Z',
        status: 'PENDING',
        totalAmount: 68.98,
        items: [
          {
            id: 'item-1',
            productId: '1',
            quantity: 1,
            unitPrice: 29.99,
            totalPrice: 29.99,
          },
          {
            id: 'item-2',
            productId: '2',
            quantity: 1,
            unitPrice: 39.99,
            totalPrice: 39.99,
          },
        ],
        createdAt: '2024-01-15T10:30:00Z',
        updatedAt: '2024-01-15T10:30:00Z',
      },
    }).as('createOrder');

    // Visit the storefront
    cy.visit('/');
  });

  it('should complete full cart to checkout flow', () => {
    // Wait for products to load
    cy.wait('@getProducts');

    // Verify products are displayed
    cy.get('[data-cy=product-card]').should('have.length', 2);
    cy.get('[data-cy=product-card]')
      .first()
      .should('contain', 'Test Product 1');
    cy.get('[data-cy=product-card]').last().should('contain', 'Test Product 2');

    // Add first product to cart
    cy.get('[data-cy=product-card]')
      .first()
      .find('[data-cy=add-to-cart-btn]')
      .click();

    // Verify cart count updates
    cy.get('[data-cy=cart-count]').should('contain', '1');

    // Add second product to cart
    cy.get('[data-cy=product-card]')
      .last()
      .find('[data-cy=add-to-cart-btn]')
      .click();

    // Verify cart count updates
    cy.get('[data-cy=cart-count]').should('contain', '2');

    // Navigate to cart
    cy.get('[data-cy=cart-link]').click();

    // Verify cart page loads
    cy.url().should('include', '/cart');
    cy.get('[data-cy=cart-title]').should('contain', 'Shopping Cart');

    // Verify cart items are displayed
    cy.get('[data-cy=cart-item]').should('have.length', 2);

    // Verify first cart item
    cy.get('[data-cy=cart-item]').first().should('contain', 'Test Product 1');
    cy.get('[data-cy=cart-item]').first().should('contain', '$29.99');

    // Verify second cart item
    cy.get('[data-cy=cart-item]').last().should('contain', 'Test Product 2');
    cy.get('[data-cy=cart-item]').last().should('contain', '$39.99');

    // Verify cart totals
    cy.get('[data-cy=subtotal]').should('contain', '$69.98');
    cy.get('[data-cy=tax]').should('contain', '$5.60'); // 8% tax
    cy.get('[data-cy=total]').should('contain', '$75.58');

    // Update quantity of first item
    cy.get('[data-cy=cart-item]')
      .first()
      .find('[data-cy=quantity-input]')
      .clear()
      .type('2');
    cy.get('[data-cy=cart-item]')
      .first()
      .find('[data-cy=quantity-input]')
      .blur();

    // Verify updated totals
    cy.get('[data-cy=subtotal]').should('contain', '$99.97'); // 29.99*2 + 39.99
    cy.get('[data-cy=tax]').should('contain', '$8.00'); // 8% tax
    cy.get('[data-cy=total]').should('contain', '$107.97');

    // Proceed to checkout
    cy.get('[data-cy=checkout-btn]').click();

    // Verify checkout page loads
    cy.url().should('include', '/checkout');
    cy.get('[data-cy=checkout-title]').should('contain', 'Checkout');

    // Fill shipping information
    cy.get('[data-cy=shipping-first-name]').type('John');
    cy.get('[data-cy=shipping-last-name]').type('Doe');
    cy.get('[data-cy=shipping-email]').type('john.doe@example.com');
    cy.get('[data-cy=shipping-address]').type('123 Main St');
    cy.get('[data-cy=shipping-city]').type('Anytown');
    cy.get('[data-cy=shipping-zip]').type('12345');

    // Fill payment information
    cy.get('[data-cy=payment-card-number]').type('4111111111111111');
    cy.get('[data-cy=payment-expiry]').type('12/25');
    cy.get('[data-cy=payment-cvv]').type('123');

    // Verify order summary
    cy.get('[data-cy=order-summary]').should('contain', 'Test Product 1');
    cy.get('[data-cy=order-summary]').should('contain', 'Test Product 2');
    cy.get('[data-cy=order-summary]').should('contain', 'Qty: 2');
    cy.get('[data-cy=order-summary]').should('contain', 'Qty: 1');

    // Submit order
    cy.get('[data-cy=place-order-btn]').click();

    // Wait for order creation API call
    cy.wait('@createOrder');

    // Verify order success message
    cy.get('[data-cy=order-success]').should('be.visible');
    cy.get('[data-cy=order-success]').should(
      'contain',
      'Order placed successfully!'
    );

    // Verify cart is cleared
    cy.get('[data-cy=cart-count]').should('contain', '0');

    // Verify redirect to products page
    cy.url().should('not.include', '/checkout');
  });

  it('should handle empty cart checkout attempt', () => {
    // Navigate directly to checkout with empty cart
    cy.visit('/checkout');

    // Verify empty cart message
    cy.get('[data-cy=empty-cart]').should('be.visible');
    cy.get('[data-cy=empty-cart]').should('contain', 'Your cart is empty');
    cy.get('[data-cy=empty-cart]').should(
      'contain',
      'Add some products to get started!'
    );

    // Verify continue shopping button
    cy.get('[data-cy=shop-btn]').should('be.visible');
    cy.get('[data-cy=shop-btn]').click();

    // Verify redirect to products page
    cy.url().should('not.include', '/checkout');
  });

  it('should handle product detail page and add to cart', () => {
    // Wait for products to load
    cy.wait('@getProducts');

    // Click on first product to view details
    cy.get('[data-cy=product-card]')
      .first()
      .find('[data-cy=view-details-btn]')
      .click();

    // Verify product detail page loads
    cy.url().should('include', '/product/1');
    cy.get('[data-cy=product-detail]').should('be.visible');
    cy.get('[data-cy=product-name]').should('contain', 'Test Product 1');
    cy.get('[data-cy=product-price]').should('contain', '$29.99');
    cy.get('[data-cy=product-stock]').should('contain', '10 in stock');

    // Update quantity
    cy.get('[data-cy=quantity-input]').clear().type('3');

    // Add to cart
    cy.get('[data-cy=add-to-cart-btn]').click();

    // Verify cart count updates
    cy.get('[data-cy=cart-count]').should('contain', '3');

    // Go back to products
    cy.get('[data-cy=back-btn]').click();

    // Verify back to products page
    cy.url().should('not.include', '/product/');
  });

  it('should handle search functionality', () => {
    // Wait for products to load
    cy.wait('@getProducts');

    // Search for a product
    cy.get('[data-cy=search-input]').type('Test Product 1');
    cy.get('[data-cy=search-input]').type('{enter}');

    // Verify search results
    cy.get('[data-cy=product-card]').should('have.length', 1);
    cy.get('[data-cy=product-card]').should('contain', 'Test Product 1');
    cy.get('[data-cy=product-card]').should('not.contain', 'Test Product 2');

    // Clear search
    cy.get('[data-cy=search-input]').clear();
    cy.get('[data-cy=search-input]').type('{enter}');

    // Verify all products are shown again
    cy.get('[data-cy=product-card]').should('have.length', 2);
  });

  it('should handle out of stock products', () => {
    // Mock product with zero stock
    cy.intercept('GET', '**/api/catalog/products', {
      statusCode: 200,
      body: [
        {
          id: '3',
          sku: 'OUT-001',
          name: 'Out of Stock Product',
          description: 'This product is out of stock',
          price: 19.99,
          stock: 0,
          imageUrl: 'https://via.placeholder.com/300x200?text=Out+of+Stock',
          createdAt: '2024-01-01T00:00:00Z',
          updatedAt: '2024-01-01T00:00:00Z',
        },
      ],
    }).as('getOutOfStockProducts');

    cy.visit('/');
    cy.wait('@getOutOfStockProducts');

    // Verify out of stock product is displayed
    cy.get('[data-cy=product-card]').should('contain', 'Out of Stock Product');
    cy.get('[data-cy=product-card]').should('contain', '0 in stock');

    // Verify add to cart button is disabled
    cy.get('[data-cy=add-to-cart-btn]').should('be.disabled');
    cy.get('[data-cy=add-to-cart-btn]').should('contain', 'Out of Stock');
  });
});
