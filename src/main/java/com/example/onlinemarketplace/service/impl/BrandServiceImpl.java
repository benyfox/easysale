package com.example.onlinemarketplace.service.impl;

import com.example.onlinemarketplace.dto.response.BrandsDto;
import com.example.onlinemarketplace.dto.response.BrandsProductsDto;
import com.example.onlinemarketplace.model.Brand;
import com.example.onlinemarketplace.repository.BrandRepository;
import com.example.onlinemarketplace.service.BrandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@RequiredArgsConstructor
@Slf4j
@Service
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<BrandsDto> getAllBrands() {
        return brandRepository
                .findAll()
                .stream()
                .map(this::convertToBrandDto)
                .collect(Collectors.toList());
    }

    @Override
    public Brand createNewBrand(Brand brand) {
        return brandRepository.save(brand);
    }

    @Override
    public Brand updateBrand(Brand brand) {
        return brandRepository.save(brand);
    }

    @Override
    public Boolean deleteCategory(Brand brand) {
        brandRepository.delete(brand);
        return true;
    }

    @Override
    public boolean existsById(Long id) {
        return brandRepository.existsById(id);
    }

    @Override
    public List<BrandsDto> getBrandByNameLike(String name) {
        return brandRepository
                .findAllByNameContains(name)
                .stream()
                .map(this::convertToBrandDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BrandsProductsDto> getProductsByBrandIds(List<String> ids) {
        List<Long> longIds = ids.stream().map(Long::parseLong).collect(Collectors.toList());
        return brandRepository
                .findAllByIdIn(longIds)
                .stream()
                .map(this::convertToBrandProductDto)
                .collect(Collectors.toList());
    }

    private BrandsDto convertToBrandDto(Brand brand) {
        return modelMapper.map(brand, BrandsDto.class);
    }

    private BrandsProductsDto convertToBrandProductDto(Brand brand) {
        return modelMapper.map(brand, BrandsProductsDto.class);
    }
}
