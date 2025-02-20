package com.cuit.admin.common;

import com.cuit.business.exhibition.service.IExhibitionService;
import com.cuit.business.exhibitionHall.domain.dto.AgeData;
import com.cuit.business.exhibitionHall.domain.dto.MonthData;
import com.cuit.business.exhibitionHall.domain.dto.PersonData;
import com.cuit.business.exhibitionHall.domain.dto.TypeData;
import com.cuit.business.exhibitionHall.service.IExhibitionHallService;
import com.cuit.common.common.CommonConstants;
import com.cuit.common.common.R;
import com.cuit.pay.orders.domain.dto.OrdersDTO;
import com.cuit.pay.orders.service.IOrdersService;
import com.cuit.system.base.BaseController;
import com.cuit.system.sysUserRoles.domain.SysUserRoles;
import com.cuit.system.sysUserRoles.service.ISysUserRolesService;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin/common")
public class CommonMgController extends BaseController {

    @Autowired
    private IExhibitionHallService exhibitionHallService;

    @Autowired
    private IOrdersService ordersService;

    @Autowired
    private IExhibitionService exhibitionService;

    @Autowired
    private ISysUserRolesService sysUserRolesService;

    /**
     * 申请专业观众
     */
    @ApiOperation("申请专业观众")
    @RequiresAuthentication
    @PostMapping("/apply")
    public R apply() {
        SysUserRoles sysUserRoles = getSysUserRoles();
        if (sysUserRoles.getRid().toString().equals(CommonConstants.UserRole.普通观众.getValue())){
            List<OrdersDTO> ordersDTOS = ordersService.selectListById(sysUserRoles.getUid());
            if(ordersDTOS == null){
                return R.error("申请失败，需要参与展会超过50次才可申请，您当前的展会次数为0");
            }else{
                if(ordersDTOS.size() < 50)
                    return R.error("申请失败，需要参与展会超过50次才可申请，您当前的展会次数为"+ordersDTOS.size());
            }
            sysUserRoles.setRid(Long.valueOf(CommonConstants.UserRole.专业观众.getValue()));
            sysUserRoles.setUpdateId(sysUserRoles.getUid());
            sysUserRolesService.updateById(sysUserRoles);
            return R.success("申请成功");
        }else
            return R.error("您不是普通观众，无法申请专业观众");
    }

    /**
     * 展会类型数据分析
     */
    @ApiOperation("展会类型数据分析")
    @RequiresAuthentication
    @PostMapping("/typeData")
    public R typeData() {
        SysUserRoles sysUserRoles = getSysUserRoles();
        List<TypeData> list = null;
        if (sysUserRoles.getRid().toString().equals(CommonConstants.UserRole.展览馆.getValue())){
            List<Long> hids = exhibitionHallService.selectHidByUid(sysUserRoles.getUid());
            list = exhibitionService.getTypeData(hids);
        }
        return R.success(list);
    }

    /**
     * 本周参观人数数据分析
     */
    @ApiOperation("本周参观人数数据分析")
    @RequiresAuthentication
    @PostMapping("/personData")
    public R personData() {
        SysUserRoles sysUserRoles = getSysUserRoles();

        List<PersonData> list = new ArrayList<>();

        for (int i = 1; i <= 7; i++) {
            PersonData personData = new PersonData();
            String week = "";
            switch (i) {
                case 1:
                    week = "周一";
                    break;
                case 2:
                    week = "周二";
                    break;
                case 3:
                    week = "周三";
                    break;
                case 4:
                    week = "周四";
                    break;
                case 5:
                    week = "周五";
                    break;
                case 6:
                    week = "周六";
                    break;
                case 7:
                    week = "周日";
                    break;
            }
            personData.setWeek(week);
            personData.setNum(new BigDecimal("0"));
            list.add(personData);
        }

        if (sysUserRoles.getRid().toString().equals(CommonConstants.UserRole.展览馆.getValue())) {
            List<Long> hids = exhibitionHallService.selectHidByUid(sysUserRoles.getUid());
            List<Long> eids =null;
            if(hids.size()>0){
                eids = exhibitionService.selectEidByHids(hids);
            }
            if(eids!=null && eids.size()>0){
                List<PersonData> personDataList = ordersService.selectPersonData(eids);
                if(personDataList.size()>0) {
                    for (PersonData personData : personDataList) {
                        String week = personData.getWeek();
                        BigDecimal num = personData.getNum();
                        int index = -1;
                        switch (week) {
                            case "周一":
                                index = 0;
                                break;
                            case "周二":
                                index = 1;
                                break;
                            case "周三":
                                index = 2;
                                break;
                            case "周四":
                                index = 3;
                                break;
                            case "周五":
                                index = 4;
                                break;
                            case "周六":
                                index = 5;
                                break;
                            case "周日":
                                index = 6;
                                break;
                        }
                        if (index != -1) {
                            list.get(index).setNum(num);
                        }
                    }
                }
            }
        }
        return R.success(list);
    }

    /**
     * 展馆月销量数据
     */
    @ApiOperation("展馆月销量数据")
    @RequiresAuthentication
    @PostMapping("/monthData")
    public R monthData() {
        SysUserRoles sysUserRoles = getSysUserRoles();

        List<MonthData> list = new ArrayList<>();

        for (int i = 1; i <= 12; i++) {
            MonthData monthData = new MonthData();
            monthData.setMonth(String.valueOf(i));
            monthData.setNum(new BigDecimal("0"));
            list.add(monthData);
        }

        if (sysUserRoles.getRid().toString().equals(CommonConstants.UserRole.展览馆.getValue())) {
            List<Long> hids = exhibitionHallService.selectHidByUid(sysUserRoles.getUid());
            List<Long> eids = null;
            if(hids.size()>0){
                eids = exhibitionService.selectEidByHids(hids);
            }
            exhibitionService.selectEidByHids(hids);
            if (eids!=null && eids.size() > 0) {
                List<MonthData> monthList = ordersService.selectMonthData(eids);
                if (monthList.size() > 0) {
                    for (MonthData monthData : monthList) {
                        int month = Integer.parseInt(monthData.getMonth());
                        if (month >= 1 && month <= 12) {
                            list.get(month - 1).setNum(monthData.getNum());
                        }
                    }
                }
            }
        }
        return R.success(list);
    }

    /**
     * 年龄段分布
     */
    @ApiOperation("年龄段分布")
    @RequiresAuthentication
    @PostMapping("/ageData")
    public R wekData() {
        SysUserRoles sysUserRoles = getSysUserRoles();
        List<AgeData> list = new ArrayList<>();
        for (int i = 1; i <= 7; i++) {
            AgeData ageData = new AgeData();
            String age = "";
            switch (i) {
                case 1:
                    age = "10+";
                    break;
                case 2:
                    age = "20+";
                    break;
                case 3:
                    age = "30+";
                    break;
                case 4:
                    age = "40+";
                    break;
                case 5:
                    age = "50+";
                    break;
                case 6:
                    age = "60+";
                    break;
                case 7:
                    age = "70+";
                    break;
                case 8:
                    age = "80+";
                    break;
            }
            ageData.setAge(age);
            ageData.setNum(new BigDecimal("0"));
            ageData.setRate("0.00%");
            list.add(ageData);
        }
        if (sysUserRoles.getRid().toString().equals(CommonConstants.UserRole.展览馆.getValue())){
            List<Long> hids = exhibitionHallService.selectHidByUid(sysUserRoles.getUid());
            List<Long> eids = null;
            if(hids.size()>0){
                eids = exhibitionService.selectEidByHids(hids);
            }
            if(eids!=null && eids.size() > 0){
                List<AgeData> ageList = ordersService.selectAgeData(eids);
                if(ageList.size()>0){
                    BigDecimal totalNum = new BigDecimal("0");
                    for (AgeData ageData : ageList) {
                        totalNum = totalNum.add(ageData.getNum());
                    }
                    for (AgeData ageData : ageList) {
                        String age = ageData.getAge();
                        BigDecimal num = ageData.getNum();
                        int index = -1;
                        switch (age) {
                            case "10+":
                                index = 0;
                                break;
                            case "20+":
                                index = 1;
                                break;
                            case "30+":
                                index = 2;
                                break;
                            case "40+":
                                index = 3;
                                break;
                            case "50+":
                                index = 4;
                                break;
                            case "60+":
                                index = 5;
                                break;
                            case "70+":
                                index = 6;
                                break;
                            case "80+":
                                index = 7;
                                break;
                        }
                        if (index != -1) {
                            list.get(index).setNum(num);
                            list.get(index).setRate(num.divide(totalNum, 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100"))+"%");
                        }
                    }
                }
            }
        }
        return R.success(list);
    }
}
