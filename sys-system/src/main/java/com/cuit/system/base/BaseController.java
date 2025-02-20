package com.cuit.system.base;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cuit.common.common.page.TableDataInfo;
import com.cuit.system.sysUser.domain.SysUser;
import com.cuit.system.sysUserRoles.domain.SysUserRoles;
import com.cuit.system.sysUserRoles.service.ISysUserRolesService;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;

public class BaseController {

    @Autowired
    ISysUserRolesService iSysUserRolesService;
    /**
     * 设置请求分页数据
     */
    protected void startPage()
    {
        PageUtils.startPage();
    }

    /**
     * 设置请求分页数据
     */
    protected void startPage(BaseEntity entity){
        PageUtils.startPage(entity);
    }

    protected void startPage(Integer pageNum,Integer pageSize){
        PageUtils.startPage(pageNum,pageSize);
    }

    /**
     * 响应请求分页数据
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected TableDataInfo getDataTable(List<?> list)
    {
        TableDataInfo rspData = new TableDataInfo();
        rspData.setRows(list);
        rspData.setTotal(new PageInfo(list).getTotal());
        return rspData;
    }

    /**
     * 获取当前用户信息
     */

    protected SysUserRoles getSysUserRoles() {
        SysUser adminnow = (SysUser) SecurityUtils.getSubject().getPrincipal();
        QueryWrapper<SysUserRoles> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", adminnow.getId());
        return iSysUserRolesService.getOne(queryWrapper);
    }

    /**
     * 获取属性值为空的属性名数组
     */
    protected String[] getNullPropertyNames(Object source) {
        BeanWrapper beanWrapper = new BeanWrapperImpl(source);
        PropertyDescriptor[] propertyDescriptors = beanWrapper.getPropertyDescriptors();
        List<String> nullPropertyNames = new ArrayList<>();
        for (PropertyDescriptor pd : propertyDescriptors) {
            String propertyName = pd.getName();
            if (beanWrapper.getPropertyValue(propertyName) == null) {
                nullPropertyNames.add(propertyName);
            }
        }
        return nullPropertyNames.toArray(new String[0]);
    }

}
