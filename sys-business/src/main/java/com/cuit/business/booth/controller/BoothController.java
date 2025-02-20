package com.cuit.business.booth.controller;

import cn.hutool.core.util.StrUtil;
import com.cuit.business.booth.domain.Booth;
import com.cuit.business.booth.domain.BoothReservation;
import com.cuit.business.booth.service.IBoothService;
import com.cuit.business.exhibitor.service.IExhibitorService;
import com.cuit.common.common.CommonConstants;
import com.cuit.common.common.R;
import com.cuit.common.common.page.TableDataInfo;
import com.cuit.system.base.BaseController;
import com.cuit.system.log.anno.Log;
import com.cuit.system.sysUserRoles.domain.SysUserRoles;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xym
 * @description 展位信息控制层
 * @since 2024-01-16 16:16
 */

@Slf4j
@Api(tags = "展位信息控制类")
@RestController
@RequestMapping("/booth")
public class BoothController extends BaseController {

    @Autowired
    private IBoothService boothService;

    @Autowired
    private IExhibitorService exhibitorService;

    /**
     * 展会对应展位信息查询
     *
     * @param id 展会id
     * @return
     */
    @Log
    @ApiOperation("展会对应展位信息查询")
    @RequiresAuthentication
    @GetMapping("/select")
    public R<TableDataInfo> getBoothDetail(@RequestParam("id") String id,Integer pageNum,Integer pageSize) {
        SysUserRoles sysUserRoles = getSysUserRoles();
        if(CommonConstants.UserRole.管理员.getValue().equals(sysUserRoles.getRid().toString())
                ||CommonConstants.UserRole.参展商.getValue().equals(sysUserRoles.getRid().toString())
                ||CommonConstants.UserRole.办展机构.getValue().equals(sysUserRoles.getRid().toString())){
            startPage(pageNum,pageSize);
            List<Booth> list = new ArrayList<>();
            if (CommonConstants.UserRole.参展商.getValue().equals(sysUserRoles.getRid().toString()))
               list = boothService.selectListByEid(Long.valueOf(id));
            else
                list = boothService.orgSelectList(Long.valueOf(id));
            if(list.size()>0)
            {
                for (Booth booth : list) {
                    booth.setSoldStatus(CommonConstants.SoldStatus.getTextFromValue(booth.getSoldStatus()));
                }
            }
            return R.success("查询成功", getDataTable(list));
        }else
            return R.error("权限不足,请联系管理员");
    }

}
