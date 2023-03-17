package com.travelProject.travelDiary.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Configuration
public class AWSConfig {
    @Value("${cloud.aws.iamAccessKey}")
    private String iamAccessKey = "IAM 생성할 때 확인했던 AccessKey 입력"; // IAM Access Key
    @Value("${cloud.aws.iamSecretKey}")
    private String iamSecretKey = "IAM 생성할 때 확인했던 SecretKey 입력"; // IAM Secret Key
    private String region = "ap-northeast-2"; // Bucket Region


    @Bean
    public AmazonS3Client amazonS3Client() {
        BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(iamAccessKey, iamSecretKey);
        return (AmazonS3Client) AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
                .build();
    }

}
