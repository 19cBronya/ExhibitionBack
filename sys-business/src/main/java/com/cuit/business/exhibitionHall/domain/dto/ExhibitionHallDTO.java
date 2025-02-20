package com.cuit.business.exhibitionHall.domain.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cuit.business.exhibitionHall.domain.ExhibitionHall;
import lombok.Data;


@Data
@TableName("exhibition_hall")
public class ExhibitionHallDTO extends ExhibitionHall {

    /*管理人姓名*/
    @TableField(exist = false)
    private String manageName;

    /**
     * 当前已被预约数
     */
    @TableField(exist = false)
    private String num;

    @TableField(exist = false)
    private String phone;
}
