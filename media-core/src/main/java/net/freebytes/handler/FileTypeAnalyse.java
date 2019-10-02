package net.freebytes.handler;

import java.io.File;

/**
 * Created by 千里明月 on 2019/1/23.
 */
public class FileTypeAnalyse {

    public static final String UNDIFINE_TYPE="common";

    public static String getSuffix(File file) {
        //文件分类识别
        String filename = file.getName();
        return getSuffix(filename);
    }

    public static String getSuffix(String fileName) {
        //文件分类识别
        return fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
    }

}
