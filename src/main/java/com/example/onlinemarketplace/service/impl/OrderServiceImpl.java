package com.example.onlinemarketplace.service.impl;

import com.example.onlinemarketplace.dto.request.OrderRequest;
import com.example.onlinemarketplace.dto.response.OrderDto;
import com.example.onlinemarketplace.exception.OrderNotFoundException;
import com.example.onlinemarketplace.exception.ProductNotFoundException;
import com.example.onlinemarketplace.exception.SellerNotFoundException;
import com.example.onlinemarketplace.exception.UserNotFoundException;
import com.example.onlinemarketplace.model.*;
import com.example.onlinemarketplace.repository.OrderRepository;
import com.example.onlinemarketplace.repository.ProductRepository;
import com.example.onlinemarketplace.repository.SellerRepository;
import com.example.onlinemarketplace.repository.UserRepository;
import com.example.onlinemarketplace.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final SellerRepository sellerRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    @Override
    public Page<OrderDto> findAll(Pageable pageable) {
        Page<Order> page = orderRepository.findAll(pageable);
        return new PageImpl<>(page.getContent().stream()
                .map(this::convertToOrderDto)
                .collect(Collectors.toList()), pageable, page.getTotalElements());
    }

    @Override
    public Page<OrderDto> findAll(Pageable pageable, String username) {
        Seller seller = sellerRepository.findByUserUsername(username)
                .orElseThrow(SellerNotFoundException::new);

        Page<Order> page = orderRepository.findAllOrdersBySellerId(seller.getId(), pageable);

        return new PageImpl<>(page.getContent().stream()
                .map(this::convertToOrderDto)
                .collect(Collectors.toList()), pageable, page.getTotalElements());
    }

    @Override
    public OrderDto findById(Long id) {
        return orderRepository.findById(id)
                .map(this::convertToOrderDto)
                .orElseThrow(OrderNotFoundException::new);
    }

    @Override
    public List<OrderDto> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(this::convertToOrderDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<OrderDto> getAllOrdersPageable(Pageable paging) {
        return orderRepository.findAll(paging)
                .map(this::convertToOrderDto);
    }

    @Override
    public OrderDto createOrder(String username, OrderRequest orderRequest) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);
        Order order = convertToOrder(orderRequest);

        Map<Product, Integer> productQuantities = orderRequest.getProducts().entrySet().stream()
                .collect(Collectors.toMap(entry -> productRepository.findById(entry.getKey()).get(),
                        Map.Entry::getValue,
                        Integer::sum,
                        HashMap::new));

        double amount = productQuantities.entrySet().stream()
                .map(entry -> (double) entry.getKey().getPrice() * entry.getValue())
                .reduce(0.0, Double::sum);

        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);
        order.setAmount(amount);
        order.setCount(productQuantities.values().stream().reduce(0, Integer::sum));
        order.setOrderNumber(System.currentTimeMillis());
        order.setTitle(productQuantities.entrySet().stream()
                .map(entry -> entry.getKey().getName() + " (" + entry.getValue() + ")")
                .collect(Collectors.joining(", ")));
        order.setOrderDate(new Date());

        List<Product> orderProducts = productQuantities.entrySet().stream()
                .flatMap(entry -> Collections.nCopies(entry.getValue(), entry.getKey()).stream())
                .collect(Collectors.toList());

        order.setProducts(orderProducts);

        return convertToOrderDto(orderRepository.save(order));
    }

    @Override
    public OrderDto updateOrder(Order orderRequest) {
        Order order = orderRepository.findById(orderRequest.getId()).orElseThrow(OrderNotFoundException::new);

        return convertToOrderDto(orderRepository.save(order));
    }

    @Override
    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new OrderNotFoundException();
        }
        orderRepository.deleteById(id);
    }

    @Override
    public List<OrderDto> findByUser(String username) {
        return null;
    }

    @Override
    public List<Order> findBySeller(Seller seller) {
        return orderRepository.findAllByUserId(seller.getUser().getId());
    }

    private OrderDto convertToOrderDto(Order order) {
        return modelMapper.map(order, OrderDto.class);
    }

    private Order convertToOrder(OrderDto orderDto) {
        return modelMapper.map(orderDto, Order.class);
    }

    private Order convertToOrder(OrderRequest orderRequest) {
        return modelMapper.map(orderRequest, Order.class);
    }
}
