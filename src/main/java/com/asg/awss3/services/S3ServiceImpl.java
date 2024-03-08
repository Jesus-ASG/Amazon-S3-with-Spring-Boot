package com.asg.awss3.services;

import com.asg.awss3.output.S3FileInfo;
import com.asg.awss3.output.UploadFileOutput;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements  S3Service{

    @Value("${aws.bucketName}")
    private String bucketName;

    private final S3Client s3Client;

    @Override
    public List<S3FileInfo> listObjects(String prefix) {
        if (prefix == null)
            prefix = "";

        log.info("Getting files by prefix={}", prefix);

        ListObjectsV2Request request = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .prefix(prefix)
                .build();


        List<S3Object> result = s3Client.listObjectsV2(request).contents();
        List<S3FileInfo> fileInfoList = new ArrayList<>();
        for (S3Object object : result){
            S3FileInfo f = new S3FileInfo();
            f.setObjectKey(object.key());
            f.setSize(object.size());

            fileInfoList.add(f);
        }
        return fileInfoList;
    }

    @Override
    public UploadFileOutput uploadFile(MultipartFile file, String location) {
        if (location == null)
            location = "";

        log.info("Uploading file to location={}", location);

        if (!location.equals("") && !location.endsWith("/"))
            location += "/";

        String fileName = location + UUID.randomUUID() + "_" + file.getOriginalFilename();

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

        try {
            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return UploadFileOutput.builder()
                .objectKey(fileName)
                .build();
    }

    @Override
    public byte[] downloadFile(String objectKey) {
        log.info("Downloading file by objectKey={}", objectKey);

        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .build();

        try (
                ResponseInputStream<GetObjectResponse> responseInputStream = s3Client.getObject(request)
        ) {
            return responseInputStream.readAllBytes();
        } catch (SdkException | IOException e) {
            throw new RuntimeException("Failed to download file from S3", e);
        }

    }

    @Override
    public void deleteFile(String objectKey) {
        log.info("Deleting file by objectKey={}", objectKey);
        try {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .build();
            s3Client.deleteObject(deleteObjectRequest);
        }
        catch (S3Exception e) {
            log.info("S3 exception, {}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

}
