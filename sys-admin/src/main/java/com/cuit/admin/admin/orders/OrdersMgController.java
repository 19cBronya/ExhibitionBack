package com.cuit.admin.admin.orders;

import com.cuit.admin.admin.orders.domain.OrdersMg;
import com.cuit.admin.admin.user.domain.UserMg;
import com.cuit.business.exhibitor.service.IExhibitorService;
import com.cuit.common.common.CommonConstants;
import com.cuit.common.common.R;
import com.cuit.common.common.ServiceException;
import com.cuit.common.common.page.TableDataInfo;
import com.cuit.common.util.excel.ExcelUtil;
import com.cuit.pay.orders.domain.dto.OrdersDTO;
import com.cuit.pay.orders.service.IOrdersService;
import com.cuit.system.base.BaseController;
import com.cuit.system.log.anno.Log;
import com.cuit.system.sysUser.service.ISysUserService;
import com.cuit.system.sysUserRoles.domain.SysUserRoles;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Api(tags = "订单信息管理")
@RestController
@RequestMapping("/admin/orders")
public class OrdersMgController extends BaseController {

    @Autowired
    private IOrdersService ordersService;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private IExhibitorService exhibitorService;

    /*分页查询订单信息*/
    @Log
    @ApiOperation("分页查询订单信息")
    @RequiresAuthentication
    @PostMapping("/page")
    public R<TableDataInfo> ordersPage(@RequestBody OrdersDTO dto){
        SysUserRoles sysUserRoles = getSysUserRoles();
        if (CommonConstants.UserRole.管理员.getValue().equals(sysUserRoles.getRid().toString())) {
            startPage(dto);
            List<OrdersDTO> list = getOrdersDTOS(dto);
            if(list.size()>0){
                for (OrdersDTO ordersDTO : list) {
                    ordersDTO.setPayStatus(CommonConstants.PayStatus.getTextFromValue(ordersDTO.getPayStatus()));
                    ordersDTO.setPurchaseType(CommonConstants.PurchaseType.getTextFromValue(ordersDTO.getPurchaseType()));
                }
            }
            return R.success("查询成功", getDataTable(list));
        }else
            return R.error("权限不足，请联系管理员");
    }

    /*订单信息导出*/
    @Log
    @ApiOperation("订单信息导出")
    @RequiresAuthentication
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody OrdersDTO dto) throws Exception{
        List<OrdersMg> ordersList = new ArrayList<>();
        SysUserRoles sysUserRoles = getSysUserRoles();
        if (CommonConstants.UserRole.管理员.getValue().equals(sysUserRoles.getRid().toString())) {
            List<OrdersDTO> list = getOrdersDTOS(dto);
            if(list.size()>0){
                for (OrdersDTO ordersDTO : list) {
                    OrdersMg ordersMg = new OrdersMg();
                    ordersMg.setOrderName(ordersDTO.getOrderName());
                    ordersMg.setOrderNo(ordersDTO.getOrderNo());
                    if (ordersDTO.getPayNo()!=null)
                        ordersMg.setPayNo(ordersDTO.getPayNo());
                    if (ordersDTO.getPayTime()!=null)
                        ordersMg.setPayTime(ordersDTO.getPayTime());
                    ordersMg.setPayMoney(ordersDTO.getPayMoney());
                    ordersMg.setPayStatus(CommonConstants.PayStatus.getTextFromValue(ordersDTO.getPayStatus()));
                    ordersMg.setPurchaseType(CommonConstants.PurchaseType.getTextFromValue(ordersDTO.getPurchaseType()));
                    ordersMg.setCreateName(ordersDTO.getName());
                    ordersMg.setCreateTime(ordersDTO.getCreateTime().toString());
                    ordersList.add(ordersMg);
                }
            }
            ExcelUtil.outputExcel(response, ordersList, OrdersMg.class, "订单信息表");
        }else
            throw new ServiceException(CommonConstants.ConstantsCode.错误.getValue(),"权限不足，请联系管理员");
    }


    private List<OrdersDTO> getOrdersDTOS(OrdersDTO dto) {
        List<OrdersDTO> list = ordersService.ordersPage(dto);
        for (OrdersDTO ordersDTO : list) {
            if(CommonConstants.PurchaseType.展位租赁.getValue().equals(ordersDTO.getPurchaseType())){
                ordersDTO.setName(exhibitorService.getById(ordersDTO.getCreateId()).getCompanyName());
            }else{
                ordersDTO.setName(sysUserService.getById(ordersDTO.getCreateId()).getName());
            }
        }
        return list;
    }

}
