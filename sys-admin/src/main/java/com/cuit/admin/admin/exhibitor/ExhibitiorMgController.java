package com.cuit.admin.admin.exhibitor;

import com.cuit.admin.admin.exhibitor.domain.ExhibitiorMg;
import com.cuit.admin.admin.user.domain.UserMg;
import com.cuit.business.exhibitor.domain.dto.ExhibitorDTO;
import com.cuit.business.exhibitor.service.IExhibitorService;
import com.cuit.common.common.CommonConstants;
import com.cuit.common.common.ServiceException;
import com.cuit.common.util.excel.ExcelUtil;
import com.cuit.system.base.BaseController;
import com.cuit.system.log.anno.Log;
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

/**
 * @author xym
 * @since 2023-12-05 15:31
 * @description 参展商管理
 */
@Slf4j
@Api(tags ="参展商管理")
@RestController
@RequestMapping("/admin/exhibitior")
public class ExhibitiorMgController extends BaseController {

    @Autowired
    private IExhibitorService exhibitorService;

    /**
     * 参展商信息导出
     */
    @Log
    @ApiOperation("参展商信息导出")
    @RequiresAuthentication
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody ExhibitorDTO exhibitorDTO) throws Exception {
        List<ExhibitiorMg> mgList = new ArrayList<>();
        List<ExhibitorDTO> list = exhibitorService.selectListByPage(exhibitorDTO);
        if(list.size()>0){
            for (ExhibitorDTO dto : list) {
                ExhibitiorMg exhibitiorMg = new ExhibitiorMg();
                exhibitiorMg.setExhibitorName(dto.getExhibitorName());
                exhibitiorMg.setCompanyName(dto.getCompanyName());
                exhibitiorMg.setCompanyInfo(dto.getCompanyInfo());
                mgList.add(exhibitiorMg);
            }

        ExcelUtil.outputExcel(response, mgList, ExhibitiorMg.class, "参展商信息表");
    } else
            throw new ServiceException(CommonConstants.ConstantsCode.错误.getValue(), "权限不足，请联系管理员");

    }

}
