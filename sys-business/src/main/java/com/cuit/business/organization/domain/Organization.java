package com.cuit.business.organization.domain;

import com.cuit.system.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xym
 * @since 2023-11-15 10:42
 * @description 参展机构实体类信息
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Organization extends BaseEntity {

    /*办展机构所属人id*/
    private Long uid;

    /*办展机构名称*/
    private String name;

    /*状态标志 0正常 1禁用*/
    private String status;

    /*创建人id*/
    private Long createId;

    /*修改人id*/
    private Long updateId;
}
