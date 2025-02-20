package com.cuit.admin.admin.user;

import com.cuit.admin.admin.user.domain.UserMg;
import com.cuit.common.common.CommonConstants;
import com.cuit.common.common.R;
import com.cuit.common.common.page.TableDataInfo;
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
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Api(tags = "账号审核管理控制类")
@RestController
@RequestMapping("/admin/userReview")
public class UserReviewController extends BaseController {

    @Autowired
    private ISysUserRolesService iSysUserRolesService;

    @Autowired
    private ISysUserService iSysUserService;

    /*查询账号审核信息*/
    @Log
    @ApiOperation("查询账号审核信息")
    @RequiresAuthentication
    @PostMapping("/page")
    public R<TableDataInfo> userPage(@RequestBody SysUserRolesDTO sysUserRolesDTO){

        List<UserMg> userMgs = new ArrayList<>();

        // 获取登录用户信息
        SysUserRoles sysUserRoles = getSysUserRoles();
        if (CommonConstants.UserRole.管理员.getValue().equals(sysUserRoles.getRid().toString())){

            sysUserRolesDTO.setUid(sysUserRoles.getUid());

            startPage(sysUserRolesDTO);
            List<SysUserRolesDTO> list = iSysUserRolesService.selectSysUserRolesAllList(sysUserRolesDTO);

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

            return R.success("查询成功",getDataTable(userMgs));

        }
        return R.error(CommonConstants.ConstantsCode.错误.getValue(),"权限不足，请联系管理员");
    }

    /*审核账号*/
    @Log
    @ApiOperation("审核账号")
    @RequiresAuthentication
    @PostMapping("/examine/{status}")
    public R examineUser(@PathVariable String status, @RequestParam("id") String[] id){
        // 获取登录用户信息
        SysUserRoles sysUserRoles = getSysUserRoles();
        if(CommonConstants.UserRole.管理员.getValue().equals(sysUserRoles.getRid().toString())){
            Long[] longIds = Arrays.stream(id)
                    .mapToLong(Long::parseLong)
                    .boxed()
                    .toArray(Long[]::new);
            return iSysUserService.forbiddenStatus(status,longIds);
        }
        else
            return R.error(CommonConstants.ConstantsCode.错误.getValue(),"权限不足，请联系管理员");
    }




}
