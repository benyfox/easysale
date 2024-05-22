package com.example.onlinemarketplace.service;

import com.example.onlinemarketplace.dto.request.ProductDeleteRequest;
import com.example.onlinemarketplace.dto.request.ProductRequest;
import com.example.onlinemarketplace.dto.response.ProductDto;
import com.example.onlinemarketplace.model.Brand;
import com.example.onlinemarketplace.model.Category;
import com.example.onlinemarketplace.model.Product;
import com.example.onlinemarketplace.model.Seller;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

public interface ProductService {

    ProductDto findById(Long id);

    List<ProductDto> getAllProducts();

    Page<ProductDto> getAllProductsPageable(Pageable paging, Float price_gt, Float price_lt);

    Page<ProductDto> getListByNameLike(String name, Pageable paging);

    ProductDto findByNameAndSeller(String name, Seller seller);

    ProductDto createNewProduct(String sellerUsername, ProductRequest product) throws IOException;

    Product updateProduct(Product product);

    void deleteProduct(ProductDeleteRequest product, String username);

    Product findByName(String name);

    Page<ProductDto> getProductsBySeller(Seller seller, Pageable paging);

    List<Product> getProductsBySeller(Seller seller);

    boolean existsByName(String name);

    Page<ProductDto> getProductsBySellerUsername(String username, Pageable paging);

    long getAllProductsCount();

    Page<ProductDto> getProductsByCategory(String category, Pageable paging);

    Page<ProductDto> getProductsByBrandId(List<String> ids, Pageable paging);
}
