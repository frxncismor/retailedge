import { TestBed } from '@angular/core/testing';
import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';
import { ProductService } from './product.service';
import { Product } from '@retailedge/api-types';

describe('ProductService', () => {
  let service: ProductService;
  let httpMock: HttpTestingController;

  const mockProducts: Product[] = [
    {
      id: '1',
      sku: 'TEST-001',
      name: 'Test Product 1',
      description: 'Test Description 1',
      price: 29.99,
      stock: 10,
      imageUrl: 'https://example.com/image1.jpg',
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
      imageUrl: 'https://example.com/image2.jpg',
      createdAt: '2024-01-02T00:00:00Z',
      updatedAt: '2024-01-02T00:00:00Z',
    },
  ];

  const mockProduct: Product = mockProducts[0];

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [ProductService],
    });
    service = TestBed.inject(ProductService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('getProducts', () => {
    it('should return products from API', () => {
      service.getProducts().subscribe((products) => {
        expect(products).toEqual(mockProducts);
        expect(products.length).toBe(2);
      });

      const req = httpMock.expectOne(`${service['apiUrl']}/products`);
      expect(req.request.method).toBe('GET');
      req.flush(mockProducts);
    });

    it('should handle API errors', () => {
      const errorMessage = 'Server error';

      service.getProducts().subscribe({
        next: () => fail('should have failed'),
        error: (error) => {
          expect(error.status).toBe(500);
          expect(error.statusText).toBe(errorMessage);
        },
      });

      const req = httpMock.expectOne(`${service['apiUrl']}/products`);
      req.flush(errorMessage, { status: 500, statusText: errorMessage });
    });
  });

  describe('getProduct', () => {
    it('should return single product from API', () => {
      const productId = '1';

      service.getProduct(productId).subscribe((product) => {
        expect(product).toEqual(mockProduct);
        expect(product.id).toBe(productId);
      });

      const req = httpMock.expectOne(
        `${service['apiUrl']}/products/${productId}`
      );
      expect(req.request.method).toBe('GET');
      req.flush(mockProduct);
    });

    it('should handle product not found', () => {
      const productId = '999';

      service.getProduct(productId).subscribe({
        next: () => fail('should have failed'),
        error: (error) => {
          expect(error.status).toBe(404);
        },
      });

      const req = httpMock.expectOne(
        `${service['apiUrl']}/products/${productId}`
      );
      req.flush('Product not found', { status: 404, statusText: 'Not Found' });
    });
  });

  describe('searchProducts', () => {
    it('should return filtered products from API', () => {
      const searchQuery = 'test';
      const expectedUrl = `${service['apiUrl']}/products/search?q=${encodeURIComponent(searchQuery)}`;

      service.searchProducts(searchQuery).subscribe((products) => {
        expect(products).toEqual(mockProducts);
      });

      const req = httpMock.expectOne(expectedUrl);
      expect(req.request.method).toBe('GET');
      req.flush(mockProducts);
    });

    it('should handle search with special characters', () => {
      const searchQuery = 'test & special';
      const expectedUrl = `${service['apiUrl']}/products/search?q=${encodeURIComponent(searchQuery)}`;

      service.searchProducts(searchQuery).subscribe((products) => {
        expect(products).toEqual([]);
      });

      const req = httpMock.expectOne(expectedUrl);
      expect(req.request.method).toBe('GET');
      req.flush([]);
    });

    it('should handle search API errors', () => {
      const searchQuery = 'test';

      service.searchProducts(searchQuery).subscribe({
        next: () => fail('should have failed'),
        error: (error) => {
          expect(error.status).toBe(500);
        },
      });

      const req = httpMock.expectOne(
        `${service['apiUrl']}/products/search?q=${encodeURIComponent(searchQuery)}`
      );
      req.flush('Server error', {
        status: 500,
        statusText: 'Internal Server Error',
      });
    });
  });
});
