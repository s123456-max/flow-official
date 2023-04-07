package com.alexmisko.controller;

import com.alexmisko.feign.UserInfoFeign;
import com.alexmisko.filter.AccessContext;
import com.alexmisko.pojo.Comment;
import com.alexmisko.service.CommentService;
import com.alexmisko.vo.LoginUserInfo;
import com.alexmisko.vo.Result;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@Slf4j
@RestController
public class CommentController {

    @Autowired
    CommentService commentService;

    @Autowired
    UserInfoFeign userInfoFeign;

    /**
     * 根据视频id查询所有评论
     * 从第一层开始一层一层往下找
     */
    @GetMapping("/comment/{id}")
    public Result<List<Comment>> getComment(@PathVariable Long id){
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("video_id", id).eq("parent_id", 0);
        // lists为第一级
        List<Comment> lists = commentService.getBaseMapper().selectList(queryWrapper);
        // 只能展示二级评论，增强型for循环不能动态添加数组元素
//        for(Comment list : lists) {
//            QueryWrapper<Comment> queryWrapper1 = new QueryWrapper<>();
//            queryWrapper1.eq("parent_id", list.getId());
//            List<Comment> lists1 = commentService.getBaseMapper().selectList(queryWrapper1);
//            list.setChild(lists1);
//        }
        int n = lists.size();
        for(int i = 0; i < lists.size(); i++){
            QueryWrapper<Comment> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("parent_id", lists.get(i).getId());
            List<Comment> lists1 = commentService.getBaseMapper().selectList(queryWrapper1);
            lists.get(i).setChild(lists1);
            lists.get(i).setUserInfo(userInfoFeign.getUserInfo(lists.get(i).getUserId()).getData());
            // 取父评论的用户id
            if(lists.get(i).getParentId()!=0){
                QueryWrapper<Comment> queryWrapper2 = new QueryWrapper<>();
                queryWrapper2.eq("id", lists.get(i).getParentId());
                Comment comment = commentService.getBaseMapper().selectOne(queryWrapper2);
                lists.get(i).setParentInfo(userInfoFeign.getUserInfo(comment.getUserId()).getData());
            }
            lists.addAll(lists1);
        }
        // 剪枝操作
        for(int i = lists.size(); i > n; i--){
            lists.remove(i - 1);
        }
        return Result.success(lists);
    }

    /**
     * 用户评论
     */
    @PostMapping("/comment/user")
    public Result<String> publishComment(@RequestBody Comment comment){
        log.info("comment: []", comment);
        LoginUserInfo loginUserInfo = AccessContext.getLoginUserInfo();
        Long userId = loginUserInfo.getId();
        comment.setUserId(userId);
        commentService.save(comment);
        return Result.success("评论成功！");
    }
}
