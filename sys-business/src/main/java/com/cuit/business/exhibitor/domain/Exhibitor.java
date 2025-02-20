package com.cuit.business.exhibitor.domain;

import com.cuit.system.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xym
 * @since 2023-12-04 16:26
 * @description 参展商实体类
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Exhibitor extends BaseEntity {

    /*参展商id*/
    private Long uid;

    /*公司图片*/
    private String companyLogo;

    /*公司名称*/
    private String companyName;

    /*公司介绍*/
    private String companyInfo;

    /*状态标志 0正常 1禁用*/
    private String status;

    /*创建人id*/
    private Long createId;

    /*修改人id*/
    private Long updateId;
}
