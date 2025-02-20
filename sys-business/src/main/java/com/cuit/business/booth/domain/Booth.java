package com.cuit.business.booth.domain;

import com.cuit.system.base.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author xym
 * @since 2024-01-16 16:16
 * @description 展位实体类
 */

@Data
public class Booth extends BaseEntity {

    /*展会id*/
    private Long eid;

    /*展位编码*/
    private String boothCode;

    /*展位租赁价格*/
    private BigDecimal price;

    /*售出状态*/
    private String soldStatus;

    /*状态标志*/
    private String status;

    /*创建人id*/
    private Long createId;

    /*修改人id*/
    private Long updateId;
}
