package com.cuit.admin.admin.exhibitionHall;

import com.cuit.admin.admin.user.domain.UserMg;
import com.cuit.business.exhibitionHall.service.IExhibitionHallService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Api(tags = "展览馆负责人管理控制类")
@RestController
@RequestMapping("/admin/exhibitionHallManager")
public class ExhibitionHallManagerMgController extends BaseController {

    @Autowired
    private IExhibitionHallService iExhibitionHallService;

    @Autowired
    private ISysUserService iSysUserService;

    //展览馆管理员分页查询
    @Log
    @ApiOperation("分页查询展览馆管理员")
    @RequiresAuthentication
    @PostMapping("/page")
    public R<TableDataInfo> userPage(@RequestBody SysUserRolesDTO sysUserRolesDTO){

        List<UserMg> userMgs = new ArrayList<>();
        // 获取登录用户信息
        SysUserRoles sysUserRoles = getSysUserRoles();
        if (CommonConstants.UserRole.管理员.getValue().equals(sysUserRoles.getRid().toString())){

            sysUserRolesDTO.setUid(sysUserRoles.getUid());
            sysUserRolesDTO.setRid(Long.valueOf(CommonConstants.UserRole.展览馆.getValue()));
            startPage(sysUserRolesDTO);

            List<SysUserRolesDTO> list = iExhibitionHallService.selectEhibitionMg(sysUserRolesDTO);

            for (SysUserRolesDTO userRolesDTO : list) {
                SysUser sysUser = iSysUserService.selectByid(userRolesDTO.getUid());

                UserMg userMg = new UserMg();
                userMg.setLoginName(sysUser.getLoginName());
                userMg.setId(sysUser.getId());
                userMg.setName(sysUser.getName());
                userMg.setLoginName(sysUser.getLoginName());
                userMg.setRoleName(userRolesDTO.getRoleName());
                userMg.setSex(sysUser.getSex());
                userMg.setPhone(sysUser.getPhone());
                userMg.setEmail(sysUser.getEmail());
                userMg.setStatus(sysUser.getStatus());
                userMg.setDelFlag(sysUser.getDelFlag());
                userMg.setCreateTime(sysUser.getCreateTime());
                userMg.setUpdateTime(sysUser.getUpdateTime());

                userMgs.add(userMg);
            }

            return R.success("查询成功",getDataTable(userMgs));
        }
        return R.error(CommonConstants.ConstantsCode.错误.getValue(),"权限不足，请联系管理员");
    }

    /**
     * 展馆负责人信息导出
     */
    @Log
    @ApiOperation("展馆负责人信息导出")
    @RequiresAuthentication
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody SysUserRolesDTO sysUserRolesDTO) throws Exception {
        List<UserMg> userMgs = new ArrayList<>();
        // 获取登录用户信息
        SysUserRoles sysUserRoles = getSysUserRoles();
        if (CommonConstants.UserRole.管理员.getValue().equals(sysUserRoles.getRid().toString())) {

            sysUserRolesDTO.setUid(sysUserRoles.getUid());
            sysUserRolesDTO.setRid(Long.valueOf(CommonConstants.UserRole.展览馆.getValue()));
            startPage(sysUserRolesDTO);

            List<SysUserRolesDTO> list = iExhibitionHallService.selectEhibitionMg(sysUserRolesDTO);

            for (SysUserRolesDTO userRolesDTO : list) {
                SysUser sysUser = iSysUserService.selectByid(userRolesDTO.getUid());

                UserMg userMg = new UserMg();

                userMg.setId(sysUser.getId());
                userMg.setName(sysUser.getName());
                userMg.setRoleName(userRolesDTO.getRoleName());
                userMg.setSex(sysUser.getSex());
                userMg.setPhone(sysUser.getPhone());
                userMg.setEmail(sysUser.getEmail());
                userMg.setStatus(sysUser.getStatus());
                userMg.setDelFlag(sysUser.getDelFlag());
                userMg.setCreateTime(sysUser.getCreateTime());
                userMg.setUpdateTime(sysUser.getUpdateTime());
                userMgs.add(userMg);
            }
            ExcelUtil.outputExcel(response, userMgs, UserMg.class, "展馆负责人信息表");
        }else
            throw new ServiceException(CommonConstants.ConstantsCode.错误.getValue(),"权限不足，请联系管理员");

    }
}
