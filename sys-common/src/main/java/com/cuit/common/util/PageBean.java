package com.cuit.common.util;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

//分页查询的工具类
@Data
@AllArgsConstructor
public class PageBean<T> {
   /* 总记录数*/
    private Integer totalCount;
   /*当前页的数据*/
    private List<T> rows;

}
