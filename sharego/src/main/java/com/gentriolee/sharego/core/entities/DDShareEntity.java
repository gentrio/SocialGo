package com.gentriolee.sharego.core.entities;

import android.graphics.Bitmap;
import android.os.Bundle;

import com.gentriolee.socialgo.annotation.ParamsRequired;

/**
 * Created by gentriolee
 */

public class DDShareEntity extends ShareEntity {

    public static final String KEY_DD_TYPE = "key_dd_type";

    public static final int TYPE_TEXT = 0;
    public static final int TYPE_IMG = 1;
    public static final int TYPE_WEB = 2;

    public static final String KEY_DD_TITLE = "key_dd_title";
    public static final String KEY_DD_SUMMARY = "key_dd_summary";
    public static final String KEY_DD_TEXT = "key_dd_text";
    public static final String KEY_DD_IMG_BITMAP = "key_dd_img_bitmap";
    public static final String KEY_DD_WEB_URL = "key_dd_web_url";

    private DDShareEntity(int type) {
        super(type);
    }

    /**
     * 分享文字
     * @param text 分享文本内容
     */
    public static ShareEntity createTextInfo(@ParamsRequired String text) {
        ShareEntity entity = new ShareEntity(TYPE_DD);
        addParams(entity.params, KEY_DD_TYPE, TYPE_TEXT);
        addParams(entity.params, KEY_DD_TEXT, text);
        return entity;
    }

    /**
     * 分享图片
     *
     * @param imgBitmap     本地图片地址
     */
    public static ShareEntity createImageInfo(@ParamsRequired Bitmap imgBitmap) {
        ShareEntity entity = new ShareEntity(TYPE_DD);
        addParams(entity.params, KEY_DD_TYPE, TYPE_IMG);
        addParams(entity.params, KEY_DD_IMG_BITMAP, imgBitmap);
        return entity;
    }

    /**
     * 分享网页
     *
     * @param webUrl  网页链接
     * @param title   网页标题
     * @param summary 网页摘要
     * @param imgBitmap     网页左边图标，本地路径
     * @param text    文本内容
     */
    public static ShareEntity createWebInfo(@ParamsRequired String webUrl, String title, String summary, Bitmap imgBitmap, String text) {
        ShareEntity entity = new ShareEntity(TYPE_WB);
        addParams(entity.params, KEY_DD_TYPE, TYPE_WEB);
        addParams(entity.params, KEY_DD_WEB_URL, webUrl);
        addParams(entity.params, KEY_DD_TEXT, text);
        addTitleSummaryAndThumb(entity.params, title, summary, imgBitmap);
        return entity;
    }

    /**
     * @param title   标题
     * @param summary 摘要
     * @param imgBitmap  本地图片地址
     */
    private static void addTitleSummaryAndThumb(Bundle params, String title, String summary, Bitmap imgBitmap) {
        addParams(params, KEY_DD_TITLE, title);
        addParams(params, KEY_DD_SUMMARY, summary);
        addParams(params, KEY_DD_IMG_BITMAP, imgBitmap);
    }
}

