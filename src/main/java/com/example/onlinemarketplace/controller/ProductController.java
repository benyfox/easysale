package com.example.onlinemarketplace.controller;

import com.example.onlinemarketplace.dto.request.ProductDeleteRequest;
import com.example.onlinemarketplace.dto.request.ProductRequest;
import com.example.onlinemarketplace.dto.response.ProductDto;
import com.example.onlinemarketplace.model.Product;
import com.example.onlinemarketplace.model.Seller;
import com.example.onlinemarketplace.service.ProductService;
import com.example.onlinemarketplace.service.SellerService;
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
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;
    private final SellerService sellerService;

    @GetMapping
    public ResponseEntity<Page<ProductDto>> getPageable(
            @RequestParam(defaultValue = "0") int _start,
            @RequestParam(defaultValue = "5") int _limit,
            @RequestParam(defaultValue = "0") Float price_gt,
            @RequestParam(defaultValue = "5000000") Float price_lt
    ) {
        Pageable paging = PageRequest.of(_start, _limit);

        Page<ProductDto> productsDto = productService.getAllProductsPageable(paging, price_gt, price_lt);
        return new ResponseEntity<>(productsDto, HttpStatus.OK);
    }

    @GetMapping("/all")
    public List<ProductDto> getAll() {
        return productService.getAllProducts();
    }

    @GetMapping("/count")
    public long getCount() {
        return productService.getAllProductsCount();
    }

    @GetMapping("/find")
    public ProductDto find(
            @RequestParam String productName,
            @RequestParam String companyName
    ) {
        Objects.requireNonNull(productName, "product name can not be null");
        Objects.requireNonNull(companyName, "company name can not be null");
        Seller seller = sellerService.findByCompanyName(companyName);
        return productService.findByNameAndSeller(productName, seller);
    }

    @GetMapping("/list")
    public ResponseEntity<Page<ProductDto>> getPageable(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "12") int pageSize
    ) {
        Pageable paging = PageRequest.of(pageNumber, pageSize);
        Page<ProductDto> productsDto = productService.getListByNameLike(name, paging);
        return new ResponseEntity<>(productsDto, HttpStatus.OK);
    }


    @GetMapping("/category/{slug}")
    public ResponseEntity<Page<ProductDto>> getByCategorySlug(
            @PathVariable String slug,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "12") int pageSize
    ) {
        System.out.println(slug);
        Pageable paging = PageRequest.of(pageNumber, pageSize);
        Page<ProductDto> productsDto = productService.getProductsByCategory(slug, paging);
        return new ResponseEntity<>(productsDto, HttpStatus.OK);
    }

    @GetMapping("/brand")
    public ResponseEntity<Page<ProductDto>> getByBrandId(
            @RequestParam(name = "id_in", required = false)
            String[] idsIn,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "12") int pageSize
    ) {
        List<String> ids = idsIn != null ? Arrays.asList(idsIn) : Collections.emptyList();
        Pageable paging = PageRequest.of(pageNumber, pageSize);
        Page<ProductDto> productsDto = productService.getProductsByBrandId(ids, paging);
        return new ResponseEntity<>(productsDto, HttpStatus.OK);
    }

    @GetMapping("/seller/{companyName}")
    public ResponseEntity<Page<ProductDto>> getBySeller(
            @PathVariable String companyName,
            @RequestParam(defaultValue = "0") int _start,
            @RequestParam(defaultValue = "12") int _limit
    ) {
        Seller seller = sellerService.findByCompanyName(companyName);
        Pageable paging = PageRequest.of(_start, _limit);
        Page<ProductDto> productsDto = productService.getProductsBySeller(seller, paging);
        return new ResponseEntity<>(productsDto, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_MERCHANT')")
    @GetMapping("/seller")
    public ResponseEntity<Page<ProductDto>> getByToken(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int _start,
            @RequestParam(defaultValue = "12") int _limit
    ) {
        Pageable paging = PageRequest.of(_start, _limit);
        Page<ProductDto> productsDto = productService.getProductsBySellerUsername(userDetails.getUsername(), paging);
        return new ResponseEntity<>(productsDto, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getById(@PathVariable Long id) {
        return new ResponseEntity<>(productService.findById(id), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_MERCHANT')")
    public ProductDto create(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @ModelAttribute ProductRequest product
    ) throws IOException {
        return productService.createNewProduct(userDetails.getUsername(), product);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ROLE_MERCHANT')")
    public Boolean delete(
            @Valid @RequestBody ProductDeleteRequest product,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        productService.deleteProduct(product, userDetails.getUsername());
        return true;
    }

    @PatchMapping
    @PreAuthorize("hasRole('ROLE_MERCHANT')")
    public Product update(@Valid @RequestBody Product product) {
        return productService.updateProduct(product);
    }
}
