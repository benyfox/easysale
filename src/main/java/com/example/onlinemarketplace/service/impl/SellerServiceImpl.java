package com.example.onlinemarketplace.service.impl;

import com.example.onlinemarketplace.dto.response.ProductDto;
import com.example.onlinemarketplace.dto.response.SellerDto;
import com.example.onlinemarketplace.exception.SellerNotFoundException;
import com.example.onlinemarketplace.model.*;
import com.example.onlinemarketplace.repository.OrderRepository;
import com.example.onlinemarketplace.repository.ProductRepository;
import com.example.onlinemarketplace.repository.SellerRepository;
import com.example.onlinemarketplace.service.SellerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class SellerServiceImpl implements SellerService {

    private final SellerRepository sellerRepository;
    private final OrderRepository ordersRepository;
    private final ProductRepository productRepository;
    private final ProductServiceImpl productService;
    private final ModelMapper modelMapper;

    @Override
    public List<SellerDto> getAllSellers() {
        return (sellerRepository
                .findAll())
                .stream()
                .map(this::convertToSellerDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<SellerDto> getAllSellersPageable(Pageable paging) {
        Page<Seller> page = sellerRepository.findAll(paging);
        return new PageImpl<>(page.getContent().stream()
                .map(this::convertToSellerDto)
                .collect(Collectors.toList()), paging, page.getTotalElements());
    }

    @Override
    public Seller findByCompanyName(String companyName) {
        return sellerRepository.findByCompanyName(companyName)
                .orElseThrow(SellerNotFoundException::new);
    }

    @Override
    public SellerDto findByCompanyNameDto(String companyName) {
        return convertToSellerDto(sellerRepository.findByCompanyName(companyName)
                .orElseThrow(SellerNotFoundException::new));
    }

    @Override
    public SellerDto findByUserId(Long userId) {
        return convertToSellerDto(sellerRepository.findByUserId(userId)
                .orElseThrow(SellerNotFoundException::new));
    }

    @Override
    public SellerDto findByUserUsername(String username) {
        Seller seller = sellerRepository.findByUserUsername(username)
                .orElseThrow(SellerNotFoundException::new);

        SellerDto sellerDto = convertToSellerDto(seller);

        List<Order> orders = ordersRepository.findAllOrdersBySellerId(seller.getId());
        sellerDto.setIncome(orders.stream()
                .mapToDouble(Order::getAmount)
                .sum()
        );

        return sellerDto;
    }

    @Override
    public Seller _findByUserUsername(String username) {
        return sellerRepository.findByUserUsername(username)
                .orElseThrow(SellerNotFoundException::new);
    }

    @Override
    public Seller createNewSeller(Seller seller) {
        Objects.requireNonNull(seller, "seller cannot be null");
        User sellerUser = seller.getUser();
        sellerUser.addRole(new Role(RoleType.ROLE_MERCHANT));
        return sellerRepository.save(seller);
    }

    @Override
    public Seller updateSeller(Seller seller) {
        Objects.requireNonNull(seller, "seller cannot be null");
        return sellerRepository.save(seller);
    }

    @Override
    public boolean deleteSeller(String companyName) {
        Objects.requireNonNull(companyName, "company name cannot be null");
        Seller seller = sellerRepository.findByCompanyName(companyName)
                .orElseThrow(SellerNotFoundException::new);

        Long id = seller.getId();
        sellerRepository.delete(seller);
        return !sellerRepository.existsById(id);
    }

    @Override
    public boolean existsById(Seller seller) {
        Objects.requireNonNull(seller, "seller cannot be null");
        return sellerRepository.existsById(seller);
    }

    @Override
    public boolean existsByCompanyName(String companyName) {
        return sellerRepository.existsByCompanyName(companyName);
    }

    @Override
    public Page<SellerDto> findAllByCompanyNameContains(String name, Pageable paging) {
        Page<Seller> page = sellerRepository.findAllByCompanyNameContains(name, paging);
        return new PageImpl<>(page.getContent().stream()
                .map(this::convertToSellerDto)
                .collect(Collectors.toList()), paging, page.getTotalElements());
    }

    @Override
    public List<ProductDto> getAllProducts(String companyName) {
        Seller seller = sellerRepository.findByCompanyName(companyName)
                .orElseThrow(SellerNotFoundException::new);

        List<Product> list = productRepository.getAllBySeller(seller);
        return list
                .stream()
                .map(productService::convertToProductDto)
                .collect(Collectors.toList());
    }

    private SellerDto convertToSellerDto(Seller seller) {
        return modelMapper.map(seller, SellerDto.class);
    }
}
