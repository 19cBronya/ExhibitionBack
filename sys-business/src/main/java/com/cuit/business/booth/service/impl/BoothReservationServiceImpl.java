package com.cuit.business.booth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cuit.business.booth.domain.Booth;
import com.cuit.business.booth.domain.BoothReservation;
import com.cuit.business.booth.mapper.BoothMapper;
import com.cuit.business.booth.mapper.BoothReservationMapper;
import com.cuit.business.booth.service.IBoothReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BoothReservationServiceImpl extends ServiceImpl<BoothReservationMapper, BoothReservation> implements IBoothReservationService {

    @Autowired
    private BoothReservationMapper boothReservationMapper;
    @Override
    public BoothReservation selectByBid(Long id) {
        return boothReservationMapper.selectByBid(id);
    }
}
