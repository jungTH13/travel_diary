package com.travelProject.travelDiary.controller;

import com.travelProject.travelDiary.service.AWSService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import com.travelProject.travelDiary.dto.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
public class MainCotroller {

    @Autowired
    private AWSService awsService;
    @GetMapping("/")
    public String healthyCheck(){
        return "";
    }

    @PostMapping("upload")
    public ResponseBody upload(MultipartFile[] multipartFileList) throws Exception{
        List<String> imagePathList = new ArrayList<>();

        for(MultipartFile multipartFile :multipartFileList){
            String uploadFileUrl = awsService.upload(multipartFile,"Main");
            imagePathList.add(uploadFileUrl);
        }

        return ResponseBody.builder().code(200).msg("이미지 업로드가 완료되었습니다.").results(imagePathList).build();
    }
}
