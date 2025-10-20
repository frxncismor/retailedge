import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import '@testing-library/jest-dom';
import ProductList from './ProductList';
import { productService } from '../services/productService';
import { Product } from '@retailedge/api-types';

// Mock the product service
jest.mock('../services/productService');
const mockProductService = productService as jest.Mocked<typeof productService>;

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

describe('ProductList', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  it('renders loading state initially', () => {
    mockProductService.getProducts.mockImplementation(
      () => new Promise(() => {})
    ); // Never resolves

    render(<ProductList />);

    expect(screen.getByText('Loading products...')).toBeInTheDocument();
  });

  it('renders products table when data is loaded', async () => {
    mockProductService.getProducts.mockResolvedValue(mockProducts);

    render(<ProductList />);

    await waitFor(() => {
      expect(screen.getByText('Product Management')).toBeInTheDocument();
      expect(screen.getByText('Add New Product')).toBeInTheDocument();
    });

    // Check table headers
    expect(screen.getByText('SKU')).toBeInTheDocument();
    expect(screen.getByText('Name')).toBeInTheDocument();
    expect(screen.getByText('Description')).toBeInTheDocument();
    expect(screen.getByText('Price')).toBeInTheDocument();
    expect(screen.getByText('Stock')).toBeInTheDocument();
    expect(screen.getByText('Actions')).toBeInTheDocument();

    // Check product data
    expect(screen.getByText('TEST-001')).toBeInTheDocument();
    expect(screen.getByText('Test Product 1')).toBeInTheDocument();
    expect(screen.getByText('Test Description 1')).toBeInTheDocument();
    expect(screen.getByText('$29.99')).toBeInTheDocument();
    expect(screen.getByText('10')).toBeInTheDocument();

    expect(screen.getByText('TEST-002')).toBeInTheDocument();
    expect(screen.getByText('Test Product 2')).toBeInTheDocument();
    expect(screen.getByText('Test Description 2')).toBeInTheDocument();
    expect(screen.getByText('$39.99')).toBeInTheDocument();
    expect(screen.getByText('5')).toBeInTheDocument();
  });

  it('renders error message when API fails', async () => {
    mockProductService.getProducts.mockRejectedValue(new Error('API Error'));

    render(<ProductList />);

    await waitFor(() => {
      expect(
        screen.getByText('Error: Failed to load products')
      ).toBeInTheDocument();
    });
  });

  it('shows no products message when empty array is returned', async () => {
    mockProductService.getProducts.mockResolvedValue([]);

    render(<ProductList />);

    await waitFor(() => {
      expect(
        screen.getByText('No products found. Add your first product!')
      ).toBeInTheDocument();
    });
  });

  it('opens add product form when Add New Product button is clicked', async () => {
    mockProductService.getProducts.mockResolvedValue(mockProducts);

    render(<ProductList />);

    await waitFor(() => {
      expect(screen.getByText('Product Management')).toBeInTheDocument();
    });

    const addButton = screen.getByText('Add New Product');
    fireEvent.click(addButton);

    expect(screen.getAllByText('Add New Product')).toHaveLength(2); // Button and form title
    expect(screen.getByLabelText('SKU *')).toBeInTheDocument();
    expect(screen.getByLabelText('Product Name *')).toBeInTheDocument();
    expect(screen.getByLabelText('Description')).toBeInTheDocument();
    expect(screen.getByLabelText('Price *')).toBeInTheDocument();
    expect(screen.getByLabelText('Stock *')).toBeInTheDocument();
  });

  it('opens edit product form when Edit button is clicked', async () => {
    mockProductService.getProducts.mockResolvedValue(mockProducts);

    render(<ProductList />);

    await waitFor(() => {
      expect(screen.getByText('Test Product 1')).toBeInTheDocument();
    });

    const editButtons = screen.getAllByText('Edit');
    fireEvent.click(editButtons[0]);

    expect(screen.getByText('Edit Product')).toBeInTheDocument();
    expect(screen.getByDisplayValue('TEST-001')).toBeInTheDocument();
    expect(screen.getByDisplayValue('Test Product 1')).toBeInTheDocument();
    expect(screen.getByDisplayValue('Test Description 1')).toBeInTheDocument();
    expect(screen.getByDisplayValue('29.99')).toBeInTheDocument();
    expect(screen.getByDisplayValue('10')).toBeInTheDocument();
  });

  it('calls delete service when Delete button is clicked and confirmed', async () => {
    mockProductService.getProducts.mockResolvedValue(mockProducts);
    mockProductService.deleteProduct.mockResolvedValue();

    // Mock window.confirm
    const confirmSpy = jest.spyOn(globalThis, 'confirm').mockReturnValue(true);

    render(<ProductList />);

    await waitFor(() => {
      expect(screen.getByText('Test Product 1')).toBeInTheDocument();
    });

    const deleteButtons = screen.getAllByText('Delete');
    fireEvent.click(deleteButtons[0]);

    expect(confirmSpy).toHaveBeenCalledWith(
      'Are you sure you want to delete this product?'
    );
    expect(mockProductService.deleteProduct).toHaveBeenCalledWith('1');

    confirmSpy.mockRestore();
  });

  it('does not call delete service when Delete button is clicked but not confirmed', async () => {
    mockProductService.getProducts.mockResolvedValue(mockProducts);

    // Mock window.confirm to return false
    const confirmSpy = jest.spyOn(globalThis, 'confirm').mockReturnValue(false);

    render(<ProductList />);

    await waitFor(() => {
      expect(screen.getByText('Test Product 1')).toBeInTheDocument();
    });

    const deleteButtons = screen.getAllByText('Delete');
    fireEvent.click(deleteButtons[0]);

    expect(confirmSpy).toHaveBeenCalledWith(
      'Are you sure you want to delete this product?'
    );
    expect(mockProductService.deleteProduct).not.toHaveBeenCalled();

    confirmSpy.mockRestore();
  });

  it('shows low stock styling for products with stock < 10', async () => {
    mockProductService.getProducts.mockResolvedValue(mockProducts);

    render(<ProductList />);

    await waitFor(() => {
      expect(screen.getByText('5')).toBeInTheDocument();
    });

    // Find the stock cell for the second product (stock: 5)
    const stockCells = screen.getAllByText(/\d+/);
    const lowStockCell = stockCells.find((cell) => cell.textContent === '5');
    expect(lowStockCell).toHaveClass('low-stock');
  });

  it('handles form submission for adding new product', async () => {
    mockProductService.getProducts.mockResolvedValue([]);
    mockProductService.createProduct.mockResolvedValue(mockProducts[0]);

    render(<ProductList />);

    await waitFor(() => {
      expect(screen.getByText('Add New Product')).toBeInTheDocument();
    });

    // Click Add New Product button
    fireEvent.click(screen.getByText('Add New Product'));

    // Fill form
    fireEvent.change(screen.getByLabelText('SKU *'), {
      target: { value: 'NEW-001' },
    });
    fireEvent.change(screen.getByLabelText('Product Name *'), {
      target: { value: 'New Product' },
    });
    fireEvent.change(screen.getByLabelText('Description'), {
      target: { value: 'New Description' },
    });
    fireEvent.change(screen.getByLabelText('Price *'), {
      target: { value: '49.99' },
    });
    fireEvent.change(screen.getByLabelText('Stock *'), {
      target: { value: '15' },
    });

    // Submit form
    fireEvent.click(screen.getByText('Create Product'));

    expect(mockProductService.createProduct).toHaveBeenCalledWith({
      sku: 'NEW-001',
      name: 'New Product',
      description: 'New Description',
      price: 49.99,
      stock: 15,
    });
  });
});
