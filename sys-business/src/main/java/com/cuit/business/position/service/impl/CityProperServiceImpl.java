package com.cuit.business.position.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cuit.business.position.domain.CityProper;
import com.cuit.business.position.domain.dto.PositionDTO;
import com.cuit.business.position.mapper.CityProperMapper;
import com.cuit.business.position.service.ICityProperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityProperServiceImpl extends ServiceImpl<CityProperMapper, CityProper> implements ICityProperService {

    @Autowired
    private CityProperMapper cityProperMapper;
    @Override
    public List<PositionDTO> selectCityProperList(Integer id) {
        return cityProperMapper.selectCityProperList(id);
    }
}
