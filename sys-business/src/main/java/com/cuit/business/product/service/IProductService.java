package com.cuit.business.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cuit.business.product.domain.Product;
import com.cuit.business.product.domain.dto.ProductDTO;
import com.cuit.common.common.R;

import java.util.List;

public interface IProductService extends IService<Product> {
    List<ProductDTO> selectByPage(Long uid, ProductDTO productDTO);

    R deleteById(Long[] id);

    R forbiddenStatus(String status, Long[] id);

    List<ProductDTO> selectByPageByEid(Long valueOf);
}
