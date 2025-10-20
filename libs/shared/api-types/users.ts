// Generated API types for Users Service
// Run 'npm run generate:api-types:users' to regenerate from OpenAPI spec

export interface Customer {
  id?: string;
  firstName: string;
  lastName: string;
  email: string;
  password?: string;
  address?: Address;
  createdAt?: string;
  updatedAt?: string;
}

export interface Address {
  street: string;
  city: string;
  state: string;
  zipCode: string;
  country: string;
}

export interface CustomerRequest {
  firstName: string;
  lastName: string;
  email: string;
  password: string;
  address?: Address;
}

export interface CustomerResponse {
  id: string;
  firstName: string;
  lastName: string;
  email: string;
  address?: Address;
  createdAt: string;
  updatedAt: string;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface LoginResponse {
  token: string;
  customer: CustomerResponse;
  expiresIn: number;
}

export interface UsersApiResponse<T> {
  data: T;
  message?: string;
  success: boolean;
}

export interface UsersPaginatedResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
  first: boolean;
  last: boolean;
}
