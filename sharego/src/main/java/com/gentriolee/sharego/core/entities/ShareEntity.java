package com.gentriolee.sharego.core.entities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;

import com.gentriolee.sharego.constant.ShareImageConstant;
import com.gentriolee.sharego.utils.ShareFileUtils;
import com.gentriolee.socialgo.annotation.ParamsRequired;
import com.gentriolee.socialgo.config.SocialConfig;
import com.gentriolee.socialgo.core.ISocial;

/**
 * Created by gentriolee
 */

public class ShareEntity {

    public static final int SHARE_TYPE_TEXT = 1;
    public static final int SHARE_TYPE_IMG = 2;
    public static final int SHARE_TYPE_WEB = 3;
    public static final int SHARE_TYPE_MINI_APP = 4;

    public static final String SHARE_TYPE = "shareType";
    public static final String SHARE_TITLE = "title";
    public static final String SHARE_IMAGE_BITMAP = "imageBitmap";
    public static final String SHARE_LINK = "link";
    public static final String SHARE_DESC = "desc";

    private int target;
    Bundle params;

    public ShareEntity(int target) {
        this.target = target;
        this.params = new Bundle();
    }

    /**
     * 分享文字
     *
     * @param text 分享文本内容
     */
    public static ShareEntity createTextInfo(@ParamsRequired int shareType, @ParamsRequired String text) {
        ShareEntity entity = new ShareEntity(shareType);
        addParams(entity.params, SHARE_TYPE, SHARE_TYPE_TEXT);
        addParams(entity.params, SHARE_TITLE, text);
        return entity;
    }

    /**
     * 分享图片
     *
     * @param imgBitmap     本地图片地址
     */
    public static ShareEntity createImageInfo(@ParamsRequired int shareType, @ParamsRequired Bitmap imgBitmap) {
        ShareEntity entity = new ShareEntity(shareType);
        addParams(entity.params, SHARE_TYPE, SHARE_TYPE_IMG);
        if (shareType == ISocial.TARGET_QQ || shareType == ISocial.TARGET_QQ_ZONE) {
            addParams(entity.params, SHARE_IMAGE_BITMAP, saveImgBitmap(imgBitmap));
        } else {
            addParams(entity.params, SHARE_IMAGE_BITMAP, imgBitmap);
        }
        return entity;
    }

    /**
     * 分享网页
     *
     * @param webUrl    网页链接
     * @param title     网页标题
     * @param summary   网页摘要
     * @param imgBitmap 网页左边图标，本地路径
     */
    public static ShareEntity createWebInfo(@ParamsRequired int shareType, @ParamsRequired String title, @ParamsRequired String summary,
                                            @ParamsRequired String webUrl, @ParamsRequired Bitmap imgBitmap) {
        ShareEntity entity = new ShareEntity(shareType);
        addParams(entity.params, SHARE_TYPE, SHARE_TYPE_WEB);
        addParams(entity.params, SHARE_LINK, webUrl);
        addParams(entity.params, SHARE_TITLE, title);
        addParams(entity.params, SHARE_DESC, summary);
        if (shareType == ISocial.TARGET_QQ || shareType == ISocial.TARGET_QQ_ZONE) {
            addParams(entity.params, SHARE_IMAGE_BITMAP, saveImgBitmap(imgBitmap));
        } else {
            addParams(entity.params, SHARE_IMAGE_BITMAP, imgBitmap);
        }
        return entity;
    }

    public Bundle getParams() {
        return params;
    }

    public void setParams(Bundle params) {
        this.params = params;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    protected static void addParams(Bundle params, String key, String value) {
        if (params == null || TextUtils.isEmpty(key) || TextUtils.isEmpty(value)) {
            return;
        }
        params.putString(key, value);
    }

    protected static void addParams(Bundle params, String key, Bitmap value) {
        if (params == null || TextUtils.isEmpty(key) || value == null) {
            return;
        }
        params.putParcelable(key, value);
    }

    protected static void addParams(Bundle params, String key, int value) {
        if (params == null || TextUtils.isEmpty(key)) {
            return;
        }
        params.putInt(key, value);
    }

    protected static void addParams(Bundle params, String key, boolean value) {
        if (params == null || TextUtils.isEmpty(key)) {
            return;
        }
        params.putBoolean(key, value);
    }


    /**
     * 因为QQ分享的图片需要为本地地址所以将bitmap存到本地 返回本地地址
     * @param bitmap
     * @return
     */
    private static String saveImgBitmap(Bitmap bitmap) {
        final String directoryPath = ShareImageConstant.getShareQQImgPath(SocialConfig.getInstance().getContext());
        final String imgFileName = System.currentTimeMillis() + ShareImageConstant.SHARE_IMG_TEMP_ENDFIX;
        final String imgFilePath = directoryPath + imgFileName;
        ShareFileUtils.saveBitmap(directoryPath, imgFilePath, bitmap);
        ShareFileUtils.deleteOtherFile(directoryPath, imgFileName);
        return imgFilePath;
    }
}

