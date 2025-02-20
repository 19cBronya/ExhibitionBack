package com.cuit.business.exhibitionExhibitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cuit.business.exhibitionExhibitor.domain.ExhibitionExhibitor;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ExhibitionExhibitorMapper extends BaseMapper<ExhibitionExhibitor> {
    ExhibitionExhibitor selectBySId(ExhibitionExhibitor exhibitionExhibitor);

    List<ExhibitionExhibitor> getByEid(Long etorid);
}
