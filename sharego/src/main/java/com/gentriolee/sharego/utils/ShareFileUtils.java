package com.gentriolee.sharego.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by gentriolee
 */

public class ShareFileUtils {

    /**
     * 保存图片
     *
     * @param path
     * @param bitmap
     */
    public static boolean saveBitmap(String directory, String path, Bitmap bitmap) {
        BufferedOutputStream bos = null;
        try {
            File directoryFile = new File(directory);
            if (!directoryFile.exists()) {
                if (!directoryFile.mkdirs()) {
                    return false;
                }
            }
            File file = new File(path);
            bos = new BufferedOutputStream(new FileOutputStream(
                    file));
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
            bos.flush();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 删除其他临时图片
     * @param path
     * @param fileName
     */
    public static void deleteOtherFile(String path, String fileName) {
        File directory = new File(path);
        File[] files = directory.listFiles();
        for (File file : files) {
            if (!file.getName().equals(fileName)) {
                file.delete();
            }
        }
    }

    /**
     * 检查图片是否损坏
     *
     * @param filePath
     * @return
     */
    public static boolean checkImgDamage(String filePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(filePath, options);
        if (options.mCancel || options.outWidth == -1
                || options.outHeight == -1) {
            return true;
        }
        return false;
    }
}
