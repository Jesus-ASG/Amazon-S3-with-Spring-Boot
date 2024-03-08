package com.asg.awss3.output;

import lombok.Data;

@Data
public class S3FileInfo {
    private String objectKey;
    private Long size;
}
