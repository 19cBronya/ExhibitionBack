package com.cuit.business.exhibitionHall.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.cuit.business.exhibition.domain.dto.ExhibitionDTO;
import com.cuit.business.exhibitionHall.domain.ExhibitionHall;
import com.cuit.business.exhibitionHall.domain.dto.ExhibitionHallDTO;
import com.cuit.business.organization.domain.dto.QueryDTO;
import com.cuit.common.common.R;
import com.cuit.system.sysUserRoles.domain.dto.SysUserRolesDTO;

import java.util.List;

public interface IExhibitionHallService extends IService<ExhibitionHall> {
    List<Long> selectUid(String name);

    List<ExhibitionHallDTO> selectExhibitionList(ExhibitionHallDTO exhibitionDTO);

    List<SysUserRolesDTO> selectEhibitionMg(SysUserRolesDTO sysUserRolesDTO);

    List<ExhibitionHall> selectListByTime(QueryDTO queryDTO);

    R saveExhibitionHallInfor(ExhibitionHall exhibitionHall, Long uid);

    List<ExhibitionHallDTO> selectList(ExhibitionHallDTO exhibitionHallDTO);

    R forbiddenStatus(String status, Long[] id, Long uid);

    R deleteByIds(Long[] id);

    R updateByStatus(String status, Long[] id);

    List<ExhibitionDTO> selectExhibitionListByHid(Long id);

    ExhibitionHallDTO selectExhibitionHallById(Long id);

    List<ExhibitionHallDTO> selectExhibitionHallByCreateId(Long uid, ExhibitionHallDTO exhibitionHallDTO);

    List<Long> selectHidByUid(Long uid);
}
