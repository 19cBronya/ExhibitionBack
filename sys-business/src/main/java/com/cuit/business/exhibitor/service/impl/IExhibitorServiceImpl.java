package com.cuit.business.exhibitor.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cuit.business.booth.domain.Booth;
import com.cuit.business.booth.domain.BoothReservation;
import com.cuit.business.booth.mapper.BoothMapper;
import com.cuit.business.booth.mapper.BoothReservationMapper;
import com.cuit.business.exhibitionExhibitor.domain.ExhibitionExhibitor;
import com.cuit.business.exhibitionExhibitor.mapper.ExhibitionExhibitorMapper;
import com.cuit.business.exhibitor.domain.Exhibitor;
import com.cuit.business.exhibitor.domain.dto.ExhibitorDTO;
import com.cuit.business.exhibitor.mapper.ExhibitorMapper;
import com.cuit.business.exhibitor.service.IExhibitorService;
import com.cuit.business.product.domain.Product;
import com.cuit.business.product.mapper.ProductMapper;
import com.cuit.common.common.CommonConstants;
import com.cuit.common.common.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class IExhibitorServiceImpl extends ServiceImpl<ExhibitorMapper, Exhibitor> implements IExhibitorService {
    @Autowired
    private ExhibitorMapper exhibitorMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private BoothMapper boothMapper;

    @Autowired
    private BoothReservationMapper boothReservationMapper;

    @Autowired
    private ExhibitionExhibitorMapper exhibitionExhibitorMapper;

    @Override
    public R saveExhibitorInfor(Exhibitor exhibitor, Long uid) {
        exhibitor.setUid(uid);
        exhibitor.setCreateId(uid);
        exhibitor.setUpdateId(uid);
        int insert = exhibitorMapper.insert(exhibitor);
        if (insert > 0) {
            return R.success("保存成功");
        }
        return R.error(CommonConstants.ConstantsCode.错误.getValue(), "保存失败");
    }

    @Override
    public List<Long> selectIdByUid(Long uid) {
        return exhibitorMapper.selectIdByUid(uid);
    }

    @Override
    public List<ExhibitorDTO> selectList(Long id) {
        return exhibitorMapper.selectExhibitorListByEid(id);
    }

    @Override
    public List<ExhibitorDTO> selectListByPage(ExhibitorDTO exhibitorDTO) {
        return exhibitorMapper.selectListByPage(exhibitorDTO);
    }

    @Override
    public List<ExhibitorDTO> selectListByPageById(Long id) {
        return exhibitorMapper.selectListByPageById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R deleteByIds(String[] id,Long uid) {
        for (String i : id) {
            Exhibitor exhibitor = exhibitorMapper.selectById(Long.valueOf(i));
            exhibitor.setUpdateId(uid);
            exhibitor.setDelFlag(CommonConstants.DeleteFlag.删除.getValue());
            exhibitorMapper.updateById(exhibitor);
            /**
             * 商品信息
             */
            List<Product> productList = productMapper.getByEid(Long.valueOf(i));
            if (productList!=null && productList.size() > 0){
                for (Product product : productList) {
                    product.setUpdateId(uid);
                    product.setDelFlag(CommonConstants.DeleteFlag.删除.getValue());
                    productMapper.updateById(product);
                }
            }
            /**
             * 展位信息
             */
            List<BoothReservation> boothReservations = boothReservationMapper.getByEid(Long.valueOf(i));
            if (boothReservations!=null && boothReservations.size() > 0){
                for (BoothReservation boothReservation : boothReservations) {
                    boothReservation.setUpdateId(uid);
                    boothReservation.setDelFlag(CommonConstants.DeleteFlag.删除.getValue());
                    boothReservationMapper.updateById(boothReservation);

                    Booth booth = boothMapper.selectById(boothReservation.getBid());
                    if (booth!=null){
                        booth.setSoldStatus(CommonConstants.SoldStatus.未售出.getValue());
                        boothMapper.updateById(booth);
                    }
                }
            }
            /**
             * 展会关联信息
             */
            List<ExhibitionExhibitor> exhibitionExhibitors = exhibitionExhibitorMapper.getByEid(Long.valueOf(i));
            if (exhibitionExhibitors!=null && exhibitionExhibitors.size() > 0){
                for (ExhibitionExhibitor exhibitionExhibitor : exhibitionExhibitors) {
                    exhibitionExhibitor.setDelFlag(CommonConstants.DeleteFlag.删除.getValue());
                    exhibitionExhibitorMapper.updateById(exhibitionExhibitor);
                }
            }
        }
        return R.success("删除成功");

    }
}
