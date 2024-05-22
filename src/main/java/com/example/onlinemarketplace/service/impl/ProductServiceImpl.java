package com.example.onlinemarketplace.service.impl;

import com.example.onlinemarketplace.dto.request.ProductDeleteRequest;
import com.example.onlinemarketplace.dto.request.ProductRequest;
import com.example.onlinemarketplace.dto.response.ProductDto;
import com.example.onlinemarketplace.exception.*;
import com.example.onlinemarketplace.model.*;
import com.example.onlinemarketplace.repository.CategoryRepository;
import com.example.onlinemarketplace.repository.ProductRepository;
import com.example.onlinemarketplace.repository.SellerRepository;
import com.example.onlinemarketplace.repository.UserRepository;
import com.example.onlinemarketplace.service.CollectionService;
import com.example.onlinemarketplace.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SellerRepository sellerRepository;
    private final UserRepository userRepository;
    private final CollectionService collectionService;
    private final ModelMapper modelMapper;
    private final MediaServiceImpl mediaService;

    @Override
    public ProductDto findById(Long id) {
        Objects.requireNonNull(id, "id cannot be null");
        return convertToProductDto(productRepository.findById(id).orElseThrow(ProductNotFoundException::new));
    }

    @Override
    public List<ProductDto> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::convertToProductDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<ProductDto> getAllProductsPageable(Pageable paging, Float price_gt, Float price_lt) {
        Page<Product> page = productRepository.getProductsByPriceBetween(paging, price_gt, price_lt);
        return new PageImpl<>(page.getContent().stream()
                .map(this::convertToProductDto)
                .collect(Collectors.toList()), paging, page.getTotalElements());
    }

    @Override
    public Page<ProductDto> getListByNameLike(String name, Pageable paging) {
        Page<Product> page = productRepository.findAllByNameContains(name, paging);
        return new PageImpl<>(page.getContent().stream()
                .map(this::convertToProductDto)
                .collect(Collectors.toList()), paging, page.getTotalElements());
    }

    @Override
    public ProductDto findByNameAndSeller(String name, Seller seller) {
        return convertToProductDto(productRepository.findByNameAndSeller(name, seller));
    }

    @Override
    @Transactional
    public ProductDto createNewProduct(String sellerUsername, ProductRequest productRequest) {
        Product product = convertToProduct(productRequest);

        Seller seller = sellerRepository.findByUserUsername(sellerUsername).orElseThrow(SellerNotFoundException::new);

        String thumbnailFilename = "";
        List<ProductImage> savedImages = new ArrayList<>();
        try {
            thumbnailFilename = uploadProductImage(productRequest.getThumbnailFile());

            ProductThumbnail thumbnail = new ProductThumbnail(product);
            thumbnail.setUrl("/static/products/" + thumbnailFilename);

            for (MultipartFile imageFile : productRequest.getImageFileList()) {
                String imageFilename = uploadProductImage(imageFile);

                ProductImage productImage = new ProductImage(product);
                productImage.setUrl("/static/products/" + imageFilename);

                savedImages.add(productImage);
            }

            product.setImageList(savedImages);
            product.setThumbnail(thumbnail);
            product.setSeller(seller);

            product = productRepository.save(product);
            collectionService.editNewArrivals();
            return convertToProductDto(product);
        } catch (Exception e) {
            log.error("Error creating product: {}", e.getMessage());
            if (!thumbnailFilename.isEmpty()) {
                mediaService.deleteProductImage(thumbnailFilename);
            }

            savedImages.forEach(productImage -> {
                String url = productImage.getUrl();
                String filename = url.substring(url.lastIndexOf("/") + 1);
                mediaService.deleteProductImage(filename);
            });

            throw new ProductCreateException();
        }
    }


    private String uploadProductImage(MultipartFile imageFile) throws IOException {
        String filename = "";

        if (imageFile != null && !imageFile.isEmpty()) {
            filename = mediaService.addProductImage(imageFile);
        }

        if (filename.isEmpty()) {
            throw new IOException("Cannot save image");
        }

        return filename;
    }

    @Override
    @Transactional
    public Product updateProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    @Transactional
    public void deleteProduct(ProductDeleteRequest product, String username) {
        Objects.requireNonNull(product, "product cannot be null");
        Seller seller = sellerRepository.findByUserUsername(username)
                .orElseThrow(SellerNotFoundException::new);
        Product productToDelete = productRepository.findById(product.getId())
                .orElseThrow(ProductNotFoundException::new);

        if (!productToDelete.getSeller().equals(seller)) {
            throw new ProductNotFoundException();
        }

        deleteProductImages(productToDelete);

        productRepository.delete(productToDelete);
    }

    private void deleteProductImages(Product product) {
        if (product.getThumbnail() != null) {
            String thumbnailUrl = product.getThumbnail().getUrl();
            String thumbnailFilename = thumbnailUrl.substring(thumbnailUrl.lastIndexOf("/") + 1);
            mediaService.deleteProductImage(thumbnailFilename);
        }

        product.getImageList().forEach(productImage -> {
            String imageUrl = productImage.getUrl();
            String imageFilename = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
            mediaService.deleteProductImage(imageFilename);
        });
    }

    @Override
    public Product findByName(String name) {
        Objects.requireNonNull(name, "name cannot be null");
        return productRepository.findByName(name).orElseThrow(ProductNotFoundException::new);
    }

    @Override
    public Page<ProductDto> getProductsBySeller(Seller seller, Pageable paging) {
        Objects.requireNonNull(seller, "seller can not be null");

        Page<Product> page = productRepository.getAllBySeller(seller, paging);
        return new PageImpl<>(page.getContent().stream()
                .map(this::convertToProductDto)
                .collect(Collectors.toList()), paging, page.getTotalElements());
    }

    @Override
    public List<Product> getProductsBySeller(Seller seller) {
        return productRepository.getAllBySeller(seller);
    }

    @Override
    public Page<ProductDto> getProductsBySellerUsername(String username, Pageable paging) {
        Objects.requireNonNull(username, "username can not be null");

        Seller seller = sellerRepository.findByUserUsername(username)
                .orElseThrow(SellerNotFoundException::new);



        return getProductsBySeller(seller, paging);
    }

    public long getAllProductsCount() {
      return productRepository.count();
    }

    @Override
    public Page<ProductDto> getProductsByCategory(String categoryStr, Pageable paging) {
        Objects.requireNonNull(categoryStr, "category can not be null");

        Category category = categoryRepository.getCategoryBySlug(categoryStr)
                .orElseThrow(CategoryNotFoundException::new);

        return productRepository.getProductsByCategory(category, paging)
                .stream()
                .map(this::convertToProductDto)
                .collect(Collectors.collectingAndThen(Collectors.toList(),
                        list -> new PageImpl<>(list, paging, list.size())));
    }

    @Override
    public Page<ProductDto> getProductsByBrandId(List<String> idsList, Pageable paging) {
        if (idsList.isEmpty()) {
            return new PageImpl<>(Collections.emptyList(), paging, 0);
        }

        List<Long> ids = idsList.stream()
                .map(Long::parseLong)
                .collect(Collectors.toList());

        return productRepository.findAllByBrand_IdIn(ids, paging)
                .stream()
                .map(this::convertToProductDto)
                .collect(Collectors.collectingAndThen(Collectors.toList(),
                        list -> new PageImpl<>(list, paging, list.size())));
    }

    @Override
    public boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }

    public ProductDto convertToProductDto(Product product) {
        return modelMapper.map(product, ProductDto.class);
    }
    public Product convertToProduct(ProductRequest product) {
        return modelMapper.map(product, Product.class);
    }
}
