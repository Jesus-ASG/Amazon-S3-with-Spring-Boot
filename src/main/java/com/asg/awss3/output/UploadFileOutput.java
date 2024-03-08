package com.asg.awss3.output;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UploadFileOutput {
    private String objectKey;
}
