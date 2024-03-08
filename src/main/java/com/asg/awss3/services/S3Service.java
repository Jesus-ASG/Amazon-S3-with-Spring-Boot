package com.asg.awss3.services;

import com.asg.awss3.output.S3FileInfo;
import com.asg.awss3.output.UploadFileOutput;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface S3Service {

    List<S3FileInfo> listObjects(String prefix);

    UploadFileOutput uploadFile(MultipartFile file, String location);

    byte[] downloadFile(String objectKey);

    void deleteFile(String objectKey);
}
