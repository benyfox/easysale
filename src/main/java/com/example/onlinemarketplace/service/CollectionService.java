package com.example.onlinemarketplace.service;

import com.example.onlinemarketplace.dto.request.CollectionRequest;
import com.example.onlinemarketplace.dto.response.CollectionDto;
import com.example.onlinemarketplace.dto.response.ProductDto;
import com.example.onlinemarketplace.model.Collection;
import com.example.onlinemarketplace.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CollectionService {

    CollectionDto findById(Long id);

    List<CollectionDto> getAllCollections();

    List<CollectionDto> getCollectionsBySlugs(List<String> slugs);

    Page<CollectionDto> getAllCollectionsPageable(Pageable paging);

    Page<CollectionDto> getListByNameLike(String name, Pageable paging);

    Page<CollectionDto> getListBySlugLike(String slug, Pageable paging);

    CollectionDto findByNameAndSeller(String slug);

    CollectionDto createNewCollection(CollectionRequest collectionRequest);

    CollectionDto updateCollection(CollectionRequest collectionRequest);

    boolean addCollectionProducts(CollectionRequest collection);

    boolean deleteCollection(CollectionRequest collectionRequest);

    boolean existsByName(String name);

    void editNewArrivals();

    void editRecommendedProducts();

    CollectionDto getRelatedProducts(Long id);
}
