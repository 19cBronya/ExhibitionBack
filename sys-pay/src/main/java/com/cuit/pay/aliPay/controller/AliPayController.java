package com.cuit.pay.aliPay.controller;


import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.alipay.easysdk.factory.Factory;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cuit.business.booth.domain.Booth;
import com.cuit.business.booth.domain.BoothReservation;
import com.cuit.business.booth.service.IBoothReservationService;
import com.cuit.business.booth.service.IBoothService;
import com.cuit.business.exhibition.domain.Exhibition;
import com.cuit.business.exhibition.service.IExhibitionService;
import com.cuit.business.exhibitionExhibitor.domain.ExhibitionExhibitor;
import com.cuit.business.exhibitionExhibitor.service.IExhibitionExhibitorService;
import com.cuit.common.common.*;
import com.cuit.pay.aliPay.config.AliPayConfig;
import com.cuit.pay.aliPay.domain.AliPay;
import com.cuit.pay.orders.domain.Orders;
import com.cuit.pay.orders.service.IOrdersService;
import com.cuit.system.base.BaseController;
import com.cuit.system.email.domain.Email;
import com.cuit.system.email.util.MailUtils;
import com.cuit.system.sysUser.domain.SysUser;
import com.cuit.system.sysUser.service.ISysUserService;
import com.cuit.system.sysUserRoles.domain.SysUserRoles;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.MathContext;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author xym
 * @since 2024-01-15 11:22
 * @description 沙箱支付宝模拟支付
 */
@Slf4j
@Api(tags ="支付宝沙箱异步回调管理")
@RestController
@RequestMapping("/alipay")
public class AliPayController{

    private static final String GATEWAY_URL ="https://openapi-sandbox.dl.alipaydev.com/gateway.do";
    private static final String FORMAT ="JSON";
    private static final String CHARSET ="utf-8";
    private static final String SIGN_TYPE ="RSA2";

    @Autowired
    AliPayConfig aliPayConfig;

    @Autowired
    private IOrdersService orderService;

    @Autowired
    private IExhibitionService exhibitionService;

    @Autowired
    private IBoothService boothService;

    @Autowired
    private IBoothReservationService boothReservationService;

    @Autowired
    private IExhibitionExhibitorService exhibitionExhibitorService;

    @Autowired
    private ISysUserService sysUserService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @ApiOperation("支付宝沙箱支付")
    @RequiresAuthentication
    @GetMapping("/pay") //http://localhost:8000/exhibitionbk/alipay/pay?subject=XXX&traceNo=XXX&totalAmount=XXX
    @Transactional(rollbackFor = Exception.class)
    public void pay(String traceNo, HttpServletResponse httpResponse) throws Exception {

        String hkey = Constants.ORDER_SUBMIT_NAME + traceNo;
        Orders orders = orderService.getByOrderNo(traceNo);

        if(orders == null){
            httpResponse.sendError(HttpStatus.BAD_REQUEST.value(), "订单信息不存在");
            return;
        }

        if (!redisTemplate.hasKey(hkey)){
            orders.setDelFlag(CommonConstants.DeleteFlag.删除.getValue());
            orderService.updateById(orders);
            httpResponse.sendError(HttpStatus.BAD_REQUEST.value(), "订单信息已过期");
            return;
        }

        AlipayClient alipayClient = new DefaultAlipayClient(GATEWAY_URL, aliPayConfig.getAppId(),
                aliPayConfig.getAppPrivateKey(), FORMAT, CHARSET, aliPayConfig.getAlipayPublicKey(), SIGN_TYPE);
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setNotifyUrl(aliPayConfig.getNotifyUrl());

        request.setBizContent("{\"out_trade_no\":\"" + traceNo + "\","
                + "\"total_amount\":\"" + orders.getPayMoney() + "\","
                + "\"subject\":\"" + orders.getOrderName() + "\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        request.setReturnUrl("http://localhost:8080/#/personalCenter"); //支付成功后返回界面
        String form = "";
        try {
            form = alipayClient.pageExecute(request).getBody(); // 调用SDK生成表单
        } catch (AlipayApiException e) {
            e.printStackTrace();
            httpResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            httpResponse.getWriter().write("支付请求失败");
            throw new ServiceException(CommonConstants.ConstantsCode.错误.getValue(),"支付失败");
        }
        httpResponse.setContentType("text/html;charset=" + CHARSET);
        httpResponse.getWriter().write(form);// 将完整的表单html输出到页面
        httpResponse.getWriter().flush();
        httpResponse.getWriter().close();
    }

    /*支付成功异步操作*/
    @PostMapping("/notify")  // 注意这里必须是POST接口
    @Transactional(rollbackFor = Exception.class)
    public R payNotify(HttpServletRequest request) throws Exception {
        if (request.getParameter("trade_status").equals("TRADE_SUCCESS")) {
            System.out.println("=========支付宝异步回调========");

            Map<String, String> params = new HashMap<>();
            Map<String, String[]> requestParams = request.getParameterMap();
            for (String name : requestParams.keySet()) {
                params.put(name, request.getParameter(name));
            }

            String traceNo = params.get("out_trade_no");
            String gmtPayment = params.get("gmt_payment");
            String alipayTradeNo = params.get("trade_no");
            // 加锁
            Object lock = new Object();
            synchronized (lock) {
                // 支付宝验签
                if (Factory.Payment.Common().verifyNotify(params)) {
                    // 验签通过
                    System.out.println("交易名称: " + params.get("subject"));
                    System.out.println("交易状态: " + params.get("trade_status"));
                    System.out.println("支付宝交易凭证号: " + params.get("trade_no"));
                    System.out.println("商户订单号: " + params.get("out_trade_no"));
                    System.out.println("交易金额: " + params.get("total_amount"));
                    System.out.println("买家在支付宝唯一id: " + params.get("buyer_id"));
                    System.out.println("买家付款时间: " + params.get("gmt_payment"));
                    System.out.println("买家付款金额: " + params.get("buyer_pay_amount"));

                    // 更新订单为已支付
                    boolean update0 = orderService.updateStatus(traceNo, CommonConstants.PayStatus.支付成功.getValue(), gmtPayment, alipayTradeNo);

                    redisTemplate.delete(Constants.ORDER_SUBMIT_NAME + traceNo);

                    //同步对应展会销售信息
                    Orders orders = orderService.getByOrderNo(traceNo);
                    Exhibition exhibition = exhibitionService.getById(orders.getEid());
                    boolean update = true;
                    if(CommonConstants.PurchaseType.展会订票.getValue().equals(orders.getPurchaseType()))
                    {

                        UpdateWrapper<Exhibition> updateWrapper = new UpdateWrapper<>();

                        updateWrapper.eq("id", exhibition.getId());
                        /*减票数*/
                        updateWrapper.lambda().set(Exhibition::getTotalVotes, exhibition.getTotalVotes() - orders.getTicketCount());
                        /*加总收入*/
                        updateWrapper.lambda().set(Exhibition::getTotalIncome, exhibition.getTotalIncome().add(orders.getPayMoney(), MathContext.UNLIMITED));
                        /*加流量人数*/
                        updateWrapper.lambda().set(Exhibition::getTotalTraffic, exhibition.getTotalTraffic() + orders.getTicketCount());
                        /*修改以上参数*/
                        update = exhibitionService.update(exhibition, updateWrapper);

                        SysUser sysUser = sysUserService.getById(orders.getCreateId());
                        Email emailDto = new Email(sysUser.getEmail(), orders.getOrderName().replace("订单","")+"期待您的光临",
                                "展会时间于"+orders.getStartTime()+"开始至"+orders.getEndTime()+"结束，请及时参加");
                        /**
                         * 生成二维码
                         */
                        String file = HutoolQRCodeGenerator.generateQRCodeWithHutool(orders.getOrderName()+orders.getOrderNo(), "E:\\Exhibition\\sys-common\\src\\main\\resources\\file\\"+orders.getOrderNo()+".png");
                        MailUtils.sendMailByFile(emailDto,file);

                    }else if(CommonConstants.PurchaseType.展位租赁.getValue().equals(orders.getPurchaseType())){
                        Booth booth = boothService.getById(orders.getBid());
                        BoothReservation boothReservation = boothReservationService.selectByBid(booth.getId());

                        if(CommonConstants.SoldStatus.已售出.getValue().equals(booth.getSoldStatus()))
                            return R.error("该展位已售出,支付失败");

                        UpdateWrapper<Booth> updateWrapper = new UpdateWrapper<>();

                        updateWrapper.eq("id", booth.getId());
                        updateWrapper.lambda().set(Booth::getSoldStatus, CommonConstants.SoldStatus.已售出.getValue());
                        update = boothService.update(booth,updateWrapper);

                        ExhibitionExhibitor exhibitionExhibitor = new ExhibitionExhibitor();
                        exhibitionExhibitor.setEid(orders.getEid());
                        exhibitionExhibitor.setEtorid(boothReservation.getEtorid());
                        exhibitionExhibitor.setHid(exhibition.getHid());
                        exhibitionExhibitorService.save(exhibitionExhibitor);

                    }
                    if(update0 && update)
                        return R.success("支付成功");
                }
            }
        }
        return R.error("支付失败");
    }

    /*提交退款申请*/
    @ApiOperation("退款")
    @GetMapping("/refund")
    @Transactional(rollbackFor = Exception.class)
    public R refundPay(String traceNo) throws AlipayApiException {

        Lock lock = new ReentrantLock();

        //加锁
        lock.lock();
        try {
            // 7天无理由退款
            String now = DateUtil.now();
            Orders orders = orderService.getByOrderNo(traceNo);
            if (orders != null) {

                LocalDateTime nowTime = LocalDateTime.now();
                LocalDateTime exhibitionStartTime = LocalDateTime.parse(orders.getStartTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                LocalDateTime exhibitionEndTime = LocalDateTime.parse(orders.getEndTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                if (nowTime.isAfter(exhibitionEndTime)) {
                    return R.error(CommonConstants.ConstantsCode.错误.getValue(), "当前时间已超过展览结束时间，不支持退款");
                }

                long between = DateUtil.between(DateUtil.parseDateTime(orders.getPayTime()), DateUtil.parseDateTime(now), DateUnit.DAY);
                if (between > 7) {
                    return R.error(CommonConstants.ConstantsCode.错误.getValue(), "该订单已超过7天，不支持退款");
                }
            }else{
                return R.error(CommonConstants.ConstantsCode.错误.getValue(), "订单不存在");
            }

            //创建Client，通用SDK提供的Client，负责调用支付宝的API
            AlipayClient alipayClient = new DefaultAlipayClient(GATEWAY_URL,
                    aliPayConfig.getAppId(), aliPayConfig.getAppPrivateKey(), FORMAT, CHARSET,
                    aliPayConfig.getAlipayPublicKey(), SIGN_TYPE);
            //创建 Request，设置参数
            AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
            JSONObject bizContent = new JSONObject();
            //String tradeNo = aliPay.getAlipayTraceNo();
            bizContent.set("trade_no", orders.getPayNo());  // 支付宝回调的订单流水号
            bizContent.set("refund_amount", orders.getPayMoney());  // 订单的总金额
            bizContent.set("out_request_no", traceNo);   //  我的订单编号


            request.setBizContent(bizContent.toString());

            //执行请求
            AlipayTradeRefundResponse response = alipayClient.execute(request);
            if (response.isSuccess()) {  //退款成功
                log.info("调用成功");
                //更新数据库状态(退款中状态)
                boolean update0 = orderService.updateStatus(traceNo, CommonConstants.PayStatus.退款成功.getValue());

                boolean update = true;

                if(CommonConstants.PurchaseType.展会订票.getValue().equals(orders.getPurchaseType())){
                    //同步对应展会销售信息
                    Exhibition exhibition = exhibitionService.getById(orders.getEid());

                    UpdateWrapper<Exhibition> updateWrapper = new UpdateWrapper<>();

                    updateWrapper.eq("id", exhibition.getId());
                    /*加票数*/
                    updateWrapper.lambda().set(Exhibition::getTotalVotes, exhibition.getTotalVotes() + orders.getTicketCount());
                    /*减总收入*/
                    updateWrapper.lambda().set(Exhibition::getTotalIncome, exhibition.getTotalIncome().subtract(orders.getPayMoney(), MathContext.UNLIMITED));
                    /*减流量人数*/
                    updateWrapper.lambda().set(Exhibition::getTotalTraffic, exhibition.getTotalTraffic() - orders.getTicketCount());
                    /*修改以上参数*/
                    update = exhibitionService.update(exhibition, updateWrapper);
                }else if(CommonConstants.PurchaseType.展位租赁.getValue().equals(orders.getPurchaseType())){
                    Booth booth = boothService.getById(orders.getBid());

                    UpdateWrapper<Booth> updateWrapper = new UpdateWrapper<>();

                    updateWrapper.eq("id", booth.getId());
                    updateWrapper.lambda().set(Booth::getSoldStatus, CommonConstants.SoldStatus.未售出.getValue());
                    update = boothService.update(booth,updateWrapper);
                }
                if(update0 && update)
                    return R.success("退款成功");
                return R.error("退款失败");
            } else {
                System.out.println(response.getBody());
                return R.error("退款失败");
            }
        }finally {
            lock.unlock();
        }
    }
}


