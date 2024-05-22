package com.example.onlinemarketplace.controller;

import com.example.onlinemarketplace.dto.request.CollectionRequest;
import com.example.onlinemarketplace.dto.response.CollectionDto;
import com.example.onlinemarketplace.service.CollectionService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
@Getter
@Setter
@RequestMapping("/api/collection")
public class CollectionController {

    private final CollectionService collectionService;

    @GetMapping("/all")
    public List<CollectionDto> getAll() {
        collectionService.editNewArrivals();
        collectionService.editRecommendedProducts();
        return collectionService.getAllCollections();
    }

    @GetMapping
    public List<CollectionDto> getCollectionsBySlugs(
            @RequestParam(name = "slug_in", required = false)
            String[] slugIn
    ) {
        List<String> slugs = slugIn != null ? Arrays.asList(slugIn) : Collections.emptyList();

        return collectionService.getCollectionsBySlugs(slugs);
    }

    @GetMapping("/related_products/{id}")
    public CollectionDto getRelatedProductsCollection(@PathVariable Long id) {
        return collectionService.getRelatedProducts(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_MERCHANT')")
    public CollectionDto create(@Valid @RequestBody CollectionRequest collectionRequest) {
        return collectionService.createNewCollection(collectionRequest);
    }

    @PutMapping
    @PreAuthorize("hasRole('ROLE_MERCHANT')")
    public ResponseEntity<String> addCollectionProduct(@Valid @RequestBody CollectionRequest collectionRequest) {
        if (collectionService.addCollectionProducts(collectionRequest)) {
            return ResponseEntity.ok(
                    collectionRequest.getProducts().size() + " products successfully added.");
        } else {
            return ResponseEntity.badRequest().body("Failed to add products to collection.");
        }
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ROLE_MERCHANT')")
    public ResponseEntity<String> deleteCollection(@Valid @RequestBody CollectionRequest collectionRequest) {
        if (collectionService.deleteCollection(collectionRequest)) {
            return ResponseEntity.ok("Collection successfully deleted.");
        } else {
            return ResponseEntity.badRequest().body("Failed to delete collection.");
        }
    }

    @PatchMapping
    @PreAuthorize("hasRole('ROLE_MERCHANT')")
    public CollectionDto updateCollection(@Valid @RequestBody CollectionRequest collectionRequest) {
        return collectionService.updateCollection(collectionRequest);
    }
}
