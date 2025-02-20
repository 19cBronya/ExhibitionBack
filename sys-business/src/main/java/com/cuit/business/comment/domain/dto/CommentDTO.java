package com.cuit.business.comment.domain.dto;

import com.cuit.business.comment.domain.Comment;
import lombok.Data;

/**
 * 评论实体类
 */
@Data
public class CommentDTO extends Comment {

    /**
     * 评论用户名
     */
    private String name;

    /**
     * 用户头像
     */
    private String avatarUrl;

    /**
     * 订单名
     */
    private String orderName;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 展会id
     */
    private Long eid;

    /**
     * 展会名
     */
    private String exhibitionName;

}
