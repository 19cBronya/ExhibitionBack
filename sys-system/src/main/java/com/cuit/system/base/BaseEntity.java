package com.cuit.system.base;


import com.alibaba.excel.annotation.ExcelIgnore;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @Id
    @Column(name = "id")
    @ExcelIgnore
    private Long id;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    @ExcelIgnore
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "update_time")
    @ExcelIgnore
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 删除标记
     */
    @ExcelIgnore
    private String delFlag;

    //分页参数
    @Transient
    @TableField(exist = false)
    @ExcelIgnore
    private Integer pageNum;

    @Transient
    @TableField(exist = false)
    @ExcelIgnore
    private Integer pageSize;

    @Transient
    @TableField(exist = false)
    @ExcelIgnore
    @JsonIgnore
    private String orderBy;

    @Transient
    @TableField(exist = false)
    @ExcelIgnore
    @JsonIgnore
    private String isAsc;

    @Transient
    @TableField(exist = false)
    @ExcelIgnore
    @JsonIgnore
    private Boolean reasonable;

}
