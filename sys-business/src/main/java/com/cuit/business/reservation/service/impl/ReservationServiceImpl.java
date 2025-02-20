package com.cuit.business.reservation.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cuit.business.reservation.domain.Reservation;
import com.cuit.business.reservation.mapper.ReservationMapper;
import com.cuit.business.reservation.service.IReservationService;
import org.springframework.stereotype.Service;

@Service
public class ReservationServiceImpl extends ServiceImpl<ReservationMapper, Reservation> implements IReservationService {
}
