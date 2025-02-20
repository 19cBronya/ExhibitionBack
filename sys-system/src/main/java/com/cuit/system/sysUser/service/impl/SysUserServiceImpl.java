package com.cuit.system.sysUser.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cuit.common.common.CommonConstants;
import com.cuit.common.common.R;
import com.cuit.common.common.ServiceException;
import com.cuit.common.util.Md5Utils;
import com.cuit.common.util.RedisUtil;
import com.cuit.system.sysUser.domain.SysUser;
import com.cuit.system.sysUser.domain.vo.EmailLogin;
import com.cuit.system.sysUser.domain.vo.UserLogin;
import com.cuit.system.sysUser.domain.vo.UserVo;
import com.cuit.system.sysUser.mapper.SysUserMapper;
import com.cuit.system.sysUser.service.ISysUserService;
import com.cuit.system.sysUserRoles.domain.SysUserRoles;
import com.cuit.system.sysUserRoles.mapper.SysUserRolesMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysUserRolesMapper sysUserRolesMapper;

    @Autowired
    private RedisUtil redisUtil;

    /*盐值*/
    @Value("${Salt.value}")
    private String salt;

    @Override
    public SysUser emailLogin(EmailLogin sysUser) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<SysUser>();
        wrapper.eq(SysUser::getEmail, sysUser.getEmail())
                .eq(SysUser::getDelFlag, CommonConstants.DeleteFlag.正常.getValue())
                .eq(SysUser::getStatus,CommonConstants.Status.启用.getValue());
        return sysUserMapper.selectOne(wrapper);
    }

    /*登录*/
    @Override
    public SysUser login(UserLogin sysUser) {
        String loginName = sysUser.getLoginName();
        String password = Md5Utils.encryption(sysUser.getPassword(),salt);
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<SysUser>();
        wrapper.eq(SysUser::getPassword, password)
                .eq(SysUser::getLoginName, loginName)
                .eq(SysUser::getDelFlag, CommonConstants.DeleteFlag.正常.getValue());
        return sysUserMapper.selectOne(wrapper);
    }

    /*注册*/
    @Override
    public String register(UserVo user) {
        String status = CommonConstants.Status.启用.getValue();
        String code = (String) redisUtil.get(user.getEmail());
        if (user.getCode().equals(code)){
            SysUser one = getUserInfo(user);
            if (one == null) {

                SysUser sysUser = new SysUser();
                SysUserRoles sysUserRoles = new SysUserRoles();

                if(user.getRId().intValue()>Long.valueOf(CommonConstants.UserRole.管理员.getValue()).intValue()||user.getRId().intValue()<Long.valueOf(CommonConstants.UserRole.普通观众.getValue()).intValue())
                    throw new ServiceException(CommonConstants.ConstantsCode.错误.getValue(),"角色信息不存在");

                if(!user.getRId().toString().equals(CommonConstants.UserRole.普通观众.getValue())){
                    status = CommonConstants.Status.审核中.getValue();
                }

                BeanUtils.copyProperties(user,sysUser);
                /*密码加盐加密*/
                String pwd= Md5Utils.encryption(user.getPassword(),salt);
                sysUser.setPassword(pwd);
                sysUser.setStatus(status);
                sysUserMapper.insert(sysUser);

                /*新增用户权限表关联数据*/
                SysUser registerUser = getUserInfo(sysUser);
                sysUserRoles.setUid(registerUser.getId());
                sysUserRoles.setRid(user.getRId());
                sysUserRoles.setCreateId(registerUser.getId());
                sysUserRoles.setUpdateId(registerUser.getId());
                sysUserRoles.setStatus(status);
                sysUserRolesMapper.insert(sysUserRoles);

            } else {
                return "用户已存在";
            }
        }else {
            return "验证码错误";
        }
        return null;
    }

    @Override
    public SysUser selectByid(Long uid) {
        return sysUserMapper.selectByid(uid);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R deleteById(Long[] id) {
        LambdaQueryWrapper<SysUser> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.in(id!=null,SysUser::getId,id);
        List<SysUser> list=this.list(queryWrapper);

        for (SysUser sysUser : list) {
            if(CommonConstants.Status.启用.getValue().equals(sysUser.getStatus()))
                return R.error(CommonConstants.ConstantsCode.错误.getValue(), "操作失败，请先禁用所选用户");
        }

        for (SysUser sysUser : list) {
            sysUser.setDelFlag(CommonConstants.DeleteFlag.删除.getValue());
            int i = sysUserMapper.updateById(sysUser);
            int j = 0;
            SysUserRoles sysUserRoles = sysUserRolesMapper.selectByUId(sysUser.getId());
            if(sysUserRoles != null){
                sysUserRoles.setDelFlag(CommonConstants.DeleteFlag.删除.getValue());
                j = sysUserRolesMapper.updateById(sysUserRoles);
            }
            if(i<0||j<0)
                return R.error(CommonConstants.ConstantsCode.错误.getValue(), "操作失败，请联系管理员");
        }
        return R.success("删除成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R forbiddenStatus(String status, Long[] id) {

        for (Long i : id) {
            SysUser sysUser = sysUserMapper.selectById(i);
            sysUser.setStatus(status);
            int updateCount0 = sysUserMapper.updateById(sysUser);
            int updateCount1 = 0;
            SysUserRoles sysUserRoles = sysUserRolesMapper.selectByUId(sysUser.getId());
            if(sysUserRoles != null){
                sysUserRoles.setStatus(status);
                updateCount1 = sysUserRolesMapper.updateById(sysUserRoles);
            }
            if(updateCount0<0||updateCount1<0){
                return R.error(CommonConstants.ConstantsCode.错误.getValue(),"操作失败，请联系管理员");
            }
        }
        if(status.equals(CommonConstants.Status.启用.getValue()))
            return R.success("启用成功");
        return R.success("禁用成功");
    }

    @Override
    public String selectById(Long id) {
        return sysUserMapper.selectOrgNameById(id);
    }

    @Override
    public Long selectOrgIdById(Long id) {
        return sysUserMapper.selectOrgIdById(id);
    }

    /*查询用户信息*/
    private SysUser getUserInfo(SysUser user) {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("login_name", user.getLoginName())
                    .ne("status", CommonConstants.Status.禁用.getValue())
                    .eq("del_flag", CommonConstants.DeleteFlag.正常.getValue());
        SysUser one;
        try {
            one = getOne(queryWrapper); // 从数据库查询用户信息
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceException(CommonConstants.ConstantsCode.系统错误.getValue(), "系统错误");
        }
        return one;
    }

}
