package com.retailedge.catalog.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.retailedge.catalog.entity.Product;
import com.retailedge.catalog.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
class ProductControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        productRepository.deleteAll();
    }

    @Test
    void testGetAllProducts() throws Exception {
        // Given
        Product product1 = createTestProduct("Test Product 1", "Description 1", new BigDecimal("29.99"), 10);
        Product product2 = createTestProduct("Test Product 2", "Description 2", new BigDecimal("39.99"), 5);
        productRepository.saveAll(Arrays.asList(product1, product2));

        // When & Then
        mockMvc.perform(get("/api/catalog/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Test Product 1")))
                .andExpect(jsonPath("$[0].price", is(29.99)))
                .andExpect(jsonPath("$[0].stock", is(10)))
                .andExpect(jsonPath("$[1].name", is("Test Product 2")))
                .andExpect(jsonPath("$[1].price", is(39.99)))
                .andExpect(jsonPath("$[1].stock", is(5)));
    }

    @Test
    void testGetProductById() throws Exception {
        // Given
        Product product = createTestProduct("Test Product", "Description", new BigDecimal("29.99"), 10);
        Product savedProduct = productRepository.save(product);

        // When & Then
        mockMvc.perform(get("/api/catalog/products/{id}", savedProduct.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(savedProduct.getId().intValue())))
                .andExpect(jsonPath("$.name", is("Test Product")))
                .andExpect(jsonPath("$.price", is(29.99)))
                .andExpect(jsonPath("$.stock", is(10)));
    }

    @Test
    void testGetProductByIdNotFound() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/catalog/products/{id}", 999))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateProduct() throws Exception {
        // Given
        String productJson = """
                {
                    "name": "New Product",
                    "description": "New Description",
                    "price": 49.99,
                    "stock": 15
                }
                """;

        // When & Then
        mockMvc.perform(post("/api/catalog/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("New Product")))
                .andExpect(jsonPath("$.description", is("New Description")))
                .andExpect(jsonPath("$.price", is(49.99)))
                .andExpect(jsonPath("$.stock", is(15)))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.createdAt", notNullValue()))
                .andExpect(jsonPath("$.updatedAt", notNullValue()));
    }

    @Test
    void testCreateProductWithInvalidData() throws Exception {
        // Given
        String invalidProductJson = """
                {
                    "name": "",
                    "price": -10.0,
                    "stock": -5
                }
                """;

        // When & Then
        mockMvc.perform(post("/api/catalog/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidProductJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateProduct() throws Exception {
        // Given
        Product product = createTestProduct("Test Product", "Description", new BigDecimal("29.99"), 10);
        Product savedProduct = productRepository.save(product);

        String updatedProductJson = """
                {
                    "name": "Updated Product",
                    "description": "Updated Description",
                    "price": 39.99,
                    "stock": 15
                }
                """;

        // When & Then
        mockMvc.perform(put("/api/catalog/products/{id}", savedProduct.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedProductJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(savedProduct.getId().intValue())))
                .andExpect(jsonPath("$.name", is("Updated Product")))
                .andExpect(jsonPath("$.description", is("Updated Description")))
                .andExpect(jsonPath("$.price", is(39.99)))
                .andExpect(jsonPath("$.stock", is(15)));
    }

    @Test
    void testUpdateProductNotFound() throws Exception {
        // Given
        String productJson = """
                {
                    "name": "Test Product",
                    "description": "Description",
                    "price": 29.99,
                    "stock": 10
                }
                """;

        // When & Then
        mockMvc.perform(put("/api/catalog/products/{id}", 999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteProduct() throws Exception {
        // Given
        Product product = createTestProduct("Test Product", "Description", new BigDecimal("29.99"), 10);
        Product savedProduct = productRepository.save(product);

        // When & Then
        mockMvc.perform(delete("/api/catalog/products/{id}", savedProduct.getId()))
                .andExpect(status().isNoContent());

        // Verify product is deleted
        mockMvc.perform(get("/api/catalog/products/{id}", savedProduct.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteProductNotFound() throws Exception {
        // When & Then
        mockMvc.perform(delete("/api/catalog/products/{id}", 999))
                .andExpect(status().isNotFound());
    }

    @Test
    void testSearchProducts() throws Exception {
        // Given
        Product product1 = createTestProduct("Test Product 1", "Description 1", new BigDecimal("29.99"), 10);
        Product product2 = createTestProduct("Test Product 2", "Description 2", new BigDecimal("39.99"), 5);
        Product product3 = createTestProduct("Other Product", "Description 3", new BigDecimal("19.99"), 8);
        productRepository.saveAll(Arrays.asList(product1, product2, product3));

        // When & Then
        mockMvc.perform(get("/api/catalog/products/search")
                        .param("q", "Test"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].name", everyItem(containsString("Test"))));
    }

    @Test
    void testSearchProductsEmptyResult() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/catalog/products/search")
                        .param("q", "nonexistent"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    private Product createTestProduct(String name, String description, BigDecimal price, Integer stock) {
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setStock(stock);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        return product;
    }
}