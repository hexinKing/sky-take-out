package com.hexin.contorller;

import com.hexin.pojo.Result;
import com.hexin.utils.AliOSSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class UploadContorller {


    @Autowired
    private AliOSSUtils aliOSSUtils;

    //    图片文件上传
    @PostMapping("/upload")
    public Result upload(MultipartFile imgge) throws IOException {
        String upload = aliOSSUtils.upload(imgge);
        return Result.success(upload);
    }


}
