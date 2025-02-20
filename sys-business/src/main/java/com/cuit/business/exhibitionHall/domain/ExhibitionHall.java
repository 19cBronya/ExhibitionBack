package com.cuit.business.exhibitionHall.domain;

import com.cuit.system.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * @author xym
 * @since 2023-11-15 10:42
 * @description 展览馆实体类信息
 */

@Data
public class ExhibitionHall extends BaseEntity {

    /*展览馆负责人id*/
    private Long uid;

    /*展馆图片信息*/
    private String picUrl;

    /*经度*/
    private String longitude;

    /*纬度*/
    private String latitude;

    /*展览馆名*/
    private String name;

    /*展览馆地址*/
    private String address;

    /*最大容量数（展位数）*/
    private Long capacityNumber;

    /*开馆时间*/
    @JsonFormat(pattern = "HH:mm:ss", timezone = "GMT+8")
    private String openingTime;

    /*闭馆时间*/
    @JsonFormat(pattern = "HH:mm:ss", timezone = "GMT+8")
    private String closingTime;

    /*状态标志 0正常 1禁用*/
    private String status;

    /*创建人id*/
    private Long createId;

    /*修改人id*/
    private Long updateId;
}
