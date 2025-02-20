package com.cuit.common.util.sql;


import com.github.pagehelper.Page;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * sql查询条件类
 *
 */
public class QueryParam {
    private Map<String, QueryOperation> params = Maps.newHashMap();

    private String orderBy = "";

    private Page<?> page;

    public Map<String, QueryOperation> getParams() {
        return params;
    }


    public boolean containKey(String key) {
        return params.containsKey(key);
    }

    public QueryOperation get(String key) {
        return params.get(key);
    }

    public QueryOperation remove(String key) {
        return params.remove(key);
    }

    public void clear() {
        params.clear();
    }

    public Page<?> getPage() {
        return page;
    }

    public void setPage(Page<?> page) {
        this.page = page;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }


}

class QueryOperation {
    private String column;
    private Object value;

    public QueryOperation(String column, Object value) {
        this.column = column;
        this.value = value;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
