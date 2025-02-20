package com.cuit.business.exhibitionHall.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cuit.business.booth.domain.Booth;
import com.cuit.business.booth.mapper.BoothMapper;
import com.cuit.business.exhibition.domain.Exhibition;
import com.cuit.business.exhibition.domain.dto.ExhibitionDTO;
import com.cuit.business.exhibition.mapper.ExhibitionMapper;
import com.cuit.business.exhibitionExhibitor.domain.ExhibitionExhibitor;
import com.cuit.business.exhibitionExhibitor.mapper.ExhibitionExhibitorMapper;
import com.cuit.business.exhibitionHall.domain.ExhibitionHall;
import com.cuit.business.exhibitionHall.domain.dto.ExhibitionHallDTO;
import com.cuit.business.exhibitionHall.service.IExhibitionHallService;
import com.cuit.business.exhibitionHall.mapper.ExhibitionHallMapper;
import com.cuit.business.organization.domain.dto.QueryDTO;
import com.cuit.business.reservation.domain.Reservation;
import com.cuit.business.reservation.mapper.ReservationMapper;
import com.cuit.common.common.CommonConstants;
import com.cuit.common.common.R;
import com.cuit.system.sysUser.domain.SysUser;
import com.cuit.system.sysUserRoles.domain.SysUserRoles;
import com.cuit.system.sysUserRoles.domain.dto.SysUserRolesDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ExhibitionHallServiceImpl extends ServiceImpl<ExhibitionHallMapper, ExhibitionHall> implements IExhibitionHallService {

    @Autowired
    private ExhibitionHallMapper exhibitionHallMapper;

    @Autowired
    private ExhibitionMapper exhibitionMapper;

    @Autowired
    private ReservationMapper reservationMapper;

    @Autowired
    private BoothMapper boothMapper;

    @Autowired
    private ExhibitionExhibitorMapper exhibitionExhibitorMapper;

    @Override
    public List<Long> selectUid(String name) {
        return exhibitionHallMapper.selectUid(name);
    }

    @Override
    public List<ExhibitionHallDTO> selectExhibitionList(ExhibitionHallDTO exhibitionDTO) {
        return exhibitionHallMapper.selectExhibitionList(exhibitionDTO);
    }

    @Override
    public List<SysUserRolesDTO> selectEhibitionMg(SysUserRolesDTO sysUserRolesDTO) {
        return exhibitionHallMapper.selectEhibitionMg(sysUserRolesDTO);
    }

    @Override
    public List<ExhibitionHall> selectListByTime(QueryDTO queryDTO) {
        return exhibitionHallMapper.selectListByTime(queryDTO);
    }

    @Override
    public R saveExhibitionHallInfor(ExhibitionHall exhibitionHall, Long uid) {
        exhibitionHall.setUid(uid);
        exhibitionHall.setCreateId(uid);
        exhibitionHall.setUpdateId(uid);
        exhibitionHallMapper.insert(exhibitionHall);
        return R.success("新增成功");
    }

    @Override
    public List<ExhibitionHallDTO> selectList(ExhibitionHallDTO exhibitionHallDTO) {
        return exhibitionHallMapper.selectListByPage(exhibitionHallDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R forbiddenStatus(String status, Long[] id, Long uid) {

        for (Long i : id) {
            Exhibition exhibition = exhibitionMapper.selectById(i);
            exhibition.setStatus(status);
            int updateCount0 = exhibitionMapper.updateById(exhibition);
            int updateCount1 = -1;

            List<Booth> booths = boothMapper.selectListByEidStatus(i);

            if (booths.size() > 0) {
                for (Booth booth : booths) {
                    booth.setStatus(status);
                    updateCount1 = boothMapper.updateById(booth);
                }
            }

            /*预定表信息插入*/
            Reservation reservation = new Reservation();
            reservation.setEid(exhibition.getId());
            reservation.setHid(exhibition.getHid());
            reservation.setStartTime(exhibition.getOpeningTime());
            reservation.setEndTime(exhibition.getClosingTime());
            reservation.setCreateId(uid);
            reservation.setUpdateId(uid);
            int insert = reservationMapper.insert(reservation);

            if (updateCount0 < 0 || updateCount1 < 0 || insert < 0) {
                return R.error(CommonConstants.ConstantsCode.错误.getValue(), "操作失败，请联系管理员");
            }

        }
        if (status.equals(CommonConstants.Status.启用.getValue())){
            return R.success("审批通过");
        }
        return R.success("审批失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R deleteByIds(Long[] id) {
        for (Long i : id) {
            //展馆删除
            ExhibitionHall exhibitionHall = exhibitionHallMapper.selectById(i);
            exhibitionHall.setDelFlag(CommonConstants.DeleteFlag.删除.getValue());
            exhibitionHallMapper.updateById(exhibitionHall);
            //预约表信息删除
            Reservation reservation = new Reservation();
            reservation.setHid(i);
            reservation = reservationMapper.selectBySId(reservation);
            if(reservation!=null){
                reservation.setDelFlag(CommonConstants.DeleteFlag.删除.getValue());
                reservationMapper.updateById(reservation);
            }
            //展会删除
            List<Exhibition> exhibitions = exhibitionMapper.selectListByEid(i);
            if(exhibitions.size()>0){
                for (Exhibition exhibition : exhibitions) {
                    exhibition.setDelFlag(CommonConstants.DeleteFlag.删除.getValue());
                    exhibitionMapper.updateById(exhibition);
                    //展位删除
                    List<Booth> boothList = boothMapper.selectListByEid(exhibition.getId());
                    if(boothList.size()>0){
                        for (Booth booth : boothList) {
                            booth.setDelFlag(CommonConstants.DeleteFlag.删除.getValue());
                            boothMapper.updateById(booth);
                        }
                    }
                }
            }
            //关联数据删除
            ExhibitionExhibitor exhibitionExhibitor = new ExhibitionExhibitor();
            exhibitionExhibitor.setHid(i);
            exhibitionExhibitor = exhibitionExhibitorMapper.selectBySId(exhibitionExhibitor);
            if(exhibitionExhibitor!=null){
                exhibitionExhibitor.setDelFlag(CommonConstants.DeleteFlag.删除.getValue());
               exhibitionExhibitorMapper.updateById(exhibitionExhibitor);
            }
        }
        return R.success("删除成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R updateByStatus(String status, Long[] id) {
        for (Long i : id) {
            //展馆禁用
            ExhibitionHall exhibitionHall = exhibitionHallMapper.selectById(i);
            exhibitionHall.setStatus(status);
            exhibitionHallMapper.updateById(exhibitionHall);
            //展会禁用
            List<Exhibition> exhibitions = exhibitionMapper.selectListByEid(i);
            if(exhibitions.size()>0){
                for (Exhibition exhibition : exhibitions) {
                    exhibition.setStatus(CommonConstants.Status.禁用.getValue());
                    exhibitionMapper.updateById(exhibition);
                    //展位禁用
                    List<Booth> boothList = boothMapper.selectListByEid(exhibition.getId());
                    if(boothList.size()>0){
                        for (Booth booth : boothList) {
                            booth.setStatus(status);
                            boothMapper.updateById(booth);
                        }
                    }
                }
            }
        }
        if(status.equals(CommonConstants.Status.启用.getValue()))
            return R.success("启用成功");
        return R.success("禁用成功");
    }

    @Override
    public List<ExhibitionDTO> selectExhibitionListByHid(Long id) {
        return exhibitionMapper.selectExhibitionListByHid(id);
    }

    @Override
    public ExhibitionHallDTO selectExhibitionHallById(Long id) {
        return exhibitionHallMapper.selectExhibitionHallById(id);
    }

    @Override
    public List<ExhibitionHallDTO> selectExhibitionHallByCreateId(Long uid, ExhibitionHallDTO exhibitionHallDTO) {
        return exhibitionHallMapper.selectExhibitionHallByCreateId(uid,exhibitionHallDTO);
    }

    @Override
    public List<Long> selectHidByUid(Long uid) {
        return exhibitionHallMapper.selectHidByUid(uid);
    }

}
