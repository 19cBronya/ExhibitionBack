package com.cuit.business.product.domain;

import com.cuit.system.base.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class Product extends BaseEntity {

    /**
     * 参展商id
     */
    private Long etorid;

    /**
     * 商品名
     */
    private String productName;

    /**
     * 商品介绍
     */
    private String productInfo;

    /**
     * 展品售价
     */
    private BigDecimal productPrice;

    /**
     * 商品图片
     */
    private String productUrl;

    /**
     * 商品库存
     */
    private Integer productStock;

    /**
     * 商品状态
     */
    private String status;

    /*创建人id*/
    private Long createId;

    /*修改人id*/
    private Long updateId;
}
