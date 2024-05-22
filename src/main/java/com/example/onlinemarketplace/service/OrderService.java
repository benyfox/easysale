package com.example.onlinemarketplace.service;

import com.example.onlinemarketplace.dto.request.CollectionRequest;
import com.example.onlinemarketplace.dto.request.OrderRequest;
import com.example.onlinemarketplace.dto.response.CollectionDto;
import com.example.onlinemarketplace.dto.response.OrderDto;
import com.example.onlinemarketplace.model.Order;
import com.example.onlinemarketplace.model.Seller;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderService {
    Page<OrderDto> findAll(Pageable pageable);

    Page<OrderDto> findAll(Pageable pageable, String username);

    OrderDto findById(Long id);

    List<OrderDto> getAllOrders();

    Page<OrderDto> getAllOrdersPageable(Pageable paging);

    OrderDto createOrder(String username, OrderRequest orderRequest);

    OrderDto updateOrder(Order order);

    void deleteOrder(Long id);

    List<OrderDto> findByUser(String username);

    List<Order> findBySeller(Seller seller);
}
