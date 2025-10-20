package com.retailedge.catalog.service;

import com.retailedge.catalog.dto.ProductRequest;
import com.retailedge.catalog.dto.ProductResponse;
import com.retailedge.catalog.entity.Product;
import com.retailedge.catalog.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    
    @Mock
    private ProductRepository productRepository;
    
    @InjectMocks
    private ProductService productService;
    
    private Product testProduct;
    private ProductRequest testProductRequest;
    private ProductResponse expectedResponse;
    
    @BeforeEach
    void setUp() {
        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("Test Product");
        testProduct.setDescription("Test Description");
        testProduct.setPrice(new BigDecimal("99.99"));
        testProduct.setCategory("Electronics");
        testProduct.setInStock(true);
        testProduct.setImageUrl("http://example.com/image.jpg");
        testProduct.setCreatedAt(LocalDateTime.now());
        testProduct.setUpdatedAt(LocalDateTime.now());
        
        testProductRequest = new ProductRequest();
        testProductRequest.setName("Test Product");
        testProductRequest.setDescription("Test Description");
        testProductRequest.setPrice(new BigDecimal("99.99"));
        testProductRequest.setCategory("Electronics");
        testProductRequest.setInStock(true);
        testProductRequest.setImageUrl("http://example.com/image.jpg");
        
        expectedResponse = new ProductResponse(
                1L, "Test Product", "Test Description", new BigDecimal("99.99"),
                "Electronics", true, "http://example.com/image.jpg",
                LocalDateTime.now(), LocalDateTime.now()
        );
    }
    
    @Test
    void getAllProducts_ShouldReturnAllProducts() {
        // Given
        List<Product> products = Arrays.asList(testProduct);
        when(productRepository.findAll()).thenReturn(products);
        
        // When
        List<ProductResponse> result = productService.getAllProducts();
        
        // Then
        assertEquals(1, result.size());
        assertEquals("Test Product", result.get(0).getName());
        verify(productRepository).findAll();
    }
    
    @Test
    void getAllProducts_WithPagination_ShouldReturnPagedProducts() {
        // Given
        List<Product> products = Arrays.asList(testProduct);
        Page<Product> productPage = new PageImpl<>(products);
        when(productRepository.findAll(any(Pageable.class))).thenReturn(productPage);
        
        // When
        Page<ProductResponse> result = productService.getAllProducts(Pageable.unpaged());
        
        // Then
        assertEquals(1, result.getContent().size());
        assertEquals("Test Product", result.getContent().get(0).getName());
        verify(productRepository).findAll(any(Pageable.class));
    }
    
    @Test
    void getProductById_WhenProductExists_ShouldReturnProduct() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        
        // When
        Optional<ProductResponse> result = productService.getProductById(1L);
        
        // Then
        assertTrue(result.isPresent());
        assertEquals("Test Product", result.get().getName());
        verify(productRepository).findById(1L);
    }
    
    @Test
    void getProductById_WhenProductNotExists_ShouldReturnEmpty() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        
        // When
        Optional<ProductResponse> result = productService.getProductById(1L);
        
        // Then
        assertFalse(result.isPresent());
        verify(productRepository).findById(1L);
    }
    
    @Test
    void getProductsByCategory_ShouldReturnProductsInCategory() {
        // Given
        List<Product> products = Arrays.asList(testProduct);
        when(productRepository.findByCategory("Electronics")).thenReturn(products);
        
        // When
        List<ProductResponse> result = productService.getProductsByCategory("Electronics");
        
        // Then
        assertEquals(1, result.size());
        assertEquals("Electronics", result.get(0).getCategory());
        verify(productRepository).findByCategory("Electronics");
    }
    
    @Test
    void getAvailableProducts_ShouldReturnInStockProducts() {
        // Given
        List<Product> products = Arrays.asList(testProduct);
        when(productRepository.findByInStockTrue()).thenReturn(products);
        
        // When
        List<ProductResponse> result = productService.getAvailableProducts();
        
        // Then
        assertEquals(1, result.size());
        assertTrue(result.get(0).getInStock());
        verify(productRepository).findByInStockTrue();
    }
    
    @Test
    void searchProducts_ShouldReturnMatchingProducts() {
        // Given
        List<Product> products = Arrays.asList(testProduct);
        when(productRepository.findByNameContainingIgnoreCase("Test")).thenReturn(products);
        
        // When
        List<ProductResponse> result = productService.searchProducts("Test");
        
        // Then
        assertEquals(1, result.size());
        assertEquals("Test Product", result.get(0).getName());
        verify(productRepository).findByNameContainingIgnoreCase("Test");
    }
    
    @Test
    void createProduct_WhenValidRequest_ShouldCreateProduct() {
        // Given
        when(productRepository.existsByNameAndIdNot("Test Product", 0L)).thenReturn(false);
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);
        
        // When
        ProductResponse result = productService.createProduct(testProductRequest);
        
        // Then
        assertEquals("Test Product", result.getName());
        verify(productRepository).existsByNameAndIdNot("Test Product", 0L);
        verify(productRepository).save(any(Product.class));
    }
    
    @Test
    void createProduct_WhenProductNameExists_ShouldThrowException() {
        // Given
        when(productRepository.existsByNameAndIdNot("Test Product", 0L)).thenReturn(true);
        
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            productService.createProduct(testProductRequest);
        });
        verify(productRepository).existsByNameAndIdNot("Test Product", 0L);
        verify(productRepository, never()).save(any(Product.class));
    }
    
    @Test
    void updateProduct_WhenProductExists_ShouldUpdateProduct() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(productRepository.existsByNameAndIdNot("Test Product", 1L)).thenReturn(false);
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);
        
        // When
        Optional<ProductResponse> result = productService.updateProduct(1L, testProductRequest);
        
        // Then
        assertTrue(result.isPresent());
        assertEquals("Test Product", result.get().getName());
        verify(productRepository).findById(1L);
        verify(productRepository).existsByNameAndIdNot("Test Product", 1L);
        verify(productRepository).save(any(Product.class));
    }
    
    @Test
    void updateProduct_WhenProductNotExists_ShouldReturnEmpty() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        
        // When
        Optional<ProductResponse> result = productService.updateProduct(1L, testProductRequest);
        
        // Then
        assertFalse(result.isPresent());
        verify(productRepository).findById(1L);
        verify(productRepository, never()).save(any(Product.class));
    }
    
    @Test
    void deleteProduct_WhenProductExists_ShouldDeleteProduct() {
        // Given
        when(productRepository.existsById(1L)).thenReturn(true);
        
        // When
        boolean result = productService.deleteProduct(1L);
        
        // Then
        assertTrue(result);
        verify(productRepository).existsById(1L);
        verify(productRepository).deleteById(1L);
    }
    
    @Test
    void deleteProduct_WhenProductNotExists_ShouldReturnFalse() {
        // Given
        when(productRepository.existsById(1L)).thenReturn(false);
        
        // When
        boolean result = productService.deleteProduct(1L);
        
        // Then
        assertFalse(result);
        verify(productRepository).existsById(1L);
        verify(productRepository, never()).deleteById(anyLong());
    }
}
