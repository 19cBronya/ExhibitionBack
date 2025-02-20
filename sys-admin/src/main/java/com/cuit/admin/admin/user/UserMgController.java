package com.cuit.admin.admin.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cuit.admin.admin.user.domain.UserMg;
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
import com.cuit.system.sysUserRoles.service.ISysUserRolesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * @author xym
 * @since 2023-12-05 15:23
 * @description 展会观众管理
 */

@Slf4j
@Api(tags = "观众管理控制类")
@RestController
@RequestMapping("/admin/user")
public class UserMgController extends BaseController {

    @Autowired
    private ISysUserRolesService iSysUserRolesService;
    @Autowired
    private ISysUserService iSysUserService;
    /*图片路径补充*/
    @Value("${Image.path}")
    private String basePath;

    //观众分页查询
    @Log
    @ApiOperation("分页查询观众及其角色信息")
    @RequiresAuthentication
    @PostMapping("/page")
    public R<TableDataInfo> userPage(@RequestBody SysUserRolesDTO sysUserRolesDTO) {

        List<UserMg> userMgs = new ArrayList<>();

        // 获取登录用户信息
        SysUserRoles sysUserRoles = getSysUserRoles();
        if (CommonConstants.UserRole.管理员.getValue().equals(sysUserRoles.getRid().toString())) {

            sysUserRolesDTO.setUid(sysUserRoles.getUid());

            startPage(sysUserRolesDTO);
            List<SysUserRolesDTO> list = iSysUserRolesService.selectSysUserRolesList(sysUserRolesDTO);

            for (SysUserRolesDTO userRolesDTO : list) {
                SysUser sysUser = iSysUserService.selectByid(userRolesDTO.getUid());

                UserMg userMg = new UserMg();

                userMg.setId(sysUser.getId());
                userMg.setLoginName(sysUser.getLoginName());
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

            return R.success("查询成功", getDataTable(userMgs));

        }
        return R.error(CommonConstants.ConstantsCode.错误.getValue(), "权限不足，请联系管理员");
    }

    //观众查看详情
    @Log
    @ApiOperation("观众查看详情")
    @RequiresAuthentication
    @GetMapping("/infor")
    public R<SysUser> getUserInfor(@RequestParam("id") String id) {
        SysUserRoles sysUserRoles = getSysUserRoles();
        if (CommonConstants.UserRole.管理员.getValue().equals(sysUserRoles.getRid().toString())) {
            SysUser sysUser = iSysUserService.getById(Long.valueOf(id));
            if (sysUser == null)
                return R.error(CommonConstants.ConstantsCode.错误.getValue(), "无该用户信息");
            /*头像信息*/
            String imgName = sysUser.getAvatarUrl();
            if (imgName!= null && !imgName.contains(basePath)) {
                imgName = basePath + imgName;
            }
            sysUser.setAvatarUrl(imgName);
            return R.success("查询成功", sysUser);
        }
        return R.error(CommonConstants.ConstantsCode.错误.getValue(), "权限不足，请联系管理员");
    }

    //观众批量删除
    @Log
    @ApiOperation("观众批量删除")
    @RequiresAuthentication
    @DeleteMapping
    public R userDeleteByIds(@RequestParam("id") String[] id) {
        SysUserRoles sysUserRoles = getSysUserRoles();
        if (CommonConstants.UserRole.管理员.getValue().equals(sysUserRoles.getRid().toString())) {
            Long[] longIds = Arrays.stream(id)
                    .mapToLong(Long::parseLong)
                    .boxed()
                    .toArray(Long[]::new);
            return iSysUserService.deleteById(longIds);
        }
        return R.error(CommonConstants.ConstantsCode.错误.getValue(), "权限不足，请联系管理员");
    }

    //观众禁用/启用
    @Log
    @ApiOperation("观众禁用/启用/审核")
    @RequiresAuthentication
    @PostMapping("/changeStatus/{status}")
    public R forbiddenUser(@PathVariable String status, @RequestParam("id") String[] id) {
        // 获取登录用户信息
        SysUserRoles sysUserRoles = getSysUserRoles();
        if (CommonConstants.UserRole.管理员.getValue().equals(sysUserRoles.getRid().toString())) {
            Long[] longIds = Arrays.stream(id)
                    .mapToLong(Long::parseLong)
                    .boxed()
                    .toArray(Long[]::new);
            return iSysUserService.forbiddenStatus(status, longIds);
        } else
            return R.error(CommonConstants.ConstantsCode.错误.getValue(), "权限不足，请联系管理员");
    }

    //观众信息导出
    @Log
    @ApiOperation("观众信息导出")
    @RequiresAuthentication
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody SysUserRolesDTO sysUserRolesDTO) throws Exception {
        try {
            List<UserMg> userMgs = new ArrayList<>();
            // 获取登录用户信息
            SysUserRoles sysUserRoles = getSysUserRoles();
            if (CommonConstants.UserRole.管理员.getValue().equals(sysUserRoles.getRid().toString())) {

                sysUserRolesDTO.setUid(sysUserRoles.getUid());

                startPage(sysUserRolesDTO);
                List<SysUserRolesDTO> list = iSysUserRolesService.selectSysUserRolesList(sysUserRolesDTO);
                for (SysUserRolesDTO userRolesDTO : list) {
                    SysUser sysUser = iSysUserService.selectByid(userRolesDTO.getUid());

                    UserMg userMg = new UserMg();

                    userMg.setId(sysUser.getId());
                    userMg.setLoginName(sysUser.getLoginName());
                    userMg.setName(sysUser.getName());
                    userMg.setRoleName(userRolesDTO.getRoleName());
                    userMg.setSex(sysUser.getSex());
                    userMg.setPhone(sysUser.getPhone());
                    userMg.setEmail(sysUser.getEmail());
                    if (sysUser.getStatus().equals(CommonConstants.Status.启用.getValue()))
                        userMg.setStatus("启用");
                    else if (sysUser.getStatus().equals(CommonConstants.Status.禁用.getValue()))
                        userMg.setStatus("禁用");
                    else
                        userMg.setStatus("审核中");

                    userMgs.add(userMg);
                }
                ExcelUtil.outputExcel(response, userMgs, UserMg.class, "观众信息表");
            } else
                throw new ServiceException(CommonConstants.ConstantsCode.错误.getValue(), "权限不足，请联系管理员");
        } catch(Exception e) {
        throw new ServiceException(CommonConstants.ConstantsCode.错误.getValue(), "文件导出失败");
        }
    }
}
