package com.cuit.business.exhibition.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cuit.business.exhibition.domain.Exhibition;
import com.cuit.business.exhibition.domain.dto.ExhibitionDTO;
import com.cuit.business.exhibitionHall.domain.dto.ExhibitionHallDTO;
import com.cuit.business.exhibitionHall.domain.dto.TypeData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ExhibitionMapper extends BaseMapper<Exhibition> {
    List<ExhibitionDTO> selectListByPage(ExhibitionDTO exhibitionDTO);

    List<String> selectExhibitionType();

    List<Exhibition> selectListByEid(Long id);

    List<ExhibitionDTO> selectExhibitionListByHid(Long hid);

    ExhibitionDTO selectExhibitionById(Long id);

    List<ExhibitionDTO> selectListById(@Param("exhibitionDTO") ExhibitionDTO exhibitionDTO,@Param("uid") Long uid);

    List<ExhibitionDTO> selectExhibitionListPageByHid(@Param("exhibitionDTO")ExhibitionDTO exhibitionDTO,@Param("id") Long id);

    List<TypeData> getTypeData(@Param("hids") List<Long> hids);

    List<Long> selectEidByHids(@Param("hids") List<Long> hids);

    List<Long> selectEidByUid(Long id);
}
