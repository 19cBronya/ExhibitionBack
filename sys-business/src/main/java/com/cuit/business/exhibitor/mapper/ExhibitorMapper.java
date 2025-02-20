package com.cuit.business.exhibitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cuit.business.exhibitor.domain.Exhibitor;
import com.cuit.business.exhibitor.domain.dto.ExhibitorDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ExhibitorMapper extends BaseMapper<Exhibitor>{
    List<Long> selectIdByUid(Long uid);

    List<ExhibitorDTO> selectExhibitorListByEid(Long id);

    List<ExhibitorDTO> selectListByPage(ExhibitorDTO exhibitorDTO);

    List<ExhibitorDTO> selectListByPageById(Long id);
}
