package com.cuit.business.reservation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cuit.business.reservation.domain.Reservation;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ReservationMapper extends BaseMapper<Reservation> {
    Reservation selectBySId(Reservation reservation);
}
