package com.gentriolee.sharego.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;

import com.gentriolee.sharego.constant.ShareImageConstant;

import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * Created by gentriolee
 */

public class ShareUtils {

    /**
     * 复制链接
     *
     * @param link
     */
    public static boolean copyLink(Context context, String link) {
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("", link);
        if (cm != null) {
            cm.setPrimaryClip(clipData);
            return true;
        }
        return false;
    }

    /**
     * 保存到相册
     */
    public static boolean saveAlbum(Context context, Bitmap imgBitmap) {
        String file = saveToFile(context, imgBitmap);
        return file != null;
    }

    public static String saveToFile(Context context, Bitmap imgBitmap) {
        String cameraPath = ShareImageConstant.getCameraSavePath(context);
        File cameraFile = new File(cameraPath);
        if (!cameraFile.exists()) {
            cameraFile.mkdirs();
        }
        String savePath = cameraPath + System.currentTimeMillis() + ".png";
        if (ShareFileUtils.saveBitmap(cameraPath, savePath, imgBitmap)) {
            File file = new File(savePath);
            //保存图片后发送广播通知更新数据库
            Uri uri = Uri.fromFile(file);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            return file.getAbsolutePath();
        }
        return null;
    }

    public static String buildTransaction(String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    public static byte[] smallBmpToByteArray(Bitmap bmp) {
        return bmpToByteArray(getBitmap(bmp, 70, 70), 32 * 1024);
    }

    public static byte[] bigBmpToByteArray(Bitmap bmp) {
        return bmpToByteArray(getBitmap(bmp, 300, 240), 128 * 1024);
    }

    private static byte[] bmpToByteArray(final Bitmap bmp, int maxkb) {
        Bitmap resultBmp = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(resultBmp);
        canvas.drawColor(Color.WHITE);
        Paint paint = new Paint();
        canvas.drawBitmap(bmp, 0, 0, paint);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        int options = 100;
        do {
            output.reset();//清空output
            resultBmp.compress(Bitmap.CompressFormat.PNG, options, output);//这里压缩options%，把压缩后的数据存放到output中
            options -= 10;
        } while (output.toByteArray().length > maxkb && options != 0);

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    private static Bitmap getBitmap(Bitmap bitmap, int width, int height) {
        //获取图片信息
        Bitmap bmp;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        byte[] bytes = getBytesByBitmap(bitmap);
        BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);

        opts.inSampleSize = computeSampleSize(opts, -1, width * height);
        opts.inJustDecodeBounds = false;
        bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);
        return bmp;
    }

    private static byte[] getBytesByBitmap(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(bitmap.getByteCount());
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        return outputStream.toByteArray();
    }

    private static int computeSampleSize(BitmapFactory.Options options,
                                         int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength,
                maxNumOfPixels);

        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }

        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options,
                                                int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;

        int lowerBound = (maxNumOfPixels == -1) ? 1 :
                (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 :
                (int) Math.min(Math.floor(w / minSideLength),
                        Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) &&
                (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }
}
