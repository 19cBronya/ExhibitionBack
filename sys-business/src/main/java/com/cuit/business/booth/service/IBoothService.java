package com.cuit.business.booth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cuit.business.booth.domain.Booth;
import com.cuit.business.booth.domain.BoothReservation;
import com.cuit.business.exhibitor.domain.Exhibitor;
import com.cuit.business.exhibitor.domain.dto.ExhibitorDTO;

import java.util.List;


public interface IBoothService extends IService<Booth> {
    List<Booth> selectListByEid(Long id);

    List<Booth> orgSelectList(Long id);
}
