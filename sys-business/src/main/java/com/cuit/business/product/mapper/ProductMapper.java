package com.cuit.business.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cuit.business.product.domain.Product;
import com.cuit.business.product.domain.dto.ProductDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductMapper extends BaseMapper<Product> {
    List<ProductDTO> selectByPage(@Param("uid") Long uid,@Param("productDTO") ProductDTO productDTO);

    List<Product> getByEid(Long etorid);

    List<ProductDTO> selectByPageByEid(Long id);
}
