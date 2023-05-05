package com.travelProject.travelDiary.controller;

import com.travelProject.travelDiary.config.exceptionCode;
import com.travelProject.travelDiary.dto.ErrorCode;
import com.travelProject.travelDiary.entity.Thumbnail;
import com.travelProject.travelDiary.entity.Travel;
import com.travelProject.travelDiary.entity.User;
import com.travelProject.travelDiary.service.AWSService;
import com.travelProject.travelDiary.service.ThumbnailService;
import com.travelProject.travelDiary.service.TravelService;
import com.travelProject.travelDiary.service.plan.PlanCommonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.travelProject.travelDiary.dto.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
public class FileUploadController {

    @Autowired
    private AWSService awsService;

    @Autowired
    private TravelService travelService;

    @Autowired
    private ThumbnailService thumbnailService;

    @Autowired
    private PlanCommonService planCommonService;

    @GetMapping("/")
    public String healthyCheck(){
        return "";
    }

    @PostMapping("/upload/{travelId}")
    public ResponseBody upload(HttpServletRequest request, MultipartFile[] multipartFileList
            , @PathVariable Long travelId
            , @RequestParam("planType") String planType
            , @RequestParam("planTypeId") Long planTypeId) throws Exception {
        User user = (User) request.getAttribute("user");

        Travel travel = travelService.selectPlanTravelOne(user.getId(), travelId);
        if(travel == null){
            throw new exceptionCode(ErrorCode.INVALID_TRAVEL_ID_PARAMETER);
        }

        planCommonService.planIdSelect(user.getId(), planType, planTypeId);

        String dirName = user.getId() + "_" + travelId;
        List<String> imagePathList = new ArrayList<>();

        for(MultipartFile multipartFile :multipartFileList) {
            if(!multipartFile.isEmpty()) {
                String uploadFileUrl = awsService.upload(multipartFile,dirName);

                imagePathList.add(uploadFileUrl);

                Thumbnail thumbnail = new Thumbnail();
                thumbnail.setUrl(uploadFileUrl);
                Long thumbnailId = thumbnailService.thumbnailInsert(thumbnail);

                planCommonService.rtbThumbNailInsert(planType, planTypeId, thumbnailId);
            }
        }

        return ResponseBody.builder().code(200).msg("이미지 업로드가 완료되었습니다.").results(imagePathList).build();
    }
}
