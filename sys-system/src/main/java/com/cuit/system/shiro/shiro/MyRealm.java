package com.cuit.system.shiro.shiro;


import com.cuit.common.util.JWTUtils;
import com.cuit.common.util.RedisUtil;
import com.cuit.system.sysUser.domain.SysUser;
import com.cuit.system.sysUser.service.ISysUserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MyRealm extends AuthorizingRealm {

    @Value("${jwt.refreshtoken-expire}")
    //refreshtoken-expire续期过期时间
    private Long REFRESHTOKENEXPIRE;
    @Autowired
    private ISysUserService ISysUserService;
    @Autowired
    private RedisUtil redisUtil;

    //根据token判断此Authenticator是否使用该realm
    //必须重写不然shiro会报错
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    /**
     * 只有当需要检测用户权限的时候才会调用此方法，例如@RequiresRoles,@RequiresPermissions之类的
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        System.out.println("授权~~~~~");
        SysUser user = (SysUser) principals.getPrimaryPrincipal();
        String uid = String.valueOf(user.getId());
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        return info;
    }


    /**
     * 默认使用此方法进行用户名正确与否验证，错误抛出异常即可，在需要用户认证和鉴权的时候才会调用
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("认证~~~~~~~");
        String jwt = (String) token.getCredentials();
        String uid = JWTUtils.getUserId(jwt);
        SysUser redisUser = (SysUser) redisUtil.hget("userInfo",uid);
        if (redisUser != null) {
            return new SimpleAuthenticationInfo(redisUser, jwt, "MyRealm");
        }

        SysUser user = ISysUserService.getById(uid);
        if(user==null)
            throw new AuthenticationException("系统错误，请联系管理员");
        redisUtil.hset("userInfo",uid, user, REFRESHTOKENEXPIRE);
        return new SimpleAuthenticationInfo(user, jwt, "MyRealm");
    }
}
