package com.example.onlinemarketplace.repository;

import com.example.onlinemarketplace.model.Category;
import com.example.onlinemarketplace.model.CategoryType;
import com.example.onlinemarketplace.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    boolean existsById(Long id);
    Optional<Order> findById(Long id);
    Page<Order> findAllByUserUsername(Pageable pageable, String username);
    Page<Order> findAllByUserId(Pageable pageable, Long userId);
    List<Order> findAllByUserId(Long userId);
    Page<Order> findAll(Pageable paging);
    Optional<Order> findByOrderNumber(Long orderNumber);

    //find all orders where product is belongs to certain seller
    @Query("select distinct o from Order o join o.products p where p.seller.id = :sellerId")
    Page<Order> findAllOrdersBySellerId(@Param("sellerId") Long sellerId, Pageable paging);

    @Query("select distinct o from Order o join o.products p where p.seller.id = :sellerId")
    List<Order> findAllOrdersBySellerId(@Param("sellerId") Long sellerId);
}
