package com.cuit.pay.orders.controller;


import cn.hutool.core.util.StrUtil;
import com.cuit.business.booth.domain.Booth;
import com.cuit.business.booth.domain.BoothReservation;
import com.cuit.business.booth.service.IBoothReservationService;
import com.cuit.business.booth.service.IBoothService;
import com.cuit.business.exhibition.domain.Exhibition;
import com.cuit.business.exhibition.domain.dto.ExhibitionOrder;
import com.cuit.business.exhibition.service.IExhibitionService;
import com.cuit.business.exhibitor.service.IExhibitorService;
import com.cuit.common.common.CommonConstants;
import com.cuit.common.common.Constants;
import com.cuit.common.common.R;
import com.cuit.common.common.page.TableDataInfo;
import com.cuit.pay.orders.domain.Orders;
import com.cuit.pay.orders.domain.dto.OrdersDTO;
import com.cuit.pay.orders.service.IOrdersService;
import com.cuit.system.base.BaseController;
import com.cuit.system.log.anno.Log;
import com.cuit.system.sysUser.domain.SysUser;
import com.cuit.system.sysUserRoles.domain.SysUserRoles;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Api(tags = "订单控制类")
@RestController
@RequestMapping("/order")
public class OrdersController extends BaseController {

    @Autowired
    private IOrdersService ordersService;

    @Autowired
    private IExhibitorService exhibitorService;

    @Autowired
    private IBoothService boothService;

    @Autowired
    private IBoothReservationService boothReservationService;

    @Autowired
    private IExhibitionService exhibitionService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /*分页查询登录用户当前订单信息")*/
    @ApiOperation("分页查询登录用户当前订单信息")
    @RequiresAuthentication
    @PostMapping("/page")
    public R<TableDataInfo> page(@RequestBody OrdersDTO orders) {
        SysUserRoles sysUser = getSysUserRoles();
        List<OrdersDTO> list = null;
        startPage(orders);
        if(sysUser.getRid().toString().equals(CommonConstants.UserRole.参展商.getValue())){
            list = ordersService.selectListByIdList(sysUser.getUid(),orders);
        }else if(sysUser.getRid().toString().equals(CommonConstants.UserRole.办展机构.getValue())){
          list = ordersService.selectListByEidList(sysUser.getUid(),orders);
        } else{
           list = ordersService.selectList(sysUser.getUid(),orders);
        }
        if (list == null || list.size() == 0) {
            return R.success("未查询到符合条件的数据信息");
        }
        return R.success("查询成功", getDataTable(list));
    }

    /*用户新增订单*/
    @Log
    @ApiOperation("新增展会订单")
    @RequiresAuthentication
    @PostMapping("/add")
    public R addOrder(@RequestBody ExhibitionOrder exhibitionOrder) {

        SysUserRoles sysUserRoles = getSysUserRoles();

        Orders orders = new Orders();
        orders.setEid(exhibitionOrder.getId());
        Exhibition exhibition = exhibitionService.getById(exhibitionOrder.getId());
        orders.setPurchaseType(CommonConstants.PurchaseType.展会订票.getValue());
        orders.setOrderName(exhibitionOrder.getExhibitionName()+"订单");
        String currentDate = LocalDate.now().toString().replace("-", "");
        String randomNumber = generateRandomNumber(13);
        orders.setOrderNo(currentDate+randomNumber);
        orders.setPayStatus(CommonConstants.PayStatus.未支付.getValue());
        if(exhibitionOrder.getTicketCount() > exhibition.getTotalVotes()){
            return R.error("该展会剩余票数不足");
        }
        orders.setTicketCount(exhibitionOrder.getTicketCount());
        orders.setPayMoney(exhibitionOrder.getTicketPrice().multiply(new BigDecimal(exhibitionOrder.getTicketCount()), MathContext.DECIMAL32));
        if(CommonConstants.UserRole.专业观众.getValue().equals(sysUserRoles.getRid().toString()))
            orders.setPayMoney(exhibitionOrder.getTicketPrice().multiply(new BigDecimal(exhibitionOrder.getTicketCount()), MathContext.DECIMAL32).multiply(new BigDecimal(0.8)));
        orders.setCreateId(sysUserRoles.getUid());
        orders.setUpdateId(sysUserRoles.getUid());
        orders.setStartTime(exhibitionOrder.getOpeningTime().toString());
        orders.setEndTime(exhibitionOrder.getClosingTime().toString());

        orderRecord(Constants.ORDER_SUBMIT_NAME,orders);

        ordersService.save(orders);
        return R.success("订单生成成功，请在30分钟内完成支付",orders);
    }

    /*记录订单30分钟过期时间*/
    private void orderRecord(String key,Orders orders) {
        String hkey = key + orders.getOrderNo();
        redisTemplate.opsForHash().put(hkey, orders.getOrderNo(), orders);
        redisTemplate.expire(hkey, 30, TimeUnit.MINUTES);
    }


    /*展位租赁订单*/
    @Log
    @ApiOperation("参展商租赁展位")
    @RequiresAuthentication
    @PostMapping("/rent")
    @Transactional(rollbackFor = Exception.class)
    public R rentBooth(@RequestBody BoothReservation boothReservation) {
        SysUserRoles sysUserRoles = getSysUserRoles();
        if (StrUtil.equalsAny(sysUserRoles.getRid().toString(),
                CommonConstants.UserRole.参展商.getValue(),
                CommonConstants.UserRole.管理员.getValue())) {

            boothReservation.setEtorid(boothReservation.getEtorid());
            boothReservation.setCreateId(sysUserRoles.getUid());
            boothReservation.setUpdateId(sysUserRoles.getUid());

            /*展位预定信息新增*/
            boolean boothReservationInsert = boothReservationService.save(boothReservation);

            /*展会信息*/
            Exhibition exhibition = exhibitionService.getById(boothReservation.getEid());
            if(exhibition==null)
                return R.error("未查询到该展会信息");

            Booth booth = boothService.getById(boothReservation.getBid());
            if (CommonConstants.SoldStatus.已售出.getValue().equals(booth.getSoldStatus())){
                return R.error("租赁失败,该展位已被其他参展商租赁");
            }
            /*订单生成*/
            Orders orders = new Orders();

            orders.setEid(boothReservation.getEid());
            orders.setBid(boothReservation.getBid());
            orders.setPurchaseType(CommonConstants.PurchaseType.展位租赁.getValue());
            orders.setOrderName(exhibition.getExhibitionName() + ",展位信息：" + boothReservation.getBoothName() + "预定订单");
            //订单编号
            String currentDate = LocalDate.now().toString().replace("-", "");
            String randomNumber = generateRandomNumber(13);
            orders.setOrderNo(currentDate+randomNumber);

            orders.setPayStatus(CommonConstants.PayStatus.未支付.getValue());

            orders.setPayMoney(boothReservation.getPrice());
            orders.setStartTime(exhibition.getOpeningTime().toString());
            orders.setEndTime(exhibition.getClosingTime().toString());

            orders.setCreateId(boothReservation.getEtorid());
            orders.setUpdateId(boothReservation.getEtorid());

            orderRecord(Constants.ORDER_SUBMIT_NAME,orders);

            /*插入订单信息*/
            boolean ordersInsert = ordersService.save(orders);

            if (boothReservationInsert && ordersInsert) {
                return R.success("租赁订单生成成功，请在30分钟完成支付",orders);
            }

            return R.error("租赁申请失败");
        }
        return R.error(CommonConstants.ConstantsCode.错误.getValue(), "您没有权限操作");
    }

    public static String generateRandomNumber(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

}
