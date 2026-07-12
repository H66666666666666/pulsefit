package com.itbin.controller;

import com.itbin.pojo.Result;
import com.itbin.utils.AliyunOSSOperator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;


@Slf4j
@RestController
public class UploadController {

    //本地保存
//    @PostMapping("/upload")
//    public Result upload(MultipartFile file) throws IOException {
//        log.info("文件上传，文件名：{}", file.getOriginalFilename());
//
//        // 获取原始文件名
//        String originalFilename = file.getOriginalFilename();
//
//        // 新的文件名
//        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
//        String newFileName = UUID.randomUUID().toString() + extension;
//
//        file.transferTo(new File("D:/images/" + newFileName));
//        return Result.success();
//    }

    @Autowired
    private AliyunOSSOperator aliyunOSSOperator;
    //阿里云保存
    @PostMapping("/uploads")
    public Result upload(MultipartFile file) throws Exception {

        log.info("文件上传：{}", file.getOriginalFilename());

        //将文件交给oss储存管理
        String url = aliyunOSSOperator.upload(file.getBytes(), file.getOriginalFilename());

        return Result.success(url);
    }

}
