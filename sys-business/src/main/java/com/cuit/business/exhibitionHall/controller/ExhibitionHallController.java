package com.cuit.business.exhibitionHall.controller;


import cn.hutool.core.util.StrUtil;
import com.cuit.business.exhibition.domain.Exhibition;
import com.cuit.business.exhibition.domain.dto.ExhibitionDTO;
import com.cuit.business.exhibition.service.IExhibitionService;
import com.cuit.business.exhibitionHall.domain.ExhibitionHall;
import com.cuit.business.exhibitionHall.domain.dto.ExhibitionHallDTO;
import com.cuit.business.exhibitionHall.service.IExhibitionHallService;
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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author xym
 * @description 展览馆控制层
 * @since 2023-11-15 14:31
 */

@Slf4j
@Api(tags = "展馆信息控制类")
@RestController
@RequestMapping("/exhibitionHall")
public class ExhibitionHallController extends BaseController {

    @Value("${Image.path}")
    private String basePath;

    @Autowired
    private IExhibitionHallService exhibitionHallService;

    @Autowired
    private IExhibitionService exhibitionService;

    /**
     * 会展馆新增
     */
    @Log
    @RequiresAuthentication
    @ApiOperation(value = "新增展馆信息")
    @PostMapping("/add")
    public R add(@RequestBody ExhibitionHall exhibitionHall) {
        SysUserRoles sysUserRoles = getSysUserRoles();
        if (StrUtil.equalsAny(sysUserRoles.getRid().toString(),
                CommonConstants.UserRole.展览馆.getValue(),
                CommonConstants.UserRole.管理员.getValue())) {
            return exhibitionHallService.saveExhibitionHallInfor(exhibitionHall, sysUserRoles.getUid());
        }
        return R.error(CommonConstants.ConstantsCode.错误.getValue(), "您没有权限操作");
    }

    /**
     * 展馆信息查询
     */
    @Log
    @ApiOperation("展馆信息分页查询")
    @RequiresAuthentication
    @PostMapping("/page")
    public R<TableDataInfo> page(@RequestBody ExhibitionHallDTO exhibitionHallDTO) {
        startPage(exhibitionHallDTO);
        List<ExhibitionHallDTO> list = exhibitionHallService.selectList(exhibitionHallDTO);
        if (list.size() == 0) {
            return R.success("未查询到符合条件的数据信息");
        }
        for (ExhibitionHallDTO dto : list) {
            /*展馆图片信息*/
            String exhibitionUrl = dto.getPicUrl();
            if (exhibitionUrl!= null && !exhibitionUrl.contains(basePath)) {
                exhibitionUrl = basePath + exhibitionUrl;
            }
            dto.setPicUrl(exhibitionUrl);
        }
        return R.success("查询成功", getDataTable(list));
    }

    /**
     * 会展通过审批
     */
    @Log
    @ApiOperation("会展审批")
    @RequiresAuthentication
    @PostMapping("/changeStatus/{status}")
    public R forbiddenUser(@PathVariable String status, @RequestParam("id") String[] id) {
        // 获取登录用户信息
        SysUserRoles sysUserRoles = getSysUserRoles();
        if (CommonConstants.UserRole.管理员.getValue().equals(sysUserRoles.getRid().toString())
                || CommonConstants.UserRole.展览馆.getValue().equals(sysUserRoles.getRid().toString())) {
            Long[] longIds = Arrays.stream(id)
                    .mapToLong(Long::parseLong)
                    .boxed()
                    .toArray(Long[]::new);
            return exhibitionHallService.forbiddenStatus(status, longIds, sysUserRoles.getUid());
        } else
            return R.error(CommonConstants.ConstantsCode.错误.getValue(), "权限不足，请联系管理员");
    }

    /**
     * 展馆信息批量删除
     *
     * @Pargram id 展馆主键id
     */
    @Log
    @RequiresAuthentication
    @ApiOperation(value = "展馆信息批量删除")
    @DeleteMapping
    public R delete(@RequestParam("id") String[] id) {
        SysUserRoles sysUserRoles = getSysUserRoles();
        if (StrUtil.equalsAny(sysUserRoles.getRid().toString(),
                CommonConstants.UserRole.展览馆.getValue(),
                CommonConstants.UserRole.管理员.getValue())) {
            Long[] longIds = Arrays.stream(id)
                    .mapToLong(Long::parseLong)
                    .boxed()
                    .toArray(Long[]::new);
            return exhibitionHallService.deleteByIds(longIds);
        }
        return R.error("权限不足，请联系管理员");
    }

    /**
     * 展馆信息批量启用/禁用
     */
    @Log
    @RequiresAuthentication
    @ApiOperation(value = "展馆信息批量启用/禁用")
    @PostMapping("/updateStatus/{status}")
    public R updateByStatus(@PathVariable String status, @RequestParam("id") String[] id) {
        SysUserRoles sysUserRoles = getSysUserRoles();
        if (StrUtil.equalsAny(sysUserRoles.getRid().toString(),
                CommonConstants.UserRole.展览馆.getValue(),
                CommonConstants.UserRole.管理员.getValue())) {
            Long[] longIds = Arrays.stream(id)
                    .mapToLong(Long::parseLong)
                    .boxed()
                    .toArray(Long[]::new);
            return exhibitionHallService.updateByStatus(status, longIds);
        }
        return R.error("权限不足，请联系管理员");
    }

    /**
     * 展馆信息修改
     */
    @Log
    @RequiresAuthentication
    @ApiOperation(value = "展馆信息修改")
    @PostMapping("/update")
    public R update(@RequestBody ExhibitionHall exhibitionHall) {
        SysUserRoles sysUserRoles = getSysUserRoles();
        if (StrUtil.equalsAny(sysUserRoles.getRid().toString(),
                CommonConstants.UserRole.展览馆.getValue(),
                CommonConstants.UserRole.管理员.getValue())) {

            ExhibitionHall exhibitionHall_new = exhibitionHallService.getById(exhibitionHall.getId());
            // 更新数据
            BeanUtils.copyProperties(exhibitionHall, exhibitionHall_new, getNullPropertyNames(exhibitionHall));

            boolean b = exhibitionHallService.updateById(exhibitionHall_new);
            if (!b) {
                return R.error("修改信息失败");
            }
            return R.success("修改信息成功");

        }
        return R.error("您没有权限操作");
    }

    /**
     * 查看展馆下展会信息
     */
    @Log
    @ApiOperation("查看展馆下展会信息")
    @RequiresAuthentication
    @GetMapping("/selectExhibitionList")
    public R selectExhibitionList(@RequestParam("id") String id) {
        List<ExhibitionDTO> list = exhibitionHallService.selectExhibitionListByHid(Long.valueOf(id));
        if (list.size() == 0) {
            return R.success("未查询到符合条件的数据信息");
        }
        for (ExhibitionDTO dto : list) {
            /*展馆图片信息*/
            String exhibitionUrl = dto.getExhibitionUrl();
            if (exhibitionUrl !=null && !exhibitionUrl.contains(basePath)) {
                exhibitionUrl = basePath + exhibitionUrl;
            }
            dto.setExhibitionUrl(exhibitionUrl);
        }
        return R.success("查询成功", list);
    }

    /**
     * 查看展馆下展会信息分页
     */
    @Log
    @ApiOperation("查看展馆下展会信息分页")
    @RequiresAuthentication
    @PostMapping("/page/selectExhibitionList")
    public R<TableDataInfo> selectExhibitionList(@RequestBody ExhibitionDTO exhibitionDTO, @RequestParam("id") String id) {
        startPage(exhibitionDTO);
        List<ExhibitionDTO> list = exhibitionService.selectExhibitionListPageByHid(exhibitionDTO, Long.valueOf(id));
        if (list.size() == 0) {
            return R.success("未查询到符合条件的数据信息");
        }
        for (ExhibitionDTO dto : list) {
            /*展馆图片信息*/
            String exhibitionUrl = dto.getExhibitionUrl();
            if (exhibitionUrl !=null && !exhibitionUrl.contains(basePath)) {
                exhibitionUrl = basePath + exhibitionUrl;
            }
            dto.setExhibitionUrl(exhibitionUrl);
        }
        return R.success("查询成功", getDataTable(list));
    }


    /**
     * 展览馆查看详情
     */

    @Log
    @ApiOperation("展览馆查看详情")
    @RequiresAuthentication
    @GetMapping("/infor")
    public R<ExhibitionHall> getUserInfor(@RequestParam("id") String id) {
        SysUserRoles sysUserRoles = getSysUserRoles();
        ExhibitionHallDTO exhibitionHallDTO = exhibitionHallService.selectExhibitionHallById(Long.valueOf(id));
        /*展馆图片信息*/
        String picUrl = exhibitionHallDTO.getPicUrl();
        if (picUrl!=null && !picUrl.contains(basePath)) {
            picUrl = basePath + picUrl;
        }
        exhibitionHallDTO.setPicUrl(picUrl);
        return R.success("查询成功", exhibitionHallDTO);
    }

    /**
     * 当前负责人展馆查询
     */
    @Log
    @ApiOperation("当前负责人展馆查询")
    @RequiresAuthentication
    @PostMapping("/selectEhibitionMg")
    public R<TableDataInfo> selectEhibitionHall(@RequestBody ExhibitionHallDTO exhibitionHallDTO) {
        SysUserRoles sysUserRoles = getSysUserRoles();
        if (CommonConstants.UserRole.管理员.getValue().equals(sysUserRoles.getRid().toString())
                || CommonConstants.UserRole.展览馆.getValue().equals(sysUserRoles.getRid().toString())) {
            startPage(exhibitionHallDTO);
            List<ExhibitionHallDTO> list = exhibitionHallService.selectExhibitionHallByCreateId(sysUserRoles.getUid(), exhibitionHallDTO);
            if (list.size() > 0) {
                for (ExhibitionHallDTO dto : list) {
                    /*展馆图片信息*/
                    String exhibitionUrl = dto.getPicUrl();
                    if (!exhibitionUrl.contains(basePath)) {
                        exhibitionUrl = basePath + exhibitionUrl;
                    }
                    dto.setPicUrl(exhibitionUrl);
                }
            }
            return R.success(getDataTable(list));

        } else
            return R.error(CommonConstants.ConstantsCode.错误.getValue(), "权限不足，请联系管理员");
    }
}
