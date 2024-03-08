package com.asg.awss3.utils;

public class FileUtils {

    public static String getFileNameFromPathString(String path){
        int folderIndex = path.lastIndexOf("/");
        String fileName = path;
        if (folderIndex != -1)
            fileName = path.substring(folderIndex + 1);
        return fileName;
    }
}
