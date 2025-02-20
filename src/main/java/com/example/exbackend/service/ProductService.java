package com.example.exbackend.service;

import com.example.exbackend.model.Product;
import com.example.exbackend.model.User;

import java.util.List;

public interface ProductService {
    Product uploadProduct(Product product, User exhibitor);
    List<Product> getProductsByExhibitor(User exhibitor);
}