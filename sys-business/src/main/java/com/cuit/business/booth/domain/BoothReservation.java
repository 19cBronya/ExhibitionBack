package com.cuit.business.booth.domain;

import com.cuit.system.base.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author xym
 * @since 2024-03-04 11:34
 * @description 展位预约实体类
 */
@Data
public class BoothReservation extends BaseEntity {

    /*关联展览会id*/
    private Long eid;

    /*参展商id*/
    private Long etorid;

    /*展位id*/
    private Long bid;

    /*展位名*/
    private String boothName;

    /*展位编码*/
    private String boothCode;

    /*展位租赁价格*/
    private BigDecimal price;

    /*展位描述*/
    private String description;

    /*状态标志*/
    private String status;

    /*创建人id*/
    private Long createId;

    /*修改人id*/
    private Long updateId;
}
