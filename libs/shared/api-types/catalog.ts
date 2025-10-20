// Generated API types for Catalog Service
// Run 'npm run generate:api-types:catalog' to regenerate from OpenAPI spec

export interface Product {
  id?: string;
  sku: string;
  name: string;
  description?: string;
  price: number;
  stock: number;
  imageUrl?: string;
  createdAt?: string;
  updatedAt?: string;
}

export interface ProductRequest {
  sku: string;
  name: string;
  description?: string;
  price: number;
  stock: number;
  imageUrl?: string;
}

export interface ProductResponse {
  id: string;
  sku: string;
  name: string;
  description?: string;
  price: number;
  stock: number;
  imageUrl?: string;
  createdAt: string;
  updatedAt: string;
}

export interface CatalogApiResponse<T> {
  data: T;
  message?: string;
  success: boolean;
}

export interface CatalogPaginatedResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
  first: boolean;
  last: boolean;
}
