package com.cuit.business.exhibition.controller;

import cn.hutool.core.util.StrUtil;
import com.cuit.business.booth.service.IBoothService;
import com.cuit.business.exhibition.domain.Exhibition;
import com.cuit.business.exhibition.domain.dto.ExhibitionDTO;
import com.cuit.business.exhibition.service.IExhibitionService;
import com.cuit.business.exhibitor.domain.Exhibitor;
import com.cuit.business.exhibitor.domain.dto.ExhibitorDTO;
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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Api(tags = "展览会信息控制类")
@RestController
@RequestMapping("/exhibition")
public class ExhibitionController extends BaseController {

    @Autowired
    private IExhibitionService exhibitionService;

    @Autowired
    private IBoothService boothService;

    @Autowired
    private IExhibitorService exhibitorService;


    /*图片路径补充*/
    @Value("${Image.path}")
    private String basePath;

    /**
     * 首页展览会信息分页查询
     * @param exhibitionDTO
     * @return
     */
    @Log
    @ApiOperation("展览会信息分页查询")
    @RequiresAuthentication
    @PostMapping("/page")
    public R<TableDataInfo> page(@RequestBody ExhibitionDTO exhibitionDTO) {
        SysUserRoles sysUserRoles = getSysUserRoles();
        if(!(CommonConstants.UserRole.管理员.getValue().equals(sysUserRoles.getRid().toString())||
                CommonConstants.UserRole.办展机构.getValue().equals(sysUserRoles.getRid().toString()))){
            exhibitionDTO.setStatus(CommonConstants.Status.启用.getValue());
        }
        startPage(exhibitionDTO);
        List<ExhibitionDTO> list = exhibitionService.selectList(exhibitionDTO);
        if (list.size() == 0) {
            return R.success("未查询到符合条件的数据信息");
        }
        for (ExhibitionDTO dto : list) {
            /*展馆图片信息*/
            String exhibitionUrl = dto.getExhibitionUrl();
            if (exhibitionUrl!=null && !exhibitionUrl.contains(basePath)) {
                exhibitionUrl = basePath + exhibitionUrl;
            }
            dto.setExhibitionUrl(exhibitionUrl);
        }
        return R.success("查询成功", getDataTable(list));
    }

    /**
     * 旗下展览会信息分页查询
     * @param exhibitionDTO
     * @return
     */
    @Log
    @ApiOperation("旗下展览会信息分页查询")
    @RequiresAuthentication
    @PostMapping("/page/selectByid")
    public R<TableDataInfo> pageSelectByid(@RequestBody ExhibitionDTO exhibitionDTO) {
        SysUserRoles sysUserRoles = getSysUserRoles();
        if(!(CommonConstants.UserRole.管理员.getValue().equals(sysUserRoles.getRid().toString())
                ||CommonConstants.UserRole.办展机构.getValue().equals(sysUserRoles.getRid().toString())
                || CommonConstants.UserRole.展览馆.getValue().equals(sysUserRoles.getRid().toString()))){
            exhibitionDTO.setStatus(CommonConstants.Status.启用.getValue());
        }
        startPage(exhibitionDTO);
        List<ExhibitionDTO> list = exhibitionService.selectListById(exhibitionDTO,sysUserRoles.getUid());
        if (list.size() == 0) {
            return R.success("未查询到符合条件的数据信息");
        }
        for (ExhibitionDTO dto : list) {
            /*展馆图片信息*/
            String exhibitionUrl = dto.getExhibitionUrl();
            if (exhibitionUrl!=null && !exhibitionUrl.contains(basePath)) {
                exhibitionUrl = basePath + exhibitionUrl;
            }
            dto.setExhibitionUrl(exhibitionUrl);
        }
        return R.success("查询成功", getDataTable(list));
    }

    /**
     * 查看展会详情
     */
    @Log
    @ApiOperation("查看展会详情")
    @RequiresAuthentication
    @GetMapping("/detail")
    public R<ExhibitionDTO> detail(@RequestParam("id") String id) {

        ExhibitionDTO dto = exhibitionService.selectById(Long.valueOf(id));
        if (dto == null) {
            return R.error("未查询到数据信息");
        }
        /*展馆图片信息*/
        String exhibitionUrl = dto.getExhibitionUrl();
        if (exhibitionUrl !=null && !exhibitionUrl.contains(basePath)) {
            exhibitionUrl = basePath + exhibitionUrl;
        }
        dto.setExhibitionUrl(exhibitionUrl);

        return R.success("查询成功", dto);
    }

    /**
     * 对应展会下参展商信息查询
     * @Pargram id 展览会id
     */
    @Log
    @ApiOperation("展览会下参展商信息分页查询")
    @RequiresAuthentication
    @GetMapping("/page/exhibitor")
    public R<TableDataInfo> exhibitorPage(@RequestParam("id") String id){
        startPage();
        List<ExhibitorDTO> list = exhibitorService.selectList(Long.valueOf(id));
        if (list.size() == 0) {
            return R.success("未查询到符合条件的数据信息");
        }
        for (ExhibitorDTO dto : list) {
            /*展馆图片信息*/
            String exhibitionUrl = dto.getCompanyLogo();
            if (exhibitionUrl !=null && !exhibitionUrl.contains(basePath)) {
                exhibitionUrl = basePath + exhibitionUrl;
            }
            dto.setCompanyLogo(exhibitionUrl);
        }
        return R.success("查询成功", getDataTable(list));
    }

    /**
     * 新增展会信息
     * @param exhibition
     * @return
     */
    @Log
    @RequiresAuthentication
    @ApiOperation(value = "新增展会信息")
    @PostMapping("/add")
    public R add(@RequestBody Exhibition exhibition) {
        SysUserRoles sysUserRoles = getSysUserRoles();
        if (StrUtil.equalsAny(sysUserRoles.getRid().toString(),
                CommonConstants.UserRole.办展机构.getValue(),
                CommonConstants.UserRole.管理员.getValue())) {
            return exhibitionService.saveExhibitionInfor(exhibition,sysUserRoles.getUid());
        }
        return R.error(CommonConstants.ConstantsCode.错误.getValue(),"您没有权限操作");
    }

    /**
     * 修改展会信息
     */
    @Log
    @RequiresAuthentication
    @ApiOperation(value = "修改展会信息")
    @PostMapping("/update")
    public R update(@RequestBody Exhibition exhibition) {
        SysUserRoles sysUserRoles = getSysUserRoles();
        if (StrUtil.equalsAny(sysUserRoles.getRid().toString(),
                CommonConstants.UserRole.办展机构.getValue(),
                CommonConstants.UserRole.管理员.getValue())) {

            Exhibition exhibitionNew = exhibitionService.getById(exhibition.getId());
            // 更新数据
            BeanUtils.copyProperties(exhibition, exhibitionNew, getNullPropertyNames(exhibition));

            boolean b = exhibitionService.updateById(exhibitionNew);
            if(!b){
                return R.error("修改信息失败");
            }
            return R.success("修改信息成功");

        }
        return R.error("您没有权限操作");
    }


    /**
     * 展会类型返回
     */
    @Log
    @RequiresAuthentication
    @ApiOperation(value = "展会类型返回")
    @PostMapping("/getTypeList")
    public R exhibitionType() {
        List<String> typeList = exhibitionService.selectExhibitionType();
        return R.success(typeList);
    }

    /**
     * 展会信息批量删除
     */
    @Log
    @RequiresAuthentication
    @ApiOperation(value = "展会信息批量删除")
    @DeleteMapping
    public R delete(@RequestParam("id") String[] id) {
        SysUserRoles sysUserRoles = getSysUserRoles();
        if (StrUtil.equalsAny(sysUserRoles.getRid().toString(),
                CommonConstants.UserRole.办展机构.getValue(),
                CommonConstants.UserRole.管理员.getValue())) {
            Long[] longIds = Arrays.stream(id)
                    .mapToLong(Long::parseLong)
                    .boxed()
                    .toArray(Long[]::new);
           return exhibitionService.deleteByIds(longIds);
        }
        return R.error("您没有权限操作");
    }

}
