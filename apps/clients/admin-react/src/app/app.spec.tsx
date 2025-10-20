import { render } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';

import App from './app';

// Mock the productService to avoid fetch errors
jest.mock('../services/productService', () => ({
  productService: {
    getProducts: jest.fn().mockResolvedValue([]),
    createProduct: jest.fn(),
    updateProduct: jest.fn(),
    deleteProduct: jest.fn(),
  },
}));

describe('App', () => {
  it('should render successfully', () => {
    const { baseElement } = render(
      <BrowserRouter>
        <App />
      </BrowserRouter>
    );
    expect(baseElement).toBeTruthy();
  });

  it('should have RetailEdge Admin as the title', () => {
    const { getByText } = render(
      <BrowserRouter>
        <App />
      </BrowserRouter>
    );
    expect(getByText('RetailEdge Admin')).toBeTruthy();
  });
});
