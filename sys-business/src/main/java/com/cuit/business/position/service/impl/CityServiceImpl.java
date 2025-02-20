package com.cuit.business.position.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cuit.business.position.domain.City;
import com.cuit.business.position.domain.dto.PositionDTO;
import com.cuit.business.position.mapper.CityMapper;
import com.cuit.business.position.service.ICityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityServiceImpl extends ServiceImpl<CityMapper, City> implements ICityService {
    @Autowired
    private CityMapper cityMapper;
    @Override
    public List<PositionDTO> selectCityList(Integer id) {
        return cityMapper.selectCityList(id);
    }
}
