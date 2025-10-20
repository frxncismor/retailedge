import { Product, Order, OrderStatus, Customer } from './shared-models';

describe('shared-models', () => {
  it('should export Product interface', () => {
    const product: Product = {
      id: '1',
      name: 'Test Product',
      description: 'Test Description',
      price: 99.99,
      category: 'Test',
      inStock: true,
      createdAt: new Date(),
      updatedAt: new Date(),
    };
    expect(product.id).toBe('1');
    expect(product.name).toBe('Test Product');
  });

  it('should export Order interface', () => {
    const order: Order = {
      id: '1',
      customerId: 'customer-1',
      products: [],
      total: 0,
      status: OrderStatus.PENDING,
      shippingAddress: {
        id: '1',
        street: '123 Main St',
        city: 'Test City',
        state: 'TS',
        zipCode: '12345',
        country: 'US',
        isDefault: true,
      },
      createdAt: new Date(),
      updatedAt: new Date(),
    };
    expect(order.id).toBe('1');
    expect(order.status).toBe(OrderStatus.PENDING);
  });

  it('should export Customer interface', () => {
    const customer: Customer = {
      id: '1',
      email: 'test@example.com',
      firstName: 'John',
      lastName: 'Doe',
      addresses: [],
      createdAt: new Date(),
      updatedAt: new Date(),
    };
    expect(customer.email).toBe('test@example.com');
    expect(customer.firstName).toBe('John');
  });
});
