package com.cuit.business.comment.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cuit.business.comment.domain.Comment;
import com.cuit.business.comment.domain.dto.CommentDTO;
import com.cuit.common.common.R;

import java.util.List;

public interface ICommentService extends IService<Comment> {
    List<CommentDTO> selectListByPage(Long id);

    R deleteByIds(Long[] id);

    List<CommentDTO> selectListByUid(Long uid);

    List<CommentDTO> selectReplyByPage(Long id);
}
