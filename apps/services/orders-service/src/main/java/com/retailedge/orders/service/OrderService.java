package com.retailedge.orders.service;

import com.retailedge.orders.dto.OrderItemRequest;
import com.retailedge.orders.dto.OrderItemResponse;
import com.retailedge.orders.dto.OrderRequest;
import com.retailedge.orders.dto.OrderResponse;
import com.retailedge.orders.entity.Order;
import com.retailedge.orders.entity.OrderItem;
import com.retailedge.orders.entity.OrderStatus;
import com.retailedge.orders.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {
    
    @Autowired
    private OrderRepository orderRepository;
    
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    public Page<OrderResponse> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable)
                .map(this::convertToResponse);
    }
    
    public Optional<OrderResponse> getOrderById(Long id) {
        return orderRepository.findById(id)
                .map(this::convertToResponse);
    }
    
    public List<OrderResponse> getOrdersByCustomerId(Long customerId) {
        return orderRepository.findByCustomerId(customerId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    public List<OrderResponse> getOrdersByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    public OrderResponse createOrder(OrderRequest request) {
        Order order = convertToEntity(request);
        
        // Calculate total amount
        BigDecimal totalAmount = request.getOrderItems().stream()
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotalAmount(totalAmount);
        
        Order savedOrder = orderRepository.save(order);
        return convertToResponse(savedOrder);
    }
    
    public Optional<OrderResponse> updateOrderStatus(Long id, OrderStatus status) {
        return orderRepository.findById(id)
                .map(order -> {
                    order.setStatus(status);
                    Order updatedOrder = orderRepository.save(order);
                    return convertToResponse(updatedOrder);
                });
    }
    
    public Optional<OrderResponse> updateOrder(Long id, OrderRequest request) {
        return orderRepository.findById(id)
                .map(existingOrder -> {
                    existingOrder.setCustomerId(request.getCustomerId());
                    existingOrder.setNotes(request.getNotes());
                    existingOrder.setShippingAddress(request.getShippingAddress());
                    existingOrder.setBillingAddress(request.getBillingAddress());
                    
                    // Clear existing items and add new ones
                    existingOrder.getOrderItems().clear();
                    for (OrderItemRequest itemRequest : request.getOrderItems()) {
                        OrderItem orderItem = convertToOrderItemEntity(itemRequest);
                        orderItem.setOrder(existingOrder);
                        existingOrder.addOrderItem(orderItem);
                    }
                    
                    // Recalculate total amount
                    BigDecimal totalAmount = request.getOrderItems().stream()
                            .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    existingOrder.setTotalAmount(totalAmount);
                    
                    Order updatedOrder = orderRepository.save(existingOrder);
                    return convertToResponse(updatedOrder);
                });
    }
    
    public boolean deleteOrder(Long id) {
        if (orderRepository.existsById(id)) {
            orderRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    public boolean existsById(Long id) {
        return orderRepository.existsById(id);
    }
    
    private Order convertToEntity(OrderRequest request) {
        Order order = new Order();
        order.setCustomerId(request.getCustomerId());
        order.setNotes(request.getNotes());
        order.setShippingAddress(request.getShippingAddress());
        order.setBillingAddress(request.getBillingAddress());
        
        for (OrderItemRequest itemRequest : request.getOrderItems()) {
            OrderItem orderItem = convertToOrderItemEntity(itemRequest);
            order.addOrderItem(orderItem);
        }
        
        return order;
    }
    
    private OrderItem convertToOrderItemEntity(OrderItemRequest request) {
        OrderItem orderItem = new OrderItem();
        orderItem.setProductId(request.getProductId());
        orderItem.setProductName(request.getProductName());
        orderItem.setQuantity(request.getQuantity());
        orderItem.setUnitPrice(request.getUnitPrice());
        orderItem.calculateTotalPrice();
        return orderItem;
    }
    
    private OrderResponse convertToResponse(Order order) {
        List<OrderItemResponse> orderItemResponses = order.getOrderItems().stream()
                .map(this::convertToOrderItemResponse)
                .collect(Collectors.toList());
        
        return new OrderResponse(
                order.getId(),
                order.getCustomerId(),
                order.getStatus(),
                order.getTotalAmount(),
                order.getNotes(),
                order.getShippingAddress(),
                order.getBillingAddress(),
                order.getCreatedAt(),
                order.getUpdatedAt(),
                orderItemResponses
        );
    }
    
    private OrderItemResponse convertToOrderItemResponse(OrderItem orderItem) {
        return new OrderItemResponse(
                orderItem.getId(),
                orderItem.getProductId(),
                orderItem.getProductName(),
                orderItem.getQuantity(),
                orderItem.getUnitPrice(),
                orderItem.getTotalPrice()
        );
    }
}
