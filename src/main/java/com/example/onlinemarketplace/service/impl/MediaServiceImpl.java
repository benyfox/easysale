package com.example.onlinemarketplace.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@AllArgsConstructor
@Slf4j
@Service
public class MediaServiceImpl {

    public ResponseEntity<?> getProductImageResponseEntity(String name) throws IOException {
        Path path = new File("src/main/resources/static/products/" + name).toPath();

        FileSystemResource file = new FileSystemResource(path);

        if (!file.exists()) {
            return ResponseEntity.badRequest().body("File not found!");
        }


            return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(Files.probeContentType(path)))
                .body(file);

    }

    public ResponseEntity<?> addProductImageWithResponseEntity(MultipartFile imageFile) {
        String filename;
        filename = addProductImage(imageFile);

        if (filename.isEmpty()) {
            log.error("Error uploading image: {}", filename);
            return ResponseEntity.badRequest().body("Error uploading image");
        }

        filename = addProductImage(imageFile);
        return ResponseEntity.ok(filename);

    }

    public String addProductImage(MultipartFile imageFile) {
        String filename = generateString() + getFileExtension(imageFile.getOriginalFilename());
        Path filePath = Paths.get(
                "src/main/resources/static/products/", filename);

        try {
            Files.createDirectories(filePath.getParent());
            imageFile.transferTo(filePath);
            log.info("Image uploaded successfully: {}", filename);
            return filename;
        } catch (IOException e) {
            log.error("Error uploading image: {}", filename, e);
            return "";
        }
    }

    public void deleteProductImage(String name) {
        String inputFile = "src/main/resources/static/products/" + name;
        Path path = new File(inputFile).toPath();

        if (!Files.exists(path)) {
            ResponseEntity.badRequest().body("Image not found!");
        }

        try {
            Files.delete(path);
            log.info("Image deleted successfully: {}", name);
            ResponseEntity.ok("Image deleted successfully");
        } catch (IOException e) {
            log.error("Error deleting image: {}", name, e);
            ResponseEntity.badRequest().body("Error deleting image");
        }
    }

    private String getFileExtension(String name) {
        String extension;
        try {
            extension = name.substring(name.lastIndexOf("."));
        } catch (Exception e) {
            extension = "";
        }

        return extension;
    }

    private String generateString() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
