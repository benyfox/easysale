package com.example.onlinemarketplace.service;

import com.example.onlinemarketplace.dto.response.BrandsDto;
import com.example.onlinemarketplace.dto.response.BrandsProductsDto;
import com.example.onlinemarketplace.dto.response.ProductDto;
import com.example.onlinemarketplace.model.Brand;
import org.springframework.data.domain.Page;


import java.util.List;

public interface BrandService {
    List<BrandsDto> getAllBrands();

    Brand createNewBrand(Brand brand);

    Brand updateBrand(Brand brand);

    Boolean deleteCategory(Brand brand);

    boolean existsById(Long id);

    List<BrandsDto> getBrandByNameLike(String name);

    List<BrandsProductsDto> getProductsByBrandIds(List<String> ids);
}
