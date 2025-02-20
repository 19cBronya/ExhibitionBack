package com.cuit.business.organization.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.cuit.business.organization.domain.Organization;
import com.cuit.business.organization.domain.dto.OrganizationDTO;

import java.util.List;

public interface IOrganizationService extends IService<Organization> {
    List<OrganizationDTO> selectListByPage(OrganizationDTO organizationDTO);

    Organization selectByUid(Long uid);
}
