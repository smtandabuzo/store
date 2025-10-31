package com.example.store.controller;

import com.example.store.dto.OrderDTO;
import com.example.store.entity.Order;
import com.example.store.exception.ResourceNotFoundException;
import com.example.store.mapper.OrderMapper;
import com.example.store.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @GetMapping
    public List<OrderDTO> getAllOrders() {
        return orderMapper.ordersToOrderDTOs(orderRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderByID(@PathVariable("id") Long id) {
        Order order = orderRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        return ResponseEntity.ok(orderMapper.orderToOrderDTO(order));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDTO createOrder(@RequestBody Order order) {
        return orderMapper.orderToOrderDTO(orderRepository.save(order));
    }
}
