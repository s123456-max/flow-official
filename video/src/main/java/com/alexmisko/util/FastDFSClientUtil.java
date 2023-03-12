package com.alexmisko.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.alexmisko.config.ConditionException;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.AppendFileStorageClient;
import com.github.tobato.fastdfs.service.FastFileStorageClient;

import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;

import com.github.tobato.fastdfs.domain.fdfs.MetaData;

@Slf4j
@Component
public class FastDFSClientUtil{

    private static final String DEFAULT_GROUP = "group1";

    private static final String PATH_KEY = "path-key:";

    private static final String UPLOADED_SIZE_KEY = "uploaded-size-key:";

    private static final String UPLOADED_NO_KEY = "uploaded-no-key:";
    
    @Value("${fdfs.web-server-url}")
    private String webServerUrl;

    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    @Autowired
    private AppendFileStorageClient appendFileStorageClient;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public String getFileType(MultipartFile file){
        if(file == null){
            throw new ConditionException("非法文件！");
        }
        String fileName = file.getOriginalFilename();
        // 获取最后一个分隔符
        int index = fileName.lastIndexOf(".");
        return fileName.substring(index+1);
    }

    // 上传
    public String updateCommonFile(MultipartFile file) throws Exception{
        Set<MetaData> metaDataSet = new HashSet<>();
        String fileType = getFileType(file);
        StorePath storePath = fastFileStorageClient.uploadFile(file.getInputStream(), file.getSize(), fileType, metaDataSet);
        return storePath.getPath();
    }

    // 删除
    public void deleteFile(String filePath){
        fastFileStorageClient.deleteFile(filePath);
    }

    // 上传可以断点续传的文件
    public String uploadAppenderFile(MultipartFile file) throws Exception{
        String fileName = file.getOriginalFilename();
        String fileType = getFileType(file);
        StorePath storePath = appendFileStorageClient.uploadAppenderFile(DEFAULT_GROUP, file.getInputStream(), file.getSize(), fileType);
        return storePath.getPath();
    }

    public void modifyAppenderFile(MultipartFile file, String filePath, long offset) throws Exception{
        appendFileStorageClient.modifyFile(DEFAULT_GROUP, filePath, file.getInputStream(), file.getSize(), offset);
    }

    public String uploadFileBySlices(MultipartFile file, String fileMD5, Integer sliceNo, Integer totalSliceNo) throws Exception{
        if(file == null || sliceNo == null || totalSliceNo == null){
            throw new ConditionException("参数异常！");
        }
        String pathKey = PATH_KEY + fileMD5;
        String uploadedSizeKey = UPLOADED_SIZE_KEY + fileMD5;
        String uploadedNoKey = UPLOADED_NO_KEY + fileMD5;
        String uploadedSizeStr = redisTemplate.opsForValue().get(uploadedSizeKey);
        Long uploadedSize = 0L;
        // 如果当前已上传的文件不为空，则从redis查找赋值
        if(!StringUtil.isNullOrEmpty(uploadedSizeStr)){
            uploadedSize = Long.valueOf(uploadedSizeStr);
        }
        String fileType = getFileType(file);
        log.info("文件类型: [{}]", fileType);
        // 第一片需要先拿文件路径
        if(sliceNo == 1){
            String path = uploadAppenderFile(file);
            log.info("文件路径: [{}]", path);
            if(StringUtil.isNullOrEmpty(path)){
                throw new ConditionException("path不存在，上传失败！");
            }
            // 向redis存储文件路径
            redisTemplate.opsForValue().set(pathKey, path);
            // 修改上传文件个数
            redisTemplate.opsForValue().set(uploadedNoKey, "1");
        }else{
            String filePath = redisTemplate.opsForValue().get(pathKey);
            log.info("文件路径: [{}]", filePath);
            if(StringUtil.isNullOrEmpty(filePath)){
                throw new ConditionException("filePath不存在，上传失败！");
            }
            redisTemplate.opsForValue().get(uploadedSizeKey);
            modifyAppenderFile(file, filePath, uploadedSize);
            redisTemplate.opsForValue().increment(uploadedNoKey);
        }
        // 修改上传文件的大小
        uploadedSize += file.getSize();
        redisTemplate.opsForValue().set(uploadedSizeKey, String.valueOf(uploadedSize));
        // 判断是否可以结束
        String uploadedNoStr = redisTemplate.opsForValue().get(uploadedNoKey);
        Integer uploadedNo = Integer.valueOf(uploadedNoStr);
        String resultPath = "";
        if(uploadedNo.equals(totalSliceNo)){
            resultPath = webServerUrl + "/" + DEFAULT_GROUP + "/" + redisTemplate.opsForValue().get(pathKey);
            // 清空所有键值
            List<String> keyList = Arrays.asList(uploadedNoKey, pathKey, uploadedSizeKey);
            redisTemplate.delete(keyList);
        }
        return resultPath;
    }


    public String uploadFile(MultipartFile file) throws IOException{
        StorePath path = fastFileStorageClient.uploadFile((InputStream) file.getInputStream(),file.getSize(), FilenameUtils.getExtension(file.getOriginalFilename()),null);
        return webServerUrl + "/" + path.getFullPath();
    }
}