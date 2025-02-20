package com.example.exbackend.service.impl;

import com.example.exbackend.model.Product;
import com.example.exbackend.model.User;
import com.example.exbackend.repository.ProductRepository;
import com.example.exbackend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Product uploadProduct(Product product, User exhibitor) {
        product.setExhibitor(exhibitor);
        return productRepository.save(product);
    }

    @Override
    public List<Product> getProductsByExhibitor(User exhibitor) {
        return productRepository.findByExhibitor(exhibitor);
    }
}