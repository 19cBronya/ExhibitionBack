package com.cuit.business.booth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cuit.business.booth.domain.Booth;
import com.cuit.business.booth.domain.BoothReservation;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BoothReservationMapper extends BaseMapper<BoothReservation> {
    List<BoothReservation> getByEid(Long etorid);

    BoothReservation selectByBid(Long id);
}
