// Generated API types for Orders Service
// Run 'npm run generate:api-types:orders' to regenerate from OpenAPI spec

export interface Order {
  id?: string;
  customerId: string;
  orderDate?: string;
  status: OrderStatus;
  totalAmount?: number;
  items?: OrderItem[];
  createdAt?: string;
  updatedAt?: string;
}

export interface OrderItem {
  id?: string;
  productId: string;
  quantity: number;
  unitPrice: number;
  totalPrice?: number;
}

export enum OrderStatus {
  PENDING = 'PENDING',
  CONFIRMED = 'CONFIRMED',
  SHIPPED = 'SHIPPED',
  DELIVERED = 'DELIVERED',
  CANCELLED = 'CANCELLED',
}

export interface OrderRequest {
  customerId: string;
  items: OrderItemRequest[];
}

export interface OrderItemRequest {
  productId: string;
  quantity: number;
  unitPrice: number;
}

export interface OrderResponse {
  id: string;
  customerId: string;
  orderDate: string;
  status: OrderStatus;
  totalAmount: number;
  items: OrderItemResponse[];
  createdAt: string;
  updatedAt: string;
}

export interface OrderItemResponse {
  id: string;
  productId: string;
  quantity: number;
  unitPrice: number;
  totalPrice: number;
}

export interface OrdersApiResponse<T> {
  data: T;
  message?: string;
  success: boolean;
}

export interface OrdersPaginatedResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
  first: boolean;
  last: boolean;
}
