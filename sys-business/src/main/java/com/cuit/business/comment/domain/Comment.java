package com.cuit.business.comment.domain;

import com.cuit.system.base.BaseEntity;
import lombok.Data;

/**
 * 评论实体类
 */
@Data
public class Comment  extends BaseEntity {

    /**
     * 主评论id
     */
    private Long mainId;

    /**
     * 评论用户id
     */
    private Long uid;

    /**
     * 回复用户id
     */
    private Long replyUid;

    /**
     * 订单id
     */
    private Long oid;

    /**
     * 评论星级
     */
    private Integer star;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 状态标志
     */
    private String status;

    /**
     * 创建人id
     */
    private Long createId;

    /**
     * 修改人id
     */
    private Long updateId;

}
