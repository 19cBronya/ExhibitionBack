package com.cuit.business.organization.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cuit.business.organization.domain.Organization;
import com.cuit.business.organization.domain.dto.OrganizationDTO;
import com.cuit.business.organization.mapper.OrganizationMapper;
import com.cuit.business.organization.service.IOrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrganizationServiceImpl extends ServiceImpl<OrganizationMapper, Organization> implements IOrganizationService {

    @Autowired OrganizationMapper organizationMapper;
    @Override
    public List<OrganizationDTO> selectListByPage(OrganizationDTO organizationDTO) {
        return organizationMapper.selectListByPage(organizationDTO);
    }

    @Override
    public Organization selectByUid(Long uid) {
        return organizationMapper.selectByUid(uid);
    }
}
