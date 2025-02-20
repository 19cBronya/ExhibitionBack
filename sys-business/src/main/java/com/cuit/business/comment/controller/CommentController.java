package com.cuit.business.comment.controller;

import com.cuit.business.comment.domain.Comment;
import com.cuit.business.comment.domain.dto.CommentDTO;
import com.cuit.business.comment.service.ICommentService;
import com.cuit.common.common.R;
import com.cuit.common.common.page.TableDataInfo;
import com.cuit.system.base.BaseController;
import com.cuit.system.log.anno.Log;
import com.cuit.system.sysUserRoles.domain.SysUserRoles;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Api(tags = "评论模块控制类")
@Slf4j
@RestController
@RequestMapping("/comment")
public class CommentController extends BaseController {

    @Autowired
    private ICommentService commentService;

    /*敏感词库*/
    private List<String> sensitiveWords;

    /*图片路径补充*/
    @Value("${Image.path}")
    private String basePath;

    public CommentController() {
        loadSensitiveWords();
    }

    /**
     * 查询当前用户评论
     */
    @Log
    @ApiOperation("分页查询当前用户评论")
    @RequiresAuthentication
    @PostMapping("/self")
    public R<TableDataInfo> getSelfComment()
    {
        SysUserRoles sysUserRoles = getSysUserRoles();
        startPage();
        List<CommentDTO> list = commentService.selectListByUid(sysUserRoles.getUid());
        if (list.size() == 0)
            return R.success("暂无信息");
        for (CommentDTO commentDTO : list) {
            /*图片显示前缀*/
            if (commentDTO.getAvatarUrl() != null) {
                if(!commentDTO.getAvatarUrl().contains(basePath))
                    commentDTO.setAvatarUrl(basePath + commentDTO.getAvatarUrl());
            }
        }
        return R.success("查询成功",getDataTable(list));
    }

    /**
     * 发表订单评论
     */
    @Log
    @ApiOperation("发表订单评论")
    @RequiresAuthentication
    @PostMapping("/publish")
    public R addComment(@RequestBody Comment comment)
    {
        SysUserRoles sysUserRoles= getSysUserRoles();
        comment.setUid(sysUserRoles.getUid());
        comment.setCreateId(sysUserRoles.getUid());
        comment.setUpdateId(sysUserRoles.getUid());
        // 进行敏感词校验
        String sensitiveWord = findSensitiveWord(comment.getContent());
        if (sensitiveWord != null) {
            return R.error("评论内容包含敏感词：" + sensitiveWord + ",请重新输入");
        }
        commentService.save(comment);
        return R.success("发表成功");
    }


    /**
     * 查询会展下评论
     * @param id 会展id
     */
    @Log
    @ApiOperation("分页查询服务下评论")
    @RequiresAuthentication
    @GetMapping("/select")
    public R<TableDataInfo> getComment(@RequestParam("id") String id)
    {
        startPage();
        List<CommentDTO> list = commentService.selectListByPage(Long.valueOf(id));
        if (list.size() == 0)
            return R.success("暂无信息");
        for (CommentDTO commentDTO : list) {
            /*图片显示前缀*/
            if (commentDTO.getAvatarUrl() != null) {
                if(!commentDTO.getAvatarUrl().contains(basePath))
                    commentDTO.setAvatarUrl(basePath + commentDTO.getAvatarUrl());
            }
        }
        return R.success("查询成功",getDataTable(list));
    }


    /**
     * 删除自己发的评论
     */
    @Log
    @ApiOperation("删除自己发的评论")
    @RequiresAuthentication
    @DeleteMapping
    public R deleteComment(@RequestParam("id") String[] id)
    {
        Long[] longIds = Arrays.stream(id)
                .mapToLong(Long::parseLong)
                .boxed()
                .toArray(Long[]::new);
        return commentService.deleteByIds(longIds);
    }

    private void loadSensitiveWords() {
        sensitiveWords = new ArrayList<>();
        Resource resource = new ClassPathResource("file/sensitive_words.txt");
        try (BufferedReader reader = new BufferedReader(new FileReader(resource.getFile()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] words = line.split(",");
                for (String word : words) {
                    sensitiveWords.add(word.trim().toLowerCase());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 回复评论
     * @Param id 评论id
     */
    @ApiOperation("回复评论")
    @RequiresAuthentication
    @PostMapping("/reply")
    public R replyComment(@RequestParam("id") String id,@RequestBody Comment comment){
        SysUserRoles sysUserRoles= getSysUserRoles();
        comment.setMainId(Long.valueOf(id));
        comment.setReplyUid(sysUserRoles.getUid());
        comment.setCreateId(sysUserRoles.getUid());
        comment.setUpdateId(sysUserRoles.getUid());
        // 进行敏感词校验
        String sensitiveWord = findSensitiveWord(comment.getContent());
        if (sensitiveWord != null) {
            return R.error("评论内容包含敏感词：" + sensitiveWord + ",请重新输入");
        }
        commentService.save(comment);
        return R.success("回复成功");
    }

    /**
     * 查看回复评论
     * @Param id 评论id
     */
    @ApiOperation("查看回复评论")
    @RequiresAuthentication
    @GetMapping("/select/reply")
    public R<TableDataInfo> getReplyComment(@RequestParam("id") String id)
    {
        startPage();
        List<CommentDTO> list = commentService.selectReplyByPage(Long.valueOf(id));
        if (list.size() == 0)
            return R.success("暂无信息");
        for (CommentDTO commentDTO : list) {
            /*图片显示前缀*/
            if (commentDTO.getAvatarUrl() != null) {
                if(!commentDTO.getAvatarUrl().contains(basePath))
                    commentDTO.setAvatarUrl(basePath + commentDTO.getAvatarUrl());
            }
        }
        return R.success("查询成功",getDataTable(list));
    }


    private String findSensitiveWord(String content) {
        content = content.toLowerCase();
        for (String word : sensitiveWords) {
            if (content.contains(word)) {
                return word;
            }
        }
        return null;
    }

}

