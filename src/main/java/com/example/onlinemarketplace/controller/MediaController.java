package com.example.onlinemarketplace.controller;

import com.example.onlinemarketplace.service.impl.MediaServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/static")
public class MediaController {
    private final MediaServiceImpl mediaService;

    @GetMapping("/products/{filename}")
    public ResponseEntity<?> find(@PathVariable String filename) throws IOException {
        Objects.requireNonNull(filename, "filename cannot be null");
        return mediaService.getProductImageResponseEntity(filename);
    }

    @PostMapping("/products")
    public ResponseEntity<?> uploadProductImage(@RequestParam("file") MultipartFile imageFile) {
        if (imageFile == null || imageFile.isEmpty()) {
            return ResponseEntity.badRequest().body("Please select a file to upload");
        }

        return mediaService.addProductImageWithResponseEntity(imageFile);
    }
}
