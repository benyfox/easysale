package com.example.onlinemarketplace.repository;

import com.example.onlinemarketplace.model.Brand;
import com.example.onlinemarketplace.model.Category;
import com.example.onlinemarketplace.model.Product;
import com.example.onlinemarketplace.model.Seller;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByName(String name);

    boolean existsById(Long id);

    boolean existsByName(String name);

    Product findByNameAndSeller(String name, Seller seller);

    Page<Product> getAllBySeller(Seller seller, Pageable paging);

    List<Product> getAllBySeller(Seller seller);

    Page<Product> findAllByNameContains(String name, Pageable paging);

    Page<Product> getProductsByCategory(Category category, Pageable paging);

    Page<Product> getProductsByPriceBetween(Pageable paging, Float min, Float max);

    List<Product> getProductsByIsAvailable(Boolean isAvailable);

    List<Product> getProductsByDiscountRateGreaterThanEqual(Integer discountRate);

    Long countAllByIdNotIn(List<Long> ids);

    Page<Product> findAllByBrand_IdIn(List<Long> ids, Pageable paging);

    List<Product> findTop10ByOrderByCreatedAtDesc();

    List<Product> findTop10ByCategory(Category category);

    List<Product> findTop10ByBrand(Brand brand);

    Optional<Product> findBySku(String sku);
}
