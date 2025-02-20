package com.cuit.system.base;


import cn.hutool.core.convert.Convert;
import com.cuit.common.common.page.PageDomain;
import com.cuit.common.common.page.TableSupport;
import com.cuit.common.util.sql.SqlUtil;
import com.github.pagehelper.PageHelper;

/**
 * 分页工具类
 *
 * @author sccl
 */
public class PageUtils extends PageHelper
{
    /**
     * 设置请求分页数据
     */
    public static void startPage()
    {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum =  Convert.toInt(pageDomain.getPageNum(), 1);
        Integer pageSize = Convert.toInt(pageDomain.getPageSize(), 20);;
        String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
        Boolean reasonable = pageDomain.getReasonable();
        PageHelper.startPage(pageNum, pageSize, orderBy).setReasonable(reasonable);
    }

    /**
     * 设置请求分页数据
     */
    public static void startPage(BaseEntity entity)
    {
        Integer pageNum = Convert.toInt(entity.getPageNum(), 1);
        Integer pageSize = Convert.toInt(entity.getPageSize(), 10);
        String orderBy = SqlUtil.escapeOrderBySql(entity.getOrderBy());
        Boolean reasonable = entity.getReasonable();
        PageHelper.startPage(pageNum, pageSize, orderBy).setReasonable(reasonable);
    }

    public static void startPage(Integer num, Integer size)
    {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = Convert.toInt(num, 1);
        Integer pageSize = Convert.toInt(size, 10);
        String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
        Boolean reasonable = pageDomain.getReasonable();
        PageHelper.startPage(pageNum, pageSize, orderBy).setReasonable(reasonable);
    }

}
