package com.cuit.business.reservation.domain;

import com.cuit.system.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author xym
 * @since 2024-01-17 14:57
 * @description 预定信息表
 */

@Data
public class Reservation extends BaseEntity {

    /*展会id*/
    private Long eid;

    /*展馆id*/
    private Long hid;

    /*开始时间*/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8",shape = JsonFormat.Shape.STRING)
    private LocalDateTime startTime;

    /*截止时间*/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8",shape = JsonFormat.Shape.STRING)
    private LocalDateTime endTime;

    /*创建人id*/
    private Long createId;

    /*修改人id*/
    private Long updateId;
}
