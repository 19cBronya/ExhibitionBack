package com.cuit.business.product.domain.dto;

import com.cuit.business.product.domain.Product;
import lombok.Data;

@Data
public class ProductDTO extends Product {
    /**
     * 所属参展商名
     */
    private String companyName;
}
