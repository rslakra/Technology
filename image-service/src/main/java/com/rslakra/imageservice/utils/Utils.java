package com.rslakra.imageservice.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * Author: Rohtash Singh Lakra
 * Created: 2019-02-17 12:01
 */
public class Utils {

    /**
     * @param fileName
     * @return
     */
    public static String getExtension(String fileName) {
        return (fileName == null ? null : fileName.substring(fileName.lastIndexOf(".") + 1));
    }

    /**
     * @param file
     * @return
     */
    public static String getExtension(MultipartFile file) {
        return (file == null ? null : file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1));
    }


}
