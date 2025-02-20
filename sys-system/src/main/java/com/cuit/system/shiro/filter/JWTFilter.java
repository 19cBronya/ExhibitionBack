package com.cuit.system.shiro.filter;

import com.cuit.common.util.JWTUtils;
import com.cuit.common.util.RedisUtil;
import com.cuit.common.util.SpringContextUtils;
import com.cuit.system.shiro.shiro.JWTToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

@Slf4j
public class JWTFilter extends BasicHttpAuthenticationFilter {

    //是否允许访问，如果带有 token，则对 token 进行检查，否则直接通过
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        //判断请求的请求头是否带上 "Token"
        if (isLoginAttempt(request, response)) {
            //如果存在，则进入 executeLogin 方法执行登入，检查 token 是否正确
            try {
                executeLogin(request, response);
                return true;
            } catch (Exception e) {
                log.error("认证出错");
                responseError(response, e.getMessage()); //这里就不进行跳转了，直接全局异常捕获
            }
        }
        //如果请求头不存在 Token，则可能是执行登陆操作或者是游客状态访问，无需检查 token，直接返回 true
        return true;
    }

    /**
     * 判断用户的请求是否为认证。
     * 检测 header 里面是否包含 Token 字段
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        System.out.println("是认证请求isLoginAttempt");
        HttpServletRequest req = (HttpServletRequest) request;
        String token = req.getHeader("token");
        System.out.println(token);
        return token != null;
    }

    /*
     * executeLogin实际上就是先调用createToken来获取token，这里重写了这个方法，就不会自动去调用createToken来获取token
     * 然后调用getSubject方法来获取当前用户再调用login方法来实现登录
     * */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        System.out.println("executeLogin");
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String token = req.getHeader("token");
        JWTToken jwt = null;

        String newJwtToken = getNewJwtToken(req, res, token);
        if (newJwtToken != null) {
            token = newJwtToken;
        }

        jwt = new JWTToken(token);
        //交给自定义的realm对象去登录，如果错误他会抛出异常并被捕获
        getSubject(request, response).login(jwt);
        return true;
    }

    /**
     * @Description: token续期
     * @param request 请求
     * @param response 响应
     * @param token 令牌
     * @return: java.lang.String
     */
    private String getNewJwtToken(HttpServletRequest request, HttpServletResponse response, String token) throws Exception {
        RedisUtil redisUtil = SpringContextUtils.getBean(RedisUtil.class);
        String uid = null;
        try {
            uid = JWTUtils.getUserId(token);
        } catch (Exception e) {
            log.error("token非法，不是规范的token，可能被篡改了");
            throw new AuthenticationException("token非法，不是规范的token，可能被篡改了");
        }
        if (!JWTUtils.verify(token) || uid == null) {
            log.error("token认证失效，请重新登陆");
            throw new AuthenticationException("token认证失效，请重新登陆");
        }

        String refreshToken = String.valueOf(redisUtil.hget("refreshToken",uid));
        String accessToken = JWTUtils.getAccessToken(token);

        if (StringUtils.isBlank(refreshToken) || !accessToken.equals(refreshToken)) {
            log.error("token过期，请重新登陆");
            throw new AuthenticationException("token过期，请重新登陆");
        }

        //token续期
        if (JWTUtils.isJwtExpired(token) && accessToken.equals(refreshToken)) {
            //生成新token
            token = JWTUtils.createToken(Long.valueOf(uid));
            log.info("token续期成功:" + token);
            response.addHeader("refreshToken", token);
            response.setHeader("Access-Control-Expose-Headers", "refreshToken");
            return token;
        }
        return null;
    }

    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        System.out.println("preHandle");
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        res.setHeader("Access-control-Allow-Origin", req.getHeader("Origin"));
        res.setHeader("Access-control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        res.setHeader("Access-control-Allow-Headers", req.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
        if (req.getMethod().equals(RequestMethod.OPTIONS.name())) {
            res.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }

    /**
     * 将非法请求跳转到 /unauthorized/**
     */
    private void responseError(ServletResponse response, String message) {
        System.out.println("responseError");

        try {
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            //设置编码，否则中文字符在重定向时会变为空字符串
            message = URLEncoder.encode(message,"UTF-8");
            httpServletResponse.sendRedirect("/exhibitionbk/unauthorized?message" + "=" + message);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
