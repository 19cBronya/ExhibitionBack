package com.cuit.business.organization.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cuit.business.organization.domain.Organization;
import com.cuit.business.organization.domain.dto.OrganizationDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrganizationMapper extends BaseMapper<Organization> {
    List<OrganizationDTO> selectListByPage(OrganizationDTO organizationDTO);

    Organization selectByUid(Long uid);
}
