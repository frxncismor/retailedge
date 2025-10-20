package com.retailedge.catalog.controller;

import com.retailedge.catalog.dto.ProductRequest;
import com.retailedge.catalog.dto.ProductResponse;
import com.retailedge.catalog.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {
    
    @Mock
    private ProductService productService;
    
    @InjectMocks
    private ProductController productController;
    
    private ProductRequest testProductRequest;
    private ProductResponse testProductResponse;
    
    @BeforeEach
    void setUp() {
        testProductRequest = new ProductRequest();
        testProductRequest.setName("Test Product");
        testProductRequest.setDescription("Test Description");
        testProductRequest.setPrice(new BigDecimal("99.99"));
        testProductRequest.setCategory("Electronics");
        testProductRequest.setInStock(true);
        testProductRequest.setImageUrl("http://example.com/image.jpg");
        
        testProductResponse = new ProductResponse(
                1L, "Test Product", "Test Description", new BigDecimal("99.99"),
                "Electronics", true, "http://example.com/image.jpg",
                LocalDateTime.now(), LocalDateTime.now()
        );
    }
    
    @Test
    void getAllProducts_ShouldReturnAllProducts() {
        // Given
        List<ProductResponse> products = Arrays.asList(testProductResponse);
        when(productService.getAllProducts()).thenReturn(products);
        
        // When
        ResponseEntity<List<ProductResponse>> response = productController.getAllProducts();
        
        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("Test Product", response.getBody().get(0).getName());
        verify(productService).getAllProducts();
    }
    
    @Test
    void getAllProducts_WithPagination_ShouldReturnPagedProducts() {
        // Given
        List<ProductResponse> products = Arrays.asList(testProductResponse);
        Page<ProductResponse> productPage = new PageImpl<>(products);
        when(productService.getAllProducts(any(Pageable.class))).thenReturn(productPage);
        
        // When
        ResponseEntity<Page<ProductResponse>> response = productController.getAllProducts(Pageable.unpaged());
        
        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getContent().size());
        assertEquals("Test Product", response.getBody().getContent().get(0).getName());
        verify(productService).getAllProducts(any(Pageable.class));
    }
    
    @Test
    void getProductById_WhenProductExists_ShouldReturnProduct() {
        // Given
        when(productService.getProductById(1L)).thenReturn(Optional.of(testProductResponse));
        
        // When
        ResponseEntity<ProductResponse> response = productController.getProductById(1L);
        
        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Test Product", response.getBody().getName());
        verify(productService).getProductById(1L);
    }
    
    @Test
    void getProductById_WhenProductNotExists_ShouldReturnNotFound() {
        // Given
        when(productService.getProductById(1L)).thenReturn(Optional.empty());
        
        // When
        ResponseEntity<ProductResponse> response = productController.getProductById(1L);
        
        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(productService).getProductById(1L);
    }
    
    @Test
    void getProductsByCategory_ShouldReturnProductsInCategory() {
        // Given
        List<ProductResponse> products = Arrays.asList(testProductResponse);
        when(productService.getProductsByCategory("Electronics")).thenReturn(products);
        
        // When
        ResponseEntity<List<ProductResponse>> response = productController.getProductsByCategory("Electronics");
        
        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("Electronics", response.getBody().get(0).getCategory());
        verify(productService).getProductsByCategory("Electronics");
    }
    
    @Test
    void getAvailableProducts_ShouldReturnInStockProducts() {
        // Given
        List<ProductResponse> products = Arrays.asList(testProductResponse);
        when(productService.getAvailableProducts()).thenReturn(products);
        
        // When
        ResponseEntity<List<ProductResponse>> response = productController.getAvailableProducts();
        
        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertTrue(response.getBody().get(0).getInStock());
        verify(productService).getAvailableProducts();
    }
    
    @Test
    void searchProducts_ShouldReturnMatchingProducts() {
        // Given
        List<ProductResponse> products = Arrays.asList(testProductResponse);
        when(productService.searchProducts("Test")).thenReturn(products);
        
        // When
        ResponseEntity<List<ProductResponse>> response = productController.searchProducts("Test");
        
        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("Test Product", response.getBody().get(0).getName());
        verify(productService).searchProducts("Test");
    }
    
    @Test
    void createProduct_WhenValidRequest_ShouldCreateProduct() {
        // Given
        when(productService.createProduct(any(ProductRequest.class))).thenReturn(testProductResponse);
        
        // When
        ResponseEntity<ProductResponse> response = productController.createProduct(testProductRequest);
        
        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Test Product", response.getBody().getName());
        verify(productService).createProduct(testProductRequest);
    }
    
    @Test
    void createProduct_WhenProductNameExists_ShouldReturnConflict() {
        // Given
        when(productService.createProduct(any(ProductRequest.class)))
                .thenThrow(new IllegalArgumentException("Product with same name already exists"));
        
        // When
        ResponseEntity<ProductResponse> response = productController.createProduct(testProductRequest);
        
        // Then
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNull(response.getBody());
        verify(productService).createProduct(testProductRequest);
    }
    
    @Test
    void updateProduct_WhenProductExists_ShouldUpdateProduct() {
        // Given
        when(productService.updateProduct(1L, testProductRequest)).thenReturn(Optional.of(testProductResponse));
        
        // When
        ResponseEntity<ProductResponse> response = productController.updateProduct(1L, testProductRequest);
        
        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Test Product", response.getBody().getName());
        verify(productService).updateProduct(1L, testProductRequest);
    }
    
    @Test
    void updateProduct_WhenProductNotExists_ShouldReturnNotFound() {
        // Given
        when(productService.updateProduct(1L, testProductRequest)).thenReturn(Optional.empty());
        
        // When
        ResponseEntity<ProductResponse> response = productController.updateProduct(1L, testProductRequest);
        
        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(productService).updateProduct(1L, testProductRequest);
    }
    
    @Test
    void updateProduct_WhenProductNameExists_ShouldReturnConflict() {
        // Given
        when(productService.updateProduct(1L, testProductRequest))
                .thenThrow(new IllegalArgumentException("Product with same name already exists"));
        
        // When
        ResponseEntity<ProductResponse> response = productController.updateProduct(1L, testProductRequest);
        
        // Then
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNull(response.getBody());
        verify(productService).updateProduct(1L, testProductRequest);
    }
    
    @Test
    void deleteProduct_WhenProductExists_ShouldDeleteProduct() {
        // Given
        when(productService.deleteProduct(1L)).thenReturn(true);
        
        // When
        ResponseEntity<Void> response = productController.deleteProduct(1L);
        
        // Then
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(productService).deleteProduct(1L);
    }
    
    @Test
    void deleteProduct_WhenProductNotExists_ShouldReturnNotFound() {
        // Given
        when(productService.deleteProduct(1L)).thenReturn(false);
        
        // When
        ResponseEntity<Void> response = productController.deleteProduct(1L);
        
        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(productService).deleteProduct(1L);
    }
}
