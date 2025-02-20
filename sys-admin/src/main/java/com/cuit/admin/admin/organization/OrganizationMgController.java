package com.cuit.admin.admin.organization;

import com.cuit.admin.admin.organization.domain.OrganizationMg;
import com.cuit.business.exhibitionHall.service.IExhibitionHallService;
import com.cuit.business.organization.domain.Organization;
import com.cuit.business.organization.service.IOrganizationService;
import com.cuit.common.common.CommonConstants;
import com.cuit.common.common.R;
import com.cuit.common.common.ServiceException;
import com.cuit.common.common.page.TableDataInfo;
import com.cuit.common.util.excel.ExcelUtil;
import com.cuit.system.base.BaseController;
import com.cuit.system.log.anno.Log;
import com.cuit.system.sysUser.domain.SysUser;
import com.cuit.system.sysUser.service.ISysUserService;
import com.cuit.system.sysUserRoles.domain.SysUserRoles;
import com.cuit.system.sysUserRoles.domain.dto.SysUserRolesDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Api(tags ="办展机构管理")
@RestController
@RequestMapping("/admin/organization")
public class OrganizationMgController extends BaseController {

    @Autowired
    private IExhibitionHallService iExhibitionHallService;

    @Autowired
    private ISysUserService iSysUserService;

    @Autowired
    private IOrganizationService organizationService;

    //办展机构分页查询
    @Log
    @ApiOperation("分页查询办展机构")
    @RequiresAuthentication
    @PostMapping("/page")
    public R<TableDataInfo> userPage(@RequestBody SysUserRolesDTO sysUserRolesDTO){

        List<OrganizationMg> organizationMgs = new ArrayList<>();
        // 获取登录用户信息
        SysUserRoles sysUserRoles = getSysUserRoles();
        if (CommonConstants.UserRole.管理员.getValue().equals(sysUserRoles.getRid().toString())){

            sysUserRolesDTO.setUid(sysUserRoles.getUid());
            sysUserRolesDTO.setRid(Long.valueOf(CommonConstants.UserRole.办展机构.getValue()));
            startPage(sysUserRolesDTO);

            List<SysUserRolesDTO> list = iExhibitionHallService.selectEhibitionMg(sysUserRolesDTO);

            for (SysUserRolesDTO userRolesDTO : list) {
                SysUser sysUser = iSysUserService.selectByid(userRolesDTO.getUid());

                OrganizationMg organizationMg = new OrganizationMg();

                Organization organization = organizationService.selectByUid(userRolesDTO.getUid());
                organizationMg.setOrganizationName(organization.getName());
                organizationMg.setManagerName(sysUser.getName());
                organizationMg.setOrganizationPhone(sysUser.getPhone());
                organizationMg.setOrganizationEmail(sysUser.getEmail());
                organizationMg.setCreateTime(organization.getCreateTime().toString());
                organizationMgs.add(organizationMg);
            }

            return R.success("查询成功",getDataTable(organizationMgs));
        }
        return R.error(CommonConstants.ConstantsCode.错误.getValue(),"权限不足，请联系管理员");
    }

    /**
     * 办展机构导出
     */
    @Log
    @ApiOperation("办展机构信息导出")
    @RequiresAuthentication
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody SysUserRolesDTO sysUserRolesDTO) throws Exception {
        List<OrganizationMg> organizationMgs = new ArrayList<>();
        // 获取登录用户信息
        SysUserRoles sysUserRoles = getSysUserRoles();
        if (CommonConstants.UserRole.管理员.getValue().equals(sysUserRoles.getRid().toString())) {

            sysUserRolesDTO.setUid(sysUserRoles.getUid());
            sysUserRolesDTO.setRid(Long.valueOf(CommonConstants.UserRole.办展机构.getValue()));
            startPage(sysUserRolesDTO);

            List<SysUserRolesDTO> list = iExhibitionHallService.selectEhibitionMg(sysUserRolesDTO);

            for (SysUserRolesDTO userRolesDTO : list) {
                SysUser sysUser = iSysUserService.selectByid(userRolesDTO.getUid());

                OrganizationMg organizationMg = new OrganizationMg();

                Organization organization = organizationService.selectByUid(userRolesDTO.getUid());
                organizationMg.setManagerName(organization.getName());
                organizationMg.setManagerName(sysUser.getName());
                organizationMg.setOrganizationPhone(sysUser.getPhone());
                organizationMg.setOrganizationName(sysUser.getEmail());
                organizationMg.setCreateTime(organization.getCreateTime().toString());
                organizationMgs.add(organizationMg);
            }
            ExcelUtil.outputExcel(response, organizationMgs, OrganizationMg.class, "办展机构信息表");
        }else
            throw new ServiceException(CommonConstants.ConstantsCode.错误.getValue(),"权限不足，请联系管理员");
    }
}
