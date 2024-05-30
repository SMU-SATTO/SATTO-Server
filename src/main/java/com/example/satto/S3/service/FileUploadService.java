//package com.example.satto.domain.S3.service;
//
//import com.amazonaws.SdkClientException;
//import com.amazonaws.services.s3.AmazonS3;
//import com.amazonaws.services.s3.AmazonS3Client;
//import com.amazonaws.services.s3.model.CannedAccessControlList;
//import com.amazonaws.services.s3.model.ObjectMetadata;
//import com.amazonaws.services.s3.model.PutObjectRequest;
//import com.example.satto.domain.S3.FileFolder;
//import com.example.satto.global.config.S3Component;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.UUID;
//
//@RequiredArgsConstructor
//@Service
//public class FileUploadService {
//
//    private final AmazonS3Client s3Client;
//    private final AmazonS3 amazonS3;
//    private final S3Component s3Component;
//
//    public String uploadProfileImg(MultipartFile file, FileFolder fileFolder) throws IOException {
//
//        String fileName = getFileFolder(fileFolder) + generateFileName(file);
//
//        try(InputStream inputStream = file.getInputStream()) {
////            s3Client.putObject(s3Component.getBucketName(), fileName, file.getInputStream(), getObjectMetadata(file));
////            return s3Component.getDefaultUrl() + fileName;
//            amazonS3.putObject(
//                    new PutObjectRequest(s3Component.getBucketName(), fileName,inputStream, getObjectMetadata(file)).withCannedAcl(CannedAccessControlList.PublicReadWrite)
//            );
//        } catch (SdkClientException e) {
//            throw new IOException("Error uploading file to S3", e);
//        }
//        return fileName;
//    }
//
//    private ObjectMetadata getObjectMetadata(MultipartFile file) {
//        ObjectMetadata objectMetadata = new ObjectMetadata();
//        objectMetadata.setContentType(file.getContentType());
//        objectMetadata.setContentLength(file.getSize());
//        return objectMetadata;
//    }
//
//    private String generateFileName(MultipartFile file) {
//        return UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
//    }
//
//    private String getFileFolder(FileFolder fileFolder) {
//
//        String folder = "";
//        if(fileFolder == FileFolder.profile_Image) {
//            folder = s3Component.getProfileFolder();
//
////        }else if(fileFolder ==FileFolder.POST_IMAGES){
////            folder = s3Component.getPostFolder();
//        }
//        return folder;
//    }
//
//
//
//}
