package com.cuit.admin.admin.exhibitionHall;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cuit.admin.admin.exhibitionHall.domain.ExhibitionHallMg;
import com.cuit.admin.admin.orders.domain.OrdersMg;
import com.cuit.business.exhibitionHall.domain.ExhibitionHall;
import com.cuit.business.exhibitionHall.domain.dto.ExhibitionHallDTO;
import com.cuit.business.exhibitionHall.service.IExhibitionHallService;
import com.cuit.common.common.CommonConstants;
import com.cuit.common.common.R;
import com.cuit.common.common.ServiceException;
import com.cuit.common.common.page.TableDataInfo;
import com.cuit.common.util.excel.ExcelUtil;
import com.cuit.pay.orders.domain.dto.OrdersDTO;
import com.cuit.system.base.BaseController;
import com.cuit.system.log.anno.Log;
import com.cuit.system.sysUser.domain.SysUser;
import com.cuit.system.sysUser.service.ISysUserService;
import com.cuit.system.sysUserRoles.domain.SysUserRoles;
import com.cuit.system.sysUserRoles.service.ISysUserRolesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xym
 * @since 2023-12-05 15:33
 * @description 展览馆管理
 */

@Slf4j
@Api(tags = "展览馆管理控制类")
@RestController
@RequestMapping("/admin/exhibitionHall")
public class ExhibitionHallMgController extends BaseController {

    @Autowired
    private ISysUserRolesService iSysUserRolesService;

    @Autowired
    private IExhibitionHallService iExhibitionHallService;

    @Autowired
    private ISysUserService iSysUserService;

    /*图片路径补充*/
    @Value("${Image.path}")
    private String basePath;

    //展览馆分页查询
    @Log
    @ApiOperation("分页查询展览馆")
    @RequiresAuthentication
    @PostMapping("/page")
    public R<TableDataInfo> exhibitionPage(@RequestBody ExhibitionHallDTO exhibitionDTO) {

        // 获取登录用户信息
        SysUserRoles sysUserRoles = getSysUserRoles();
        if (CommonConstants.UserRole.管理员.getValue().equals(sysUserRoles.getRid().toString())) {

            startPage(exhibitionDTO);
            List<ExhibitionHallDTO> list = iExhibitionHallService.selectExhibitionList(exhibitionDTO);
            for (ExhibitionHallDTO dto : list) {
                /*展馆图片信息*/
                String picUrl = dto.getPicUrl();
                if (picUrl!=null && !picUrl.contains(basePath)) {
                    picUrl = basePath + picUrl;
                }
                dto.setPicUrl(picUrl);
            }
            return R.success("查询成功",getDataTable(list));
        }
        return R.error(CommonConstants.ConstantsCode.错误.getValue(), "权限不足，请联系管理员");
    }


    //展览馆查看详情
    @Log
    @ApiOperation("展览馆查看详情")
    @RequiresAuthentication
    @GetMapping("/infor")
    public R<ExhibitionHall> getUserInfor(@RequestParam("id") String id){
        SysUserRoles sysUserRoles = getSysUserRoles();
        if (CommonConstants.UserRole.管理员.getValue().equals(sysUserRoles.getRid().toString())){
            ExhibitionHall exhibitionHall = iExhibitionHallService.getById(Long.valueOf(id));
            if(exhibitionHall ==null)
                return R.error(CommonConstants.ConstantsCode.错误.getValue(),"无该信息");
            /*展馆图片信息*/
            String picUrl = exhibitionHall.getPicUrl();
            if (picUrl!=null && !picUrl.contains(basePath)) {
                picUrl = basePath + picUrl;
            }
            exhibitionHall.setPicUrl(picUrl);
            return R.success("查询成功", exhibitionHall);
        }
        return R.error(CommonConstants.ConstantsCode.错误.getValue(),"权限不足，请联系管理员");
    }

    /**
     * 展馆信息列表导出
     */
    @Log
    @ApiOperation("展馆信息列表导出")
    @RequiresAuthentication
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody ExhibitionHallDTO exhibitionHallDTO) throws Exception{
        List<ExhibitionHallMg> exhibitionMgList = new ArrayList<>();
        // 获取登录用户信息
        SysUserRoles sysUserRoles = getSysUserRoles();
        if (CommonConstants.UserRole.管理员.getValue().equals(sysUserRoles.getRid().toString())) {

            startPage(exhibitionHallDTO);
            List<ExhibitionHallDTO> list = iExhibitionHallService.selectExhibitionList(exhibitionHallDTO);
            if(list.size()>0){
                ExhibitionHallMg exhibitionHallMg = new ExhibitionHallMg();
                for (ExhibitionHallDTO dto : list) {
                    exhibitionHallMg.setName(dto.getName());
                    exhibitionHallMg.setManageName(dto.getManageName());
                    exhibitionHallMg.setAddress(dto.getAddress());
                    exhibitionHallMg.setCapacityNumber(dto.getCapacityNumber().toString());
                    exhibitionHallMg.setOpeningTime(dto.getOpeningTime());
                   exhibitionHallMg.setClosingTime(dto.getClosingTime());
                   exhibitionHallMg.setStatus(CommonConstants.Status.getTextFromValue(dto.getStatus()));
                   exhibitionHallMg.setCreateTime(dto.getCreateTime().toString());
                   exhibitionMgList.add(exhibitionHallMg);
               }
                ExcelUtil.outputExcel(response, exhibitionMgList, ExhibitionHallMg.class, "展馆信息表");
            }
        }
        else
            throw new ServiceException(CommonConstants.ConstantsCode.错误.getValue(),"权限不足，请联系管理员");
    }




}
