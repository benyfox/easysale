package com.example.onlinemarketplace.controller;

import com.example.onlinemarketplace.dto.request.OrderRequest;
import com.example.onlinemarketplace.dto.response.OrderDto;
import com.example.onlinemarketplace.model.Order;
import com.example.onlinemarketplace.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/order")
public class OrderContoller {
    private final OrderService orderService;

    @GetMapping("all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Page<OrderDto>> getPageable(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "5") int pageSize
    ) {
        Pageable paging = PageRequest.of(pageNumber, pageSize);
        Page<OrderDto> userDto = orderService.findAll(paging);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_MERCHANT')" +
        "or hasRole('ROLE_ADMIN')")
    public ResponseEntity<Page<OrderDto>> getPageable(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int _start,
            @RequestParam(defaultValue = "5") int _limit
    ) {
        Pageable paging = PageRequest.of(_start, _limit);
        Page<OrderDto> userDto = orderService.findAll(paging, userDetails.getUsername());
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

//    @GetMapping
//    public UserDto getUserOrdersPageable(
//            @AuthenticationPrincipal OrderRequest orderRequest,
//            @RequestParam(defaultValue = "0") int pageNumber,
//            @RequestParam(defaultValue = "5") int pageSize
//    ) {
//        try {
//            return
//        } catch (NullPointerException e) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
//        }
//    }
//
    @PostMapping
    public OrderDto create(@AuthenticationPrincipal UserDetails userDetails,
                           @Valid @RequestBody OrderRequest orderRequest) {
        return orderService.createOrder(userDetails.getUsername(), orderRequest);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public boolean delete(@Valid @RequestBody Order order) {
        orderService.deleteOrder(order.getId());
        return true;
    }

    @PatchMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public OrderDto update(@AuthenticationPrincipal UserDetails userDetails,
                           @Valid @RequestBody Order order) {
        return orderService.updateOrder(order);
    }
}