package com.cuit.business.exhibitionHall.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.cuit.business.exhibitionHall.domain.ExhibitionHall;
import com.cuit.business.exhibitionHall.domain.dto.ExhibitionHallDTO;
import com.cuit.business.organization.domain.dto.QueryDTO;
import com.cuit.common.common.R;
import com.cuit.system.sysUserRoles.domain.dto.SysUserRolesDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ExhibitionHallMapper extends BaseMapper<ExhibitionHall> {
    List<Long> selectUid(String name);

    List<ExhibitionHallDTO> selectExhibitionList(ExhibitionHallDTO exhibitionDTO);

    List<SysUserRolesDTO> selectEhibitionMg(SysUserRolesDTO sysUserRolesDTO);

    List<ExhibitionHall> selectListByTime(QueryDTO queryDTO);

    List<ExhibitionHallDTO> selectListByPage(ExhibitionHallDTO exhibitionHallDTO);

    ExhibitionHallDTO selectExhibitionHallById(Long id);

    List<ExhibitionHallDTO> selectExhibitionHallByCreateId(@Param("uid") Long uid, @Param("exhibitionHallDTO") ExhibitionHallDTO exhibitionHallDTO);

    List<Long> selectHidByUid(Long uid);
}
