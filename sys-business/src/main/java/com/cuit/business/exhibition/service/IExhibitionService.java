package com.cuit.business.exhibition.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cuit.business.exhibition.domain.Exhibition;
import com.cuit.business.exhibition.domain.dto.ExhibitionDTO;
import com.cuit.business.exhibitionHall.domain.dto.TypeData;
import com.cuit.common.common.R;

import java.util.List;


public interface IExhibitionService extends IService<Exhibition> {
    List<ExhibitionDTO> selectList(ExhibitionDTO exhibitionDTO);

    R saveExhibitionInfor(Exhibition exhibition,Long uid);

    List<String> selectExhibitionType();

    R deleteByIds(Long[] id);

    ExhibitionDTO selectById(Long id);

    List<ExhibitionDTO> selectListById(ExhibitionDTO exhibitionDTO, Long uid);

    List<ExhibitionDTO> selectExhibitionListPageByHid(ExhibitionDTO exhibitionDTO, Long valueOf);

    List<TypeData> getTypeData(List<Long> hids);

    List<Long> selectEidByHids(List<Long> hids);

    List<Long> selectEidByUid(Long id);
}
