package com.cuit.system.sysUser.service;



import com.baomidou.mybatisplus.extension.service.IService;
import com.cuit.common.common.R;
import com.cuit.system.sysUser.domain.SysUser;
import com.cuit.system.sysUser.domain.vo.EmailLogin;
import com.cuit.system.sysUser.domain.vo.UserLogin;
import com.cuit.system.sysUser.domain.vo.UserVo;

import java.util.List;

public interface ISysUserService extends IService<SysUser> {

    SysUser emailLogin(EmailLogin sysUser);

    SysUser login(UserLogin sysUser);

    String register(UserVo user);

    SysUser selectByid(Long uid);

    R deleteById(Long[] id);

    com.cuit.common.common.R forbiddenStatus(String status, Long[] id);

    String selectById(Long id);

    Long selectOrgIdById(Long id);
}
