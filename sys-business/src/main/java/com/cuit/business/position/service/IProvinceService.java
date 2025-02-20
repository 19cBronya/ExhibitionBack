package com.cuit.business.position.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cuit.business.position.domain.Province;
import com.cuit.business.position.domain.dto.PositionDTO;

import java.util.List;

public interface IProvinceService extends IService<Province> {
    List<PositionDTO> selectProcinceList();
}
