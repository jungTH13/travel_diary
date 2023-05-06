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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            , @RequestParam("planTypeId") Long planTypeId
            , @RequestParam("idArr") String idArr) throws Exception {
        User user = (User) request.getAttribute("user");
        String userId = user.getId();

        Travel travel = travelService.selectPlanTravelOne(user.getId(), travelId);
        if(travel == null){
            throw new exceptionCode(ErrorCode.INVALID_TRAVEL_ID_PARAMETER);
        }

        planCommonService.planIdSelect(userId, planType, planTypeId);

        String dirName = userId + "_" + travelId;
        List<Map<String, Object>> imagePathList = new ArrayList<Map<String, Object>>();

        for(MultipartFile multipartFile :multipartFileList) {
            if(!multipartFile.isEmpty()) {
                Map<String, Object> insertParam = new HashMap<String, Object>();
                String uploadFileUrl = awsService.upload(multipartFile,dirName);

                Thumbnail thumbnail = new Thumbnail();
                thumbnail.setUrl(uploadFileUrl);
                Long thumbnailId = thumbnailService.thumbnailInsert(thumbnail);

                planCommonService.rtbThumbNailInsert(planType, planTypeId, thumbnailId);

                insertParam.put("thumbnailId", thumbnailId);
                insertParam.put("url", uploadFileUrl);
                imagePathList.add(insertParam);
            }
        }

        if(!idArr.equals("")){
            String[] arr = idArr.split(",");
            for(String intValue :arr) {
                Long deleteId = Long.valueOf(intValue);

                Thumbnail thumbnail = new Thumbnail();
                thumbnail.setId(deleteId);

                planCommonService.rtbThumbNailDelete(planType, planTypeId, deleteId, userId);
                //배치처리로 따로 삭제예정이라서 주석처리
                //thumbnailService.thumbnailDelete(thumbnail, userId);
            }
        }
        return ResponseBody.builder().code(200).msg("이미지 업로드가 완료되었습니다.").results(imagePathList).build();
    }
}
