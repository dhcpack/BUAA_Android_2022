package com.example.hang.ui.mine.utils.function;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import com.example.hang.RegisterActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Bitmap 帮助类
 */
public class BitMapUtils {
    /**
     * Save Bitmap
     *
     * @param name file name
     * @param bitmap  picture to save
     */

    public static Boolean saveBitmap(String name, Bitmap bitmap, Context mContext) {
        Log.d("Save Bitmap", "Ready to save picture");
        //指定我们想要存储文件的地址
        String TargetPath = mContext.getFilesDir() + "/images/";
        Log.d("Save Bitmap", "Save Path=" + TargetPath);
        //判断指定文件夹的路径是否存在
        if (!FileUtils.fileIsExist(TargetPath)) {
            Log.d("Save Bitmap", "TargetPath isn't exist");
            return false;
        } else {
            //如果指定文件夹创建成功，那么我们则需要进行图片存储操作
            File saveFile = new File(TargetPath, name);
            try {
                FileOutputStream saveImgOut = new FileOutputStream(saveFile);
                // compress - 压缩的意思
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, saveImgOut);
                //存储完成后需要清除相关的进程
                saveImgOut.flush();
                saveImgOut.close();
                Log.d("Save Bitmap", "The picture is save to your phone!");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return true;
        }
    }
}
