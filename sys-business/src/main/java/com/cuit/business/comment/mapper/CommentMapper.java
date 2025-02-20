package com.cuit.business.comment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cuit.business.comment.domain.Comment;
import com.cuit.business.comment.domain.dto.CommentDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
    List<CommentDTO> selectListByPage(Long id);

    List<Comment> selectCommentList(Long id);

    List<CommentDTO> selectListByUid(Long uid);

    List<CommentDTO> selectReplyByPage(Long id);
}
