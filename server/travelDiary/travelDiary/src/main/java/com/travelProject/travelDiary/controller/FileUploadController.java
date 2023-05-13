package com.travelProject.travelDiary.controller;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.travelProject.travelDiary.common.ByteArrayMultipartFile;
import com.travelProject.travelDiary.config.exceptionCode;
import com.travelProject.travelDiary.dto.ErrorCode;
import com.travelProject.travelDiary.dto.ResponseBody;
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
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

        if(multipartFileList != null) {
            for(MultipartFile originalFile :multipartFileList) {
                if(!originalFile.isEmpty()) {
                    Map<String, Object> insertParam = new HashMap<String, Object>();
                    InputStream inputStream = new ByteArrayInputStream(originalFile.getBytes());
                    BufferedImage originalImage = ImageIO.read(inputStream);

                    int tWidth = 0;// 생성할 썸네일이미지의 너비
                    int tHeight = 0; // 생성할 썸네일이미지의 높이

                    for(int i = 3; i < 7; i++) {
                        tWidth = (int) (originalImage.getWidth() / i);
                        tHeight = (int) (originalImage.getHeight() / i);
                        if(tWidth < 400) { break; }
                    }

                    BufferedImage resizedImage = new BufferedImage(tWidth, tHeight, BufferedImage.TYPE_3BYTE_BGR); // 썸네일이미지
                    Graphics2D graphic = resizedImage.createGraphics();
                    Image image = originalImage.getScaledInstance(tWidth, tHeight, Image.SCALE_SMOOTH);
                    if(getOrientation(originalFile) != 1) {
                        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
                        graphic.rotate(Math.toRadians(90), tWidth, tHeight);
                        graphic.drawImage(bufferedImage, null, 0, 0);
                    } else {
                        graphic.drawImage(image, 0, 0, tWidth, tHeight, null);
                    }

                    graphic.dispose(); // 리소스를 모두 해제

                    ByteArrayOutputStream os = new ByteArrayOutputStream();
                    String formatName = originalFile.getOriginalFilename().substring(originalFile.getOriginalFilename().lastIndexOf(".") + 1);
                    ImageIO.write(resizedImage, formatName, os);

                    byte[] bytes = os.toByteArray();
                    MultipartFile thumbNailImage = new ByteArrayMultipartFile(bytes, originalFile.getOriginalFilename());

                    String thumbNailUrl = awsService.upload(thumbNailImage,dirName);
                    String originalUrl = awsService.upload(originalFile,dirName);

                    Thumbnail thumbnail = new Thumbnail();
                    thumbnail.setUrl(thumbNailUrl);
                    thumbnail.setOriginalUrl(originalUrl);
                    Long thumbnailId = thumbnailService.thumbnailInsert(thumbnail);

                    planCommonService.rtbThumbNailInsert(planType, planTypeId, thumbnailId);

                    insertParam.put("thumbnailId", thumbnailId);
                    insertParam.put("url", thumbNailUrl);
                    insertParam.put("originalUrl", originalUrl);
                    imagePathList.add(insertParam);
                }
            }
        }

        if(!idArr.equals("")){
            String[] arr = idArr.split(",");
            for(String intValue :arr) {
                Long deleteId = Long.valueOf(intValue);

                Thumbnail thumbnail = new Thumbnail();
                thumbnail.setId(deleteId);

                planCommonService.rtbThumbNailDelete(planType, planTypeId, deleteId, userId);
            }
        }

        return ResponseBody.builder().code(200).msg("이미지 업로드가 완료되었습니다.").results(imagePathList).build();
    }

    public int getOrientation(MultipartFile imageMultiFile) throws MetadataException, IOException {
        int orientation = 1; // 회전정보, 1. 0도, 3. 180도, 6. 270도, 8. 90도 회전한 정보

        Metadata metadata; // 이미지 메타 데이터 객체
        Directory directory; // 이미지의 Exif 데이터를 읽기 위한 객체

        try (InputStream inputStream = imageMultiFile.getInputStream()) {
            metadata = ImageMetadataReader.readMetadata(inputStream);
            directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);

            if(directory != null){
                orientation = directory.getInt(ExifIFD0Directory.TAG_ORIENTATION); // 회전정보
            }
        } catch (Exception e) {
            orientation = 1;
        }

        return orientation;
    }
}