package com.cuit.business.exhibition.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cuit.business.booth.domain.Booth;
import com.cuit.business.booth.mapper.BoothMapper;
import com.cuit.business.exhibition.domain.Exhibition;
import com.cuit.business.exhibition.domain.dto.ExhibitionDTO;
import com.cuit.business.exhibition.mapper.ExhibitionMapper;
import com.cuit.business.exhibition.service.IExhibitionService;
import com.cuit.business.exhibitionExhibitor.domain.ExhibitionExhibitor;
import com.cuit.business.exhibitionExhibitor.mapper.ExhibitionExhibitorMapper;
import com.cuit.business.exhibitionHall.domain.dto.TypeData;
import com.cuit.business.reservation.domain.Reservation;
import com.cuit.business.reservation.mapper.ReservationMapper;
import com.cuit.common.common.CommonConstants;
import com.cuit.common.common.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExhibitionServiceImpl extends ServiceImpl<ExhibitionMapper, Exhibition> implements IExhibitionService {

    @Autowired
    private ExhibitionMapper exhibitionMapper;

    @Autowired
    private BoothMapper boothMapper;

    @Autowired
    private ExhibitionExhibitorMapper exhibitionExhibitorMapper;

    @Autowired
    private ReservationMapper reservationMapper;

    @Override
    public List<ExhibitionDTO> selectList(ExhibitionDTO exhibitionDTO) {
        return exhibitionMapper.selectListByPage(exhibitionDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R saveExhibitionInfor(Exhibition exhibition, Long uid) {
        exhibition.setStatus(CommonConstants.Status.审核中.getValue());//需管理员审核
        exhibition.setCreateId(uid);
        exhibition.setUpdateId(uid);
        int insert = exhibitionMapper.insert(exhibition);

        /*展位信息插入*/
        int exhibitionNum = exhibition.getExhibitionNum();
        List<String> boothCodes = generateBoothCodes(exhibitionNum); // 生成展位编码列表
        for (int i = 0; i < exhibitionNum; i++) {
            // 创建展位对象
            Booth booth = new Booth();
            // 设置展位属性值
            booth.setEid(exhibition.getId());
            booth.setBoothCode(boothCodes.get(i)); // 获取对应位置的展位编码
            booth.setPrice(exhibition.getTicketPrice().divide(new BigDecimal(4))); // 设置展位租赁价格为门票价格的四分之一
            booth.setSoldStatus(CommonConstants.SoldStatus.未售出.getValue()); // 设置售出状态为未售出
            booth.setStatus(CommonConstants.Status.审核中.getValue()); // 设置状态标志为审核中
            booth.setCreateId(uid);
            booth.setUpdateId(uid);
            // 插入展位对象到展位表中
            boothMapper.insert(booth);
        }

        if (insert > 0) {
            return R.success("保存成功");
        }
        return R.error(CommonConstants.ConstantsCode.错误.getValue(), "保存失败");
    }

    @Override
    public List<String> selectExhibitionType() {
        return exhibitionMapper.selectExhibitionType();
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public R deleteByIds(Long[] id) {
        boolean hasExhibitionInOpenTime = false;
        for (Long i : id) {
            //展会删除
            Exhibition exhibition = exhibitionMapper.selectById(i);
            LocalDateTime currentTime = LocalDateTime.now(); // 获取当前时间

            if (currentTime.isBefore(exhibition.getOpeningTime()) || currentTime.isAfter(exhibition.getClosingTime())) {
                exhibition.setDelFlag(CommonConstants.DeleteFlag.删除.getValue());
                exhibitionMapper.updateById(exhibition);
                //预约表信息删除
                Reservation reservation = new Reservation();
                reservation.setEid(i);
                reservation = reservationMapper.selectBySId(reservation);
                if(reservation!=null){
                    reservation.setDelFlag(CommonConstants.DeleteFlag.删除.getValue());
                    reservationMapper.updateById(reservation);
                }
                //展位删除
                List<Booth> boothList = boothMapper.selectListByEid(i);
                if(boothList.size()>0){
                    for (Booth booth : boothList) {
                        booth.setDelFlag(CommonConstants.DeleteFlag.删除.getValue());
                        boothMapper.updateById(booth);
                    }
                }
                //关联表信息删除
                ExhibitionExhibitor exhibitionExhibitor = new ExhibitionExhibitor();
                exhibitionExhibitor.setEid(i);
                exhibitionExhibitor = exhibitionExhibitorMapper.selectBySId(exhibitionExhibitor);
                if(exhibitionExhibitor!=null){
                    exhibitionExhibitor.setDelFlag(CommonConstants.DeleteFlag.删除.getValue());
                    exhibitionExhibitorMapper.updateById(exhibitionExhibitor);
                }
            } else {
                hasExhibitionInOpenTime = true;
            }
        }
        if (hasExhibitionInOpenTime) {
            return R.error("存在展会在开放时间和关闭时间之间，无法删除");
        } else {
            return R.success("删除成功");
        }
    }

    @Override
    public ExhibitionDTO selectById(Long id) {
        return exhibitionMapper.selectExhibitionById(id);
    }

    @Override
    public List<ExhibitionDTO> selectListById(ExhibitionDTO exhibitionDTO, Long uid) {
        return exhibitionMapper.selectListById(exhibitionDTO,uid);
    }

    @Override
    public List<ExhibitionDTO> selectExhibitionListPageByHid(ExhibitionDTO exhibitionDTO, Long id) {
        return exhibitionMapper.selectExhibitionListPageByHid(exhibitionDTO,id);
    }

    @Override
    public List<TypeData> getTypeData(List<Long> hids) {
        return exhibitionMapper.getTypeData(hids);
    }

    @Override
    public List<Long> selectEidByHids(List<Long> hids) {
        return exhibitionMapper.selectEidByHids(hids);
    }

    @Override
    public List<Long> selectEidByUid(Long id) {
        return exhibitionMapper.selectEidByUid(id);
    }


    // 生成展位编码
    private List<String> generateBoothCodes(int exhibitionNum) {
        List<String> boothCodes = new ArrayList<>();
        int number = 1; // 初始数字

        for (int i = 0; i < exhibitionNum; i++) {
            for (char boothChar = 'A'; boothChar <= 'Z'; boothChar++) {
                boothCodes.add(boothChar + String.valueOf(number)); // 添加展位编号到列表中
            }
            number++;
        }
        return boothCodes;
    }
}
