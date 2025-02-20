package com.example.exbackend.controller;

import com.example.exbackend.model.Product;
import com.example.exbackend.model.User;
import com.example.exbackend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/upload")
    public ResponseEntity<Product> uploadProduct(@RequestBody Product product) {
        User exhibitor = getCurrentUser();
        Product uploadedProduct = productService.uploadProduct(product, exhibitor);
        return ResponseEntity.ok(uploadedProduct);
    }

    @GetMapping("/my-products")
    public ResponseEntity<List<Product>> getMyProducts() {
        User exhibitor = getCurrentUser();
        List<Product> myProducts = productService.getProductsByExhibitor(exhibitor);
        return ResponseEntity.ok(myProducts);
    }

    private User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}