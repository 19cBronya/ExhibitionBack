package com.cuit.business.exhibitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cuit.business.exhibitor.domain.Exhibitor;
import com.cuit.business.exhibitor.domain.dto.ExhibitorDTO;
import com.cuit.common.common.R;

import java.util.List;

public interface IExhibitorService extends IService<Exhibitor> {
    R saveExhibitorInfor(Exhibitor exhibitor, Long uid);

    List<Long> selectIdByUid(Long uid);

    List<ExhibitorDTO> selectList(Long id);

    List<ExhibitorDTO> selectListByPage(ExhibitorDTO exhibitorDTO);

    List<ExhibitorDTO> selectListByPageById(Long id);

    R deleteByIds(String[] id,Long uid);
}
