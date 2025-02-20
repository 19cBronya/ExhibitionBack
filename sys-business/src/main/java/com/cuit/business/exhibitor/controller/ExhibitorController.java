package com.cuit.business.exhibitor.controller;

import cn.hutool.core.util.StrUtil;
import com.cuit.business.exhibition.domain.Exhibition;
import com.cuit.business.exhibitor.domain.Exhibitor;
import com.cuit.business.exhibitor.domain.dto.ExhibitorDTO;
import com.cuit.business.exhibitor.service.IExhibitorService;
import com.cuit.common.common.CommonConstants;
import com.cuit.common.common.R;
import com.cuit.common.common.page.TableDataInfo;
import com.cuit.common.util.OssManagerUtil;
import com.cuit.system.base.BaseController;
import com.cuit.system.log.anno.Log;
import com.cuit.system.sysUserRoles.domain.SysUserRoles;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author xym
 * @since 2023-12-04 16:26
 * @description 参展商控制层
 */

@Slf4j
@Api(tags = "参展商信息控制类")
@RestController
@RequestMapping("/exhibitor")
public class ExhibitorController extends BaseController {

    /*图片路径补充*/
    @Value("${Image.path}")
    private String basePath;

    @Autowired
    private IExhibitorService exhibitorService;

    /**
     * 参展商新增
     */
    @Log
    @RequiresAuthentication
    @ApiOperation(value = "新增参展商信息")
    @PostMapping("/add")
    public R add(@RequestBody Exhibitor exhibitor) {
        SysUserRoles sysUserRoles = getSysUserRoles();
        if (StrUtil.equalsAny(sysUserRoles.getRid().toString(),
                CommonConstants.UserRole.参展商.getValue(),
                CommonConstants.UserRole.管理员.getValue())) {
            return exhibitorService.saveExhibitorInfor(exhibitor,sysUserRoles.getUid());
        }
        return R.error(CommonConstants.ConstantsCode.错误.getValue(),"您没有权限操作");
    }


    /**
     * 参展商信息分页查询
     */
    @Log
    @ApiOperation("参展商信息分页查询")
    @RequiresAuthentication
    @PostMapping("/page")
    public R<TableDataInfo> page(@RequestBody ExhibitorDTO exhibitorDTO) {
        SysUserRoles sysUserRoles = getSysUserRoles();
        if(!(CommonConstants.UserRole.管理员.getValue().equals(sysUserRoles.getRid().toString())||
                CommonConstants.UserRole.办展机构.getValue().equals(sysUserRoles.getRid().toString()))){
            exhibitorDTO.setStatus(CommonConstants.Status.启用.getValue());
        }
        startPage(exhibitorDTO);
        List<ExhibitorDTO> list = exhibitorService.selectListByPage(exhibitorDTO);
        for (ExhibitorDTO dto : list) {
            /*展馆图片信息*/
            String picUrl = dto.getCompanyLogo();
            if (picUrl!=null && !picUrl.contains(basePath)) {
                picUrl = basePath + picUrl;
            }
            dto.setCompanyLogo(picUrl);
        }
        return R.success("查询成功",getDataTable(list));
    }

    /**
     * 旗下参展商信息分页查询
     */
    @Log
    @ApiOperation("旗下参展商信息分页查询")
    @RequiresAuthentication
    @PostMapping("/page/selectById")
    public R<TableDataInfo> pageSelectById(@RequestBody ExhibitorDTO exhibitorDTO) {
        SysUserRoles sysUserRoles = getSysUserRoles();
        if(!(CommonConstants.UserRole.管理员.getValue().equals(sysUserRoles.getRid().toString())||
                CommonConstants.UserRole.办展机构.getValue().equals(sysUserRoles.getRid().toString()))){
            exhibitorDTO.setStatus(CommonConstants.Status.启用.getValue());
        }
        startPage(exhibitorDTO);
        List<ExhibitorDTO> list = exhibitorService.selectListByPageById(sysUserRoles.getUid());
        for (ExhibitorDTO dto : list) {
            /*展馆图片信息*/
            String picUrl = dto.getCompanyLogo();
            if (picUrl !=null &&!picUrl.contains(basePath)) {
                picUrl = basePath + picUrl;
            }
            dto.setCompanyLogo(picUrl);
        }
        return R.success("查询成功",getDataTable(list));
    }

    /**
     * 参展商信息修改
     */
    @Log
    @RequiresAuthentication
    @ApiOperation(value = "参展商信息修改")
    @PostMapping("/update")
    public R update(@RequestBody Exhibitor exhibitor) {
        SysUserRoles sysUserRoles = getSysUserRoles();
        if (StrUtil.equalsAny(sysUserRoles.getRid().toString(),
                CommonConstants.UserRole.参展商.getValue(),
                CommonConstants.UserRole.管理员.getValue())) {
            exhibitor.setUpdateId(sysUserRoles.getUid());
            exhibitorService.updateById(exhibitor);
            return R.success("修改成功");
        }else
            return R.error("权限不足，请联系管理员");
    }

    /**
     * 参展商信息删除
     */
    @Log
    @RequiresAuthentication
    @ApiOperation(value = "参展商信息删除")
    @DeleteMapping
    public R delete(@RequestParam("id") String[] id) {
        SysUserRoles sysUserRoles = getSysUserRoles();
        if (StrUtil.equalsAny(sysUserRoles.getRid().toString(),
                CommonConstants.UserRole.参展商.getValue(),
                CommonConstants.UserRole.管理员.getValue())) {
            return exhibitorService.deleteByIds(id,sysUserRoles.getUid());
        }else
            return R.error("权限不足，请联系管理员");
    }

}
