package com.retailedge.orders.service;

import com.retailedge.orders.dto.OrderItemRequest;
import com.retailedge.orders.dto.OrderRequest;
import com.retailedge.orders.dto.OrderResponse;
import com.retailedge.orders.entity.Order;
import com.retailedge.orders.entity.OrderItem;
import com.retailedge.orders.entity.OrderStatus;
import com.retailedge.orders.repository.OrderRepository;
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
class OrderServiceTest {
    
    @Mock
    private OrderRepository orderRepository;
    
    @InjectMocks
    private OrderService orderService;
    
    private Order testOrder;
    private OrderRequest testOrderRequest;
    private OrderResponse expectedResponse;
    
    @BeforeEach
    void setUp() {
        testOrder = new Order();
        testOrder.setId(1L);
        testOrder.setCustomerId(1L);
        testOrder.setStatus(OrderStatus.PENDING);
        testOrder.setTotalAmount(new BigDecimal("99.99"));
        testOrder.setNotes("Test order");
        testOrder.setCreatedAt(LocalDateTime.now());
        testOrder.setUpdatedAt(LocalDateTime.now());
        
        OrderItem orderItem = new OrderItem();
        orderItem.setId(1L);
        orderItem.setProductId(1L);
        orderItem.setProductName("Test Product");
        orderItem.setQuantity(1);
        orderItem.setUnitPrice(new BigDecimal("99.99"));
        orderItem.setTotalPrice(new BigDecimal("99.99"));
        testOrder.addOrderItem(orderItem);
        
        testOrderRequest = new OrderRequest();
        testOrderRequest.setCustomerId(1L);
        testOrderRequest.setNotes("Test order");
        
        OrderItemRequest orderItemRequest = new OrderItemRequest();
        orderItemRequest.setProductId(1L);
        orderItemRequest.setProductName("Test Product");
        orderItemRequest.setQuantity(1);
        orderItemRequest.setUnitPrice(new BigDecimal("99.99"));
        testOrderRequest.setOrderItems(Arrays.asList(orderItemRequest));
        
        expectedResponse = new OrderResponse(
                1L, 1L, OrderStatus.PENDING, new BigDecimal("99.99"),
                "Test order", null, null, LocalDateTime.now(), LocalDateTime.now(), null
        );
    }
    
    @Test
    void getAllOrders_ShouldReturnAllOrders() {
        // Given
        List<Order> orders = Arrays.asList(testOrder);
        when(orderRepository.findAll()).thenReturn(orders);
        
        // When
        List<OrderResponse> result = orderService.getAllOrders();
        
        // Then
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getCustomerId());
        verify(orderRepository).findAll();
    }
    
    @Test
    void getOrderById_WhenOrderExists_ShouldReturnOrder() {
        // Given
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
        
        // When
        Optional<OrderResponse> result = orderService.getOrderById(1L);
        
        // Then
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getCustomerId());
        verify(orderRepository).findById(1L);
    }
    
    @Test
    void getOrderById_WhenOrderNotExists_ShouldReturnEmpty() {
        // Given
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());
        
        // When
        Optional<OrderResponse> result = orderService.getOrderById(1L);
        
        // Then
        assertFalse(result.isPresent());
        verify(orderRepository).findById(1L);
    }
    
    @Test
    void getOrdersByCustomerId_ShouldReturnCustomerOrders() {
        // Given
        List<Order> orders = Arrays.asList(testOrder);
        when(orderRepository.findByCustomerId(1L)).thenReturn(orders);
        
        // When
        List<OrderResponse> result = orderService.getOrdersByCustomerId(1L);
        
        // Then
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getCustomerId());
        verify(orderRepository).findByCustomerId(1L);
    }
    
    @Test
    void createOrder_WhenValidRequest_ShouldCreateOrder() {
        // Given
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);
        
        // When
        OrderResponse result = orderService.createOrder(testOrderRequest);
        
        // Then
        assertEquals(1L, result.getCustomerId());
        verify(orderRepository).save(any(Order.class));
    }
    
    @Test
    void updateOrderStatus_WhenOrderExists_ShouldUpdateStatus() {
        // Given
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);
        
        // When
        Optional<OrderResponse> result = orderService.updateOrderStatus(1L, OrderStatus.CONFIRMED);
        
        // Then
        assertTrue(result.isPresent());
        verify(orderRepository).findById(1L);
        verify(orderRepository).save(any(Order.class));
    }
    
    @Test
    void deleteOrder_WhenOrderExists_ShouldDeleteOrder() {
        // Given
        when(orderRepository.existsById(1L)).thenReturn(true);
        
        // When
        boolean result = orderService.deleteOrder(1L);
        
        // Then
        assertTrue(result);
        verify(orderRepository).existsById(1L);
        verify(orderRepository).deleteById(1L);
    }
    
    @Test
    void deleteOrder_WhenOrderNotExists_ShouldReturnFalse() {
        // Given
        when(orderRepository.existsById(1L)).thenReturn(false);
        
        // When
        boolean result = orderService.deleteOrder(1L);
        
        // Then
        assertFalse(result);
        verify(orderRepository).existsById(1L);
        verify(orderRepository, never()).deleteById(anyLong());
    }
}
