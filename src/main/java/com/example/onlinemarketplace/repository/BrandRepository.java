package com.example.onlinemarketplace.repository;

import com.example.onlinemarketplace.model.Brand;
import com.example.onlinemarketplace.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
    List<Brand> findAllByNameContains(String name);

    List<Brand> findAllByIdIn(List<Long> longIds);
}
