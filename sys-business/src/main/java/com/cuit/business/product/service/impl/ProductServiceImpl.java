package com.cuit.business.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cuit.business.product.domain.Product;
import com.cuit.business.product.domain.dto.ProductDTO;
import com.cuit.business.product.mapper.ProductMapper;
import com.cuit.business.product.service.IProductService;
import com.cuit.common.common.CommonConstants;
import com.cuit.common.common.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements IProductService {

    @Autowired
    private ProductMapper productMapper;
    @Override
    public List<ProductDTO> selectByPage(Long uid,ProductDTO productDTO) {
        return productMapper.selectByPage(uid,productDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R deleteById(Long[] id) {
        for (Long i : id) {
            Product product = productMapper.selectById(i);
            product.setDelFlag(CommonConstants.DeleteFlag.删除.getValue());
            productMapper.updateById(product);
        }
        return R.success("删除成功");
    }

    @Override
    public R forbiddenStatus(String status, Long[] id) {
        for (Long i : id) {
            Product product = productMapper.selectById(i);
            product.setStatus(status);
            productMapper.updateById(product);
        }
        if (status.equals(CommonConstants.Status.启用.getValue())){
            return R.success("启用通过");
        }
        return R.success("禁用成功");
    }

    @Override
    public List<ProductDTO> selectByPageByEid(Long id) {
        return productMapper.selectByPageByEid(id);
    }
}
