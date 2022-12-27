package com.example.hang.ui.mine.utils.function;

import java.io.File;

/**
 * File 帮助类
 */
public class FileUtils {
    /**
     * 判断指定目录的文件夹是否存在，如果不存在则需要创建新的文件夹
     * @param fileName 指定目录
     * @return 返回创建结果 TRUE or FALSE
     */
    public static boolean fileIsExist(String fileName) {
        //传入指定的路径，然后判断路径是否存在
        File file = new File(fileName);
        if (file.exists())
            return true;
        else{
            //file.mkdirs() 创建文件夹的意思
            return file.mkdirs();
        }
    }
}
