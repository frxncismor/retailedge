export interface Product {
  id: string;
  sku: string;
  name: string;
  description: string;
  price: number;
  stock: number;
  created_at: string;
  updated_at: string;
}

export interface ProductFormData {
  sku: string;
  name: string;
  description: string;
  price: number;
  stock: number;
}
