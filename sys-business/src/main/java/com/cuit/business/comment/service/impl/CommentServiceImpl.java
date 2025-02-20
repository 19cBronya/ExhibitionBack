package com.cuit.business.comment.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cuit.business.comment.domain.Comment;
import com.cuit.business.comment.domain.dto.CommentDTO;
import com.cuit.business.comment.mapper.CommentMapper;
import com.cuit.business.comment.service.ICommentService;
import com.cuit.common.common.CommonConstants;
import com.cuit.common.common.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public List<CommentDTO> selectListByPage(Long id) {
        return commentMapper.selectListByPage(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R deleteByIds(Long[] ids) {
        for (Long id : ids) {
            Comment comment = commentMapper.selectById(id);
            comment.setDelFlag(CommonConstants.DeleteFlag.删除.getValue());
            commentMapper.updateById(comment);
            List<Comment> commentList = commentMapper.selectCommentList(comment.getId());
            for (Comment list : commentList) {
                list.setDelFlag(CommonConstants.DeleteFlag.删除.getValue());
                commentMapper.updateById(list);
            }
        }
        return R.success("删除成功");
    }

    @Override
    public List<CommentDTO> selectListByUid(Long uid) {
        return commentMapper.selectListByUid(uid);
    }

    @Override
    public List<CommentDTO> selectReplyByPage(Long id) {
        return commentMapper.selectReplyByPage(id);
    }
}
