package com.example.alddeul_babsang.apiPayload.review.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.alddeul_babsang.apiPayload.code.status.ErrorStatus;
import com.example.alddeul_babsang.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class S3Service {

    private final AmazonS3 amazonS3;
    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucketName;


    public String uploadFile(MultipartFile file, String folderName) throws IOException {

        try {
            String fileName = createFileName(file.getOriginalFilename());
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(file.getContentType());
            amazonS3.putObject(new PutObjectRequest(bucketName, fileName, file.getInputStream(),objectMetadata));
            return amazonS3.getUrl(bucketName, fileName).toString();
        } catch (IOException e) {
            throw new GeneralException(ErrorStatus._FILE_UPLOAD_ERROR);
        } catch (Exception e) {
            // 다른 예외가 발생하면 일반적인 처리
            throw new GeneralException(ErrorStatus._INTERNAL_SERVER_ERROR);
        }
    }
    private String createFileName(String originalFileName) throws IllegalAccessException {
        return UUID.randomUUID().toString().concat(getFileExtension(originalFileName));
    }

    private String getFileExtension(String fileName) {
            return fileName.substring(fileName.lastIndexOf("."));
    }

}
