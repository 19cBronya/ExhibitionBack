package com.cuit.business.organization.controller;


import com.cuit.business.exhibition.domain.dto.ExhibitionDTO;
import com.cuit.business.exhibitionHall.domain.ExhibitionHall;
import com.cuit.business.exhibitionHall.service.IExhibitionHallService;
import com.cuit.business.organization.domain.Organization;
import com.cuit.business.organization.domain.dto.OrganizationDTO;
import com.cuit.business.organization.domain.dto.QueryDTO;
import com.cuit.business.organization.service.IOrganizationService;
import com.cuit.common.common.CommonConstants;
import com.cuit.common.common.R;
import com.cuit.common.common.page.TableDataInfo;
import com.cuit.system.base.BaseController;
import com.cuit.system.log.anno.Log;
import com.cuit.system.sysUserRoles.domain.SysUserRoles;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author xym
 * @since 2023-11-15 14:31
 * @description 参展机构控制层
 */
@Api(tags = "办展机构信息控制类")
@RestController
@RequestMapping("/organization")
public class OrganizationController extends BaseController {

    @Autowired
    private IExhibitionHallService exhibitionHallService;

    @Autowired
    private IOrganizationService organizationService;

    /*展览馆下拉选择*/
    @Log
    @ApiOperation("展览馆下拉选择")
    @PostMapping("/getHallList")
    public R getExhibitionList(@RequestBody QueryDTO queryDTO){
        List<ExhibitionHall> list = exhibitionHallService.selectListByTime(queryDTO);
        if (list.size() == 0){
            return R.success("未查询到符合条件的数据信息");
        }
        return R.success("查询成功",list);
    }

    /**
     * 新增办展机构信息
     */
    @Log
    @ApiOperation("新增办展机构信息")
    @RequiresAuthentication
    @PostMapping("/add")
    public R add(@RequestBody Organization organization) {
        SysUserRoles sysUserRoles = getSysUserRoles();
        if(CommonConstants.UserRole.管理员.getValue().equals(sysUserRoles.getRid().toString())||
                CommonConstants.UserRole.办展机构.getValue().equals(sysUserRoles.getRid().toString())){
                organization.setUid(sysUserRoles.getUid());
                organization.setCreateId(sysUserRoles.getUid());
                organization.setUpdateId(sysUserRoles.getUid());
                organization.setCreateTime(LocalDateTime.now());
                organizationService.save(organization);
                return R.success("新增成功");
        }else
            return R.error("您没有权限进行此操作");
    }


    /**
     * 分页查询办展机构信息
     */
    @Log
    @ApiOperation("分页查询办展机构信息")
    @RequiresAuthentication
    @PostMapping("/page")
    public R<TableDataInfo> page(@RequestBody OrganizationDTO organizationDTO) {
        SysUserRoles sysUserRoles = getSysUserRoles();
        if(!(CommonConstants.UserRole.管理员.getValue().equals(sysUserRoles.getRid().toString())||
                CommonConstants.UserRole.办展机构.getValue().equals(sysUserRoles.getRid().toString()))){
            organizationDTO.setStatus(CommonConstants.Status.启用.getValue());
        }
        startPage(organizationDTO);
        List<OrganizationDTO> list = organizationService.selectListByPage(organizationDTO);
        if (list.size() == 0) {
            return R.success("未查询到符合条件的数据信息");
        }
        return R.success("查询成功", getDataTable(list));
    }

    /**
     * 修改办展机构信息
     */
    @Log
    @ApiOperation("修改办展机构信息")
    @RequiresAuthentication
    @PostMapping("/update")
    public R update(@RequestBody Organization organization) {
        SysUserRoles sysUserRoles = getSysUserRoles();
        if(CommonConstants.UserRole.管理员.getValue().equals(sysUserRoles.getRid().toString())||
                CommonConstants.UserRole.办展机构.getValue().equals(sysUserRoles.getRid().toString())){
            organization.setUpdateId(sysUserRoles.getUid());
            organization.setUpdateTime(LocalDateTime.now());
            organizationService.updateById(organization);
            return R.success("修改成功");
        }else
            return R.error("您没有权限进行此操作");
    }

}
