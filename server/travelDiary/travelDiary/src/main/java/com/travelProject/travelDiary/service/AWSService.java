package com.travelProject.travelDiary.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@Slf4j
public class AWSService {

    @Autowired
    private AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3buket}")
    private String S3Bucket;

    public String upload(MultipartFile file,String dirName) throws IOException {
        String convertFilename = convertFilename(file.getOriginalFilename());

        return upload(file, dirName, convertFilename);
    }

    public String convertFilename(String filename){
        String uuid = UUID.randomUUID().toString();
        return uuid +'.'+ filename;
    }

    public String upload(MultipartFile file, String dirName, String fileName) throws IOException {
        String filePath = dirName+"/"+fileName;

        //메타 데이터 설정
        long size = file.getSize();
        ObjectMetadata objectMetaData = new ObjectMetadata();
        objectMetaData.setContentType(file.getContentType());
        objectMetaData.setContentLength(size);

        //s3 업로드
        amazonS3Client.putObject(new PutObjectRequest(S3Bucket, filePath, file.getInputStream(), null)
                .withCannedAcl(CannedAccessControlList.PublicRead));

        //url 반환
        return amazonS3Client.getUrl(S3Bucket, filePath).toString();
    }

    public Boolean delete(String filePath){
        boolean isExistObject = amazonS3Client.doesObjectExist(S3Bucket, filePath);
        if (isExistObject == true) {
            amazonS3Client.deleteObject(S3Bucket, filePath);
            return true;
        }
        else return false;
    }

//    public List<String> uploadImageToS3(MultipartFile[] multipartFileList) throws Exception {
//        List<String> imagePathList = new ArrayList<>();
//
//        for(MultipartFile multipartFile: multipartFileList){
//            String originalName = multipartFile.getOriginalFilename(); // 파일 이름
//            long size = multipartFile.getSize(); // 파일 크기
//
//            ObjectMetadata objectMetaData = new ObjectMetadata();
//            objectMetaData.setContentType(multipartFile.getContentType());
//            objectMetaData.setContentLength(size);
//
//            amazonS3Client.putObject(
//                    new PutObjectRequest(S3Bucket, originalName, multipartFile.getInputStream(), objectMetaData)
//                            .withCannedAcl(CannedAccessControlList.PublicRead)
//            );
//
//            // 접근가능한 URL 가져오기
//            String imagePath = amazonS3Client.getUrl(S3Bucket, originalName).toString();
//            imagePathList.add(imagePath);
//        }
//
//        return imagePathList;
//    }
}
