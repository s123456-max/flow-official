package com.alexmisko.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;


@Component
public class FastDFSClientUtil{
    
    @Value("${fdfs.web-server-url}")
    private String webServerUrl;

    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    public String uploadFile(MultipartFile file) throws IOException{
        StorePath path = fastFileStorageClient.uploadFile((InputStream) file.getInputStream(),file.getSize(), FilenameUtils.getExtension(file.getOriginalFilename()),null);
        return webServerUrl + "/" + path.getFullPath();
    }
}