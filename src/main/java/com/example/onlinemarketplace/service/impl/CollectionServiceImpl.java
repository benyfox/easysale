package com.example.onlinemarketplace.service.impl;

import com.example.onlinemarketplace.dto.request.CollectionRequest;
import com.example.onlinemarketplace.dto.response.CollectionDto;
import com.example.onlinemarketplace.dto.response.ProductDto;
import com.example.onlinemarketplace.exception.CollectionAlreadyExistsException;
import com.example.onlinemarketplace.exception.CollectionNotFoundException;
import com.example.onlinemarketplace.model.Collection;
import com.example.onlinemarketplace.exception.ProductNotFoundException;
import com.example.onlinemarketplace.model.Product;
import com.example.onlinemarketplace.repository.CollectionRepository;
import com.example.onlinemarketplace.repository.ProductRepository;
import com.example.onlinemarketplace.service.CollectionService;
import com.example.onlinemarketplace.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CollectionServiceImpl implements CollectionService {

    private final CollectionRepository collectionRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    @Override
    public CollectionDto findById(Long id) {
        Objects.requireNonNull(id, "id cannot be null");
        return convertToCollectionDto(collectionRepository.findById(id).orElseThrow(ProductNotFoundException::new));
    }

    @Override
    public List<CollectionDto> getAllCollections() {
        return collectionRepository.findAll().stream()
                .map(this::convertToCollectionDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CollectionDto> getCollectionsBySlugs(List<String> slugs) {
        return collectionRepository.findAllBySlugIn(slugs).stream()
                .map(this::convertToCollectionDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<CollectionDto> getAllCollectionsPageable(Pageable paging) {
        Page<Collection> page = collectionRepository.findAll(paging);
        return new PageImpl<>(page.getContent().stream()
                .map(this::convertToCollectionDto)
                .collect(Collectors.toList()), paging, page.getTotalElements());
    }

    @Override
    public Page<CollectionDto> getListByNameLike(String name, Pageable paging) {
        Page<Collection> page = collectionRepository.findAllByNameContains(name, paging);
        return new PageImpl<>(page.getContent().stream()
                .map(this::convertToCollectionDto)
                .collect(Collectors.toList()), paging, page.getTotalElements());
    }

    @Override
    public Page<CollectionDto> getListBySlugLike(String slug, Pageable paging) {
        Page<Collection> page = collectionRepository.findAllBySlugContains(slug, paging);
        return new PageImpl<>(page.getContent().stream()
                .map(this::convertToCollectionDto)
                .collect(Collectors.toList()), paging, page.getTotalElements());
    }

    @Override
    public CollectionDto findByNameAndSeller(String slug) {
        return convertToCollectionDto(collectionRepository.findBySlug(slug)
                .orElseThrow(CollectionNotFoundException::new));
    }

    @Override
    public CollectionDto createNewCollection(CollectionRequest collectionRequest) {
        Collection collection = convertToCollection(collectionRequest);

        collection.setSlug(collectionRequest.getName().toLowerCase().replace(" ", "-"));

        if (collectionRepository.existsBySlug(collection.getSlug())) {
            throw new CollectionAlreadyExistsException();
        }

        List<Product> products = createProductList(collectionRequest);

        collection.setProducts(products);

        return convertToCollectionDto(collectionRepository.save(collection));
    }

    @Override
    public CollectionDto updateCollection(CollectionRequest collectionRequest) {
        Collection collection = convertToCollection(collectionRequest);

        if (!collectionRepository.existsById(collection.getId())) {
            throw new CollectionNotFoundException();
        }

        if (collection.getName() != null && !collectionRequest.getName().isEmpty()) {
            collection.setSlug(collectionRequest.getName().toLowerCase().replace(" ", "-"));
        }

        List<Product> products = createProductList(collectionRequest);

        collection.setProducts(products);

        return convertToCollectionDto(collectionRepository.save(collection));
    }


    @Override
    public boolean addCollectionProducts(CollectionRequest collectionRequest) {
        Collection collection = convertToCollection(collectionRequest);

        if (!collectionRepository.existsById(collection.getId())) {
            throw new CollectionNotFoundException();
        }

        List<Product> products = createProductList(collectionRequest);

        if (products.isEmpty()) {
            return false;
        }

        for (Product product : products) {
            if (collection.getProducts().contains(product)) {
                return false;
            }
        }

        for (Product product : products) {
            if (product.getCollections().contains(collection)) {
                return false;
            }
        }

        collection.addProducts(products);
        collectionRepository.save(collection);
        return true;
    }

    @Override
    public boolean deleteCollection(CollectionRequest collectionRequest) {
        Collection collection = convertToCollection(collectionRequest);

        if (!collectionRepository.existsById(collection.getId())) {
            throw new CollectionNotFoundException();
        }

        collectionRepository.delete(collection);
        return true;
    }

    @Override
    public boolean existsByName(String name) {
        return collectionRepository.existsBySlug(name);
    }

    public CollectionDto convertToCollectionDto(Collection collection) {
        return modelMapper.map(collection, CollectionDto.class);
    }
    public Collection convertToCollection(CollectionRequest collectionRequest) {
        return modelMapper.map(collectionRequest, Collection.class);
    }

    private List<Product> createProductList(CollectionRequest collectionRequest) {
        List<Product> products = new ArrayList<>();
        if (collectionRequest.getProducts() != null && !collectionRequest.getProducts().isEmpty()) {
            products = collectionRequest.getProducts().stream()
                    .map(productId -> productRepository.findById(productId).orElseThrow(ProductNotFoundException::new))
                    .collect(Collectors.toList());
        }

        return products;
    }

    public void editNewArrivals() {
        List<Product> products = productRepository.findTop10ByOrderByCreatedAtDesc();

        Collection collection = collectionRepository.findBySlug("new_arrivals_products").orElse(
                Collection.builder()
                        .name("Новые поступления")
                        .slug("new_arrivals_products")
                        .build()
        );

        collection.setProducts(products);
        collectionRepository.save(collection);
    }

    public void editRecommendedProducts() {
        List<Product> products = productRepository.findTop10ByOrderByCreatedAtDesc();

        Collection collection = collectionRepository.findBySlug("shop-recommend-items").orElse(
                Collection.builder()
                        .name("Рекомендуемое")
                        .slug("shop-recommend-items")
                        .build()
        );

        collection.setProducts(products);
        collectionRepository.save(collection);
    }

    public CollectionDto getRelatedProducts(Long productId) {
        Product product = productRepository
                .findById(productId)
                .orElseThrow(ProductNotFoundException::new);

        List<Product> relatedProducts = new ArrayList<>();

         if (product.getBrand() != null) {
             relatedProducts = productRepository.findTop10ByBrand(product.getBrand());
         } else if (product.getCategory() != null) {
             relatedProducts = productRepository.findTop10ByCategory(product.getCategory());
         }

        CollectionDto collectionDto = new CollectionDto();
        collectionDto.setProducts(relatedProducts.stream().map(this::convertToProductDto).collect(Collectors.toList()));
        return collectionDto;
    }

    public ProductDto convertToProductDto(Product product) {
        return modelMapper.map(product, ProductDto.class);
    }
}
