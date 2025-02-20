package com.cuit.business.booth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cuit.business.booth.domain.BoothReservation;

public interface IBoothReservationService extends IService<BoothReservation> {
    BoothReservation selectByBid(Long id);
}
