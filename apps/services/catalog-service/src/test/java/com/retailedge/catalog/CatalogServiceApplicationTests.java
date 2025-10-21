package com.retailedge.catalog;

import com.retailedge.catalog.dto.ProductRequest;
import com.retailedge.catalog.dto.ProductResponse;
import com.retailedge.catalog.entity.Product;
import com.retailedge.catalog.repository.ProductRepository;
import com.retailedge.catalog.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class CatalogServiceApplicationTests {

    @Autowired
    private ProductService productService;

    @MockBean
    private ProductRepository productRepository;

    @Test
    void contextLoads() {
        assertNotNull(productService);
    }

    @Test
    void testGetAllProducts() {
        // Given
        Product product1 = createTestProduct(1L, "Test Product 1", "Description 1", new BigDecimal("29.99"), "Electronics");
        Product product2 = createTestProduct(2L, "Test Product 2", "Description 2", new BigDecimal("39.99"), "Books");
        List<Product> products = Arrays.asList(product1, product2);

        when(productRepository.findAll()).thenReturn(products);

        // When
        List<ProductResponse> result = productService.getAllProducts();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Test Product 1", result.get(0).getName());
        assertEquals("Test Product 2", result.get(1).getName());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testGetProductById() {
        // Given
        Long productId = 1L;
        Product product = createTestProduct(productId, "Test Product", "Description", new BigDecimal("29.99"), "Electronics");
        
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // When
        Optional<ProductResponse> result = productService.getProductById(productId);

        // Then
        assertTrue(result.isPresent());
        assertEquals("Test Product", result.get().getName());
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    void testGetProductByIdNotFound() {
        // Given
        Long productId = 999L;
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // When
        Optional<ProductResponse> result = productService.getProductById(productId);

        // Then
        assertFalse(result.isPresent());
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    void testCreateProduct() {
        // Given
        ProductRequest productRequest = new ProductRequest("New Product", "New Description", new BigDecimal("49.99"), "Electronics");
        productRequest.setInStock(true);
        productRequest.setImageUrl("http://example.com/image.jpg");
        Product savedProduct = createTestProduct(1L, "New Product", "New Description", new BigDecimal("49.99"), "Electronics");
        
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        // When
        ProductResponse result = productService.createProduct(productRequest);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("New Product", result.getName());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testUpdateProduct() {
        // Given
        Long productId = 1L;
        Product existingProduct = createTestProduct(productId, "Test Product", "Description", new BigDecimal("29.99"), "Electronics");
        ProductRequest updatedProductRequest = new ProductRequest("Updated Product", "Updated Description", new BigDecimal("39.99"), "Electronics");
        updatedProductRequest.setInStock(true);
        updatedProductRequest.setImageUrl("http://example.com/image.jpg");
        Product updatedProduct = createTestProduct(productId, "Updated Product", "Updated Description", new BigDecimal("39.99"), "Electronics");
        
        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);

        // When
        Optional<ProductResponse> result = productService.updateProduct(productId, updatedProductRequest);

        // Then
        assertTrue(result.isPresent());
        assertEquals("Updated Product", result.get().getName());
        assertEquals("Updated Description", result.get().getDescription());
        assertEquals(new BigDecimal("39.99"), result.get().getPrice());
        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testUpdateProductNotFound() {
        // Given
        Long productId = 999L;
        ProductRequest productRequest = new ProductRequest("Test Product", "Description", new BigDecimal("29.99"), "Electronics");
        productRequest.setInStock(true);
        productRequest.setImageUrl("http://example.com/image.jpg");
        
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // When
        Optional<ProductResponse> result = productService.updateProduct(productId, productRequest);

        // Then
        assertFalse(result.isPresent());
        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void testDeleteProduct() {
        // Given
        Long productId = 1L;
        Product product = createTestProduct(productId, "Test Product", "Description", new BigDecimal("29.99"), "Electronics");
        
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        doNothing().when(productRepository).deleteById(productId);

        // When
        boolean result = productService.deleteProduct(productId);

        // Then
        assertTrue(result);
        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(1)).deleteById(productId);
    }

    @Test
    void testDeleteProductNotFound() {
        // Given
        Long productId = 999L;
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // When
        boolean result = productService.deleteProduct(productId);

        // Then
        assertFalse(result);
        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, never()).deleteById(anyLong());
    }

    @Test
    void testSearchProducts() {
        // Given
        String searchQuery = "test";
        Product product1 = createTestProduct(1L, "Test Product 1", "Description 1", new BigDecimal("29.99"), "Electronics");
        Product product2 = createTestProduct(2L, "Test Product 2", "Description 2", new BigDecimal("39.99"), "Books");
        List<Product> products = Arrays.asList(product1, product2);
        
        when(productRepository.findByNameContainingIgnoreCase(searchQuery)).thenReturn(products);

        // When
        List<ProductResponse> result = productService.searchProducts(searchQuery);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(p -> p.getName().contains("Test Product 1")));
        assertTrue(result.stream().anyMatch(p -> p.getName().contains("Test Product 2")));
        verify(productRepository, times(1)).findByNameContainingIgnoreCase(searchQuery);
    }

    @Test
    void testSearchProductsEmptyResult() {
        // Given
        String searchQuery = "nonexistent";
        when(productRepository.findByNameContainingIgnoreCase(searchQuery)).thenReturn(Arrays.asList());

        // When
        List<ProductResponse> result = productService.searchProducts(searchQuery);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(productRepository, times(1)).findByNameContainingIgnoreCase(searchQuery);
    }

    private Product createTestProduct(Long id, String name, String description, BigDecimal price, String category) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setCategory(category);
        product.setInStock(true);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        return product;
    }
}