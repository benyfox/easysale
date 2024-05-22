package com.example.onlinemarketplace.repository;

import com.example.onlinemarketplace.model.Collection;
import com.example.onlinemarketplace.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CollectionRepository extends JpaRepository<Collection, Long> {
    boolean existsBySlug(String name);

    Optional<Collection> findBySlug(String slug);

    Page<Collection> findAllBySlugContains(String slug, Pageable paging);

    Page<Collection> findAllByNameContains(String name, Pageable paging);

    List<Collection> findAllBySlugIn(List<String> slugs);
}
