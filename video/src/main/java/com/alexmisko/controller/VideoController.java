package com.alexmisko.controller;

import com.alexmisko.config.ConditionException;
import com.alexmisko.feign.UserInfoFeign;
import com.alexmisko.filter.AccessContext;
import com.alexmisko.pojo.Video;
import com.alexmisko.service.VideoService;
import com.alexmisko.util.FastDFSClientUtil;
import com.alexmisko.vo.LoginUserInfo;
import com.alexmisko.vo.Result;
import com.alexmisko.vo.UserInfo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
public class VideoController {

    @Autowired
    VideoService videoService;

    @Autowired
    UserInfoFeign userInfoFeign;

    @Autowired
    private FastDFSClientUtil fastDFSClientUtil;

    /**
     * 查询一条视频
     */
    @GetMapping("video/admin/{id}")
    public Result<Video> oneVideo(@PathVariable Long id){
        LoginUserInfo loginUserInfo = AccessContext.getLoginUserInfo();
        log.info("userId and username and role: [{}] [{}] [{}]", loginUserInfo.getId(), loginUserInfo.getUsername(), loginUserInfo.getRole());
        Video video = videoService.getById(id);
        Result<UserInfo> result = userInfoFeign.getUserInfo(id);
        video.setUserInfo(result.getData());
        return Result.success(video);
    }

    /**
     * 分页查询视频
     */
    @GetMapping("video/user")
    public Result<IPage<Video>> pageVideo(Long page, Long num){
        QueryWrapper<Video> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");
        IPage<Video> iPage = new Page<>(page, num);
        IPage<Video> iPageVideo = videoService.page(iPage, queryWrapper);
        List<Video> videoList =  iPageVideo.getRecords();
        for(Video video : videoList){
            // 先遍历取id，再请求userInfo
            Result<UserInfo> result = userInfoFeign.getUserInfo(video.getUserId());
            video.setUserInfo(result.getData());
        }
        // 最后重新设置值
        iPageVideo.setRecords(videoList);
        return Result.success(iPageVideo);
    }

    /**
     * 直接上传视频
     */
    @PostMapping("video/user")
    public Result<String> uploadVideo(@RequestParam("img") MultipartFile file){
        String groupPath = null;
        try {
            groupPath = fastDFSClientUtil.uploadFile(file);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ConditionException("上传失败！");
        }
        return new Result<>("200", "上传成功！", groupPath);
    }

    /**
     * 分片上传视频
     */
    @PostMapping(value = "video/chunk/user")
    public Result<String> uploadVideoChunk(@RequestParam("file") MultipartFile file, String fileMD5, Integer sliceNo, Integer totalSliceNo) throws Exception{
        log.info("fileMD5 and sliceNo and totalSliceNo: [{}], [{}], [{}]", fileMD5, sliceNo, totalSliceNo);
        String groupPath = null;
        try {
            groupPath = fastDFSClientUtil.uploadFileBySlices(file, fileMD5, sliceNo, totalSliceNo);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ConditionException("上传失败！");
        }
        return Result.success("上传成功！", groupPath);
    }

    /*
     * 动态发布
     */
    @PostMapping("/flow")
    public Result<String> postFlow(Video video){
        return Result.success("发布动态成功！");
    }
}
