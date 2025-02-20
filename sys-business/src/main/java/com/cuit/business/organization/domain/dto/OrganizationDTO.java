package com.cuit.business.organization.domain.dto;

import com.cuit.business.organization.domain.Organization;
import lombok.Data;

@Data
public class OrganizationDTO extends Organization {
    /**
     * 机构负责人
     */
    private String managerName;
}
