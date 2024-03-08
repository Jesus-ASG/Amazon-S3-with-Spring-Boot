package com.asg.awss3.controllers;

import com.asg.awss3.input.UploadFileInput;
import com.asg.awss3.output.S3FileInfo;
import com.asg.awss3.output.UploadFileOutput;
import com.asg.awss3.services.S3Service;
import com.asg.awss3.utils.FileUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "api/v1/s3")
@RequiredArgsConstructor
@Tag(name = "S3 controller")
public class S3Controller {
    private final S3Service s3Service;


    @Operation(summary = "List files")
    @GetMapping("/list")
    public ResponseEntity<List<S3FileInfo>> listFiles(
            @RequestParam(required = false) String prefix
    ) {
        return ResponseEntity.ok(s3Service.listObjects(prefix));
    }


    @Operation(summary = "Upload a file")
    @PostMapping("/upload")
    public ResponseEntity<UploadFileOutput> uploadFile (
            @RequestPart("file") MultipartFile file, @RequestParam(required = false) String location
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                s3Service.uploadFile(file, location)
        );
    }


    @Operation(summary = "Download a file")
    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadFile (
            @RequestParam String objectKey
    ) {
        HttpHeaders headers = new HttpHeaders();
        String fileName = "\"" + FileUtils.getFileNameFromPathString(objectKey) + "\"";

        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
        headers.add(HttpHeaders.CONTENT_TYPE, "application/octet-stream");
        return new ResponseEntity<>(s3Service.downloadFile(objectKey), headers, HttpStatus.OK);
    }


    @Operation(summary = "Delete a file")
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteFile(
            @RequestParam String objectKey
    ) {
        s3Service.deleteFile(objectKey);
        return ResponseEntity.noContent().build();
    }

}
