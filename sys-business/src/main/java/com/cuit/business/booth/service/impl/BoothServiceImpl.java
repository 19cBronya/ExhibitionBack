package com.cuit.business.booth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cuit.business.booth.domain.Booth;
import com.cuit.business.booth.domain.BoothReservation;
import com.cuit.business.booth.mapper.BoothMapper;
import com.cuit.business.booth.service.IBoothService;
import com.cuit.business.exhibitor.domain.Exhibitor;
import com.cuit.business.exhibitor.domain.dto.ExhibitorDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoothServiceImpl extends ServiceImpl<BoothMapper, Booth> implements IBoothService {

    @Autowired
    private BoothMapper boothMapper;

    @Override
    public List<Booth> selectListByEid(Long id) {
        return boothMapper.selectListByEid(id);
    }


    @Override
    public List<Booth> orgSelectList(Long id) {
        return boothMapper.selectListByEidStatus(id);
    }
}
