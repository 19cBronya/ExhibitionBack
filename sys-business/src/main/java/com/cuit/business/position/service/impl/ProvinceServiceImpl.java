package com.cuit.business.position.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cuit.business.position.domain.Province;
import com.cuit.business.position.domain.dto.PositionDTO;
import com.cuit.business.position.mapper.CityMapper;
import com.cuit.business.position.mapper.CityProperMapper;
import com.cuit.business.position.mapper.ProvinceMapper;
import com.cuit.business.position.service.IProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProvinceServiceImpl extends ServiceImpl<ProvinceMapper, Province> implements IProvinceService {
    @Autowired
    private ProvinceMapper provinceMapper;
    @Autowired
    private CityMapper cityMapper;
    @Autowired
    private CityProperMapper cityProperMapper;

    @Override
    public List<PositionDTO> selectProcinceList() {
        return provinceMapper.selectProcinceList();
    }
}
