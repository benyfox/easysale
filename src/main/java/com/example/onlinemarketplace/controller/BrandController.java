package com.example.onlinemarketplace.controller;

import com.example.onlinemarketplace.dto.response.BrandsDto;
import com.example.onlinemarketplace.dto.response.BrandsProductsDto;
import com.example.onlinemarketplace.model.Brand;
import com.example.onlinemarketplace.service.BrandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/brand")
public class BrandController {

    private final BrandService brandService;

    @GetMapping("/all")
    public List<BrandsDto> get() {
        return brandService.getAllBrands();
    }

    @GetMapping
    public List<BrandsDto> getByQuery(@RequestParam(required = false) String name) {
        if (name == null)
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "query can not be null");

        return brandService.getBrandByNameLike(name);
    }

    @GetMapping("/products")
    public List<BrandsProductsDto> getProductsByBrandsIds(@RequestParam(name = "id_in", required = false)
      String[] idsIn) {
        List<String> ids = idsIn != null ? Arrays.asList(idsIn) : Collections.emptyList();
        return brandService.getProductsByBrandIds(ids);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public Brand create(@Valid @RequestBody Brand brand) {
        return brandService.createNewBrand(brand);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping
    public boolean delete(@Valid @RequestBody Brand brand) {
        return brandService.deleteCategory(brand);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping
    public Brand update(@Valid @RequestBody Brand brand) {
        return brandService.updateBrand(brand);
    }
}
