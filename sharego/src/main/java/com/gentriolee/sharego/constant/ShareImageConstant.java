package com.gentriolee.sharego.constant;

import android.content.Context;
import android.os.Environment;

/**
 * Created by gentriolee
 */

public class ShareImageConstant {

    /**
     * 保存相册的存放路径
     */
    private static final String cameraPath = "/Album/";

    /**
     * 分享图片临时存放路径
     */
    private static final String sharePath = "/temp/share/";

    private static final String shareQQImgPath = sharePath + "qq/img/";

    public static final String SHARE_IMG_TEMP_ENDFIX = ".temp";

    /**
     * 分享QQ的图片路径
     * @param context
     * @return
     */
    public static String getShareQQImgPath(Context context) {
        return context.getExternalCacheDir() + shareQQImgPath;
    }

    /**
     * 相册保存路径
     * @return
     */
    public static String getCameraSavePath(Context context) {
        return Environment.getExternalStorageDirectory() + context.getPackageName() + cameraPath;
    }
}
