package com.gentriolee.sharego.core.entities;

import android.graphics.Bitmap;
import android.os.Bundle;

import com.gentriolee.socialgo.annotation.ParamsRequired;

import java.util.ArrayList;

/**
 * Created by gentriolee
 */

public class WBShareEntity extends ShareEntity {

    public static final String KEY_WB_TYPE = "key_wb_type";
    /**
     * 依次为：文本，图片，音乐，视频，网页
     */
    public static final int TYPE_TEXT = 0;
    public static final int TYPE_IMG_TEXT = 1;
    public static final int TYPE_MULTI_IMAGES = 2;
    public static final int TYPE_VIDEO = 3;
    public static final int TYPE_WEB = 4;

    public static final String KEY_WB_TITLE = "key_wb_title";
    public static final String KEY_WB_SUMMARY = "key_wb_summary";
    public static final String KEY_WB_TEXT = "key_wb_text";
    public static final String KEY_WB_IMG_BITMAP = "key_wb_img_bitmap";
    public static final String KEY_WB_MULTI_IMG = "key_wb_multi_img";
    public static final String KEY_WB_VIDEO_URL = "key_wb_video_url";
    public static final String KEY_WB_WEB_URL = "key_wb_web_url";

    private WBShareEntity(int type) {
        super(type);
    }

    /**
     * @param text 分享文本内容
     */
    public static ShareEntity createTextInfo(@ParamsRequired String text) {
        ShareEntity entity = new ShareEntity(TYPE_WB);
        addParams(entity.params, KEY_WB_TYPE, TYPE_TEXT);
        addParams(entity.params, KEY_WB_TEXT, text);
        return entity;
    }

    /**
     * 分享图文
     *
     * @param imgBitmap  本地图片
     * @param text 文本内容
     */
    public static ShareEntity createImageTextInfo(@ParamsRequired Bitmap imgBitmap, String text) {
        ShareEntity entity = new ShareEntity(TYPE_WB);
        addParams(entity.params, KEY_WB_TYPE, TYPE_IMG_TEXT);
        addParams(entity.params, KEY_WB_TEXT, text);
        addTitleSummaryAndThumb(entity.params, "", "", imgBitmap);
        return entity;
    }

    /**
     * 分享多图
     *
     * @param images 图片List，最多9张
     * @param text   文本内容
     */
    public static ShareEntity createMultiImageInfo(@ParamsRequired ArrayList<String> images, String text) {
        ShareEntity entity = new ShareEntity(TYPE_WB);
        addParams(entity.params, KEY_WB_TYPE, TYPE_MULTI_IMAGES);
        addParams(entity.params, KEY_WB_MULTI_IMG, images);
        addParams(entity.params, KEY_WB_TEXT, text);
        return entity;
    }

    /**
     * 分享视频
     *
     * @param videoUrl   视频路径，本地视频
     * @param imgBitmap 视频封面
     * @param text       文本内容
     */
    public static ShareEntity createVideoInfo(@ParamsRequired String videoUrl, Bitmap imgBitmap, String text) {
        ShareEntity entity = new ShareEntity(TYPE_WB);
        addParams(entity.params, KEY_WB_TYPE, TYPE_VIDEO);
        addParams(entity.params, KEY_WB_VIDEO_URL, videoUrl);
        addParams(entity.params, KEY_WB_TEXT, text);
        addTitleSummaryAndThumb(entity.params, "", "", imgBitmap);
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
        addParams(entity.params, KEY_WB_TYPE, TYPE_WEB);
        addParams(entity.params, KEY_WB_WEB_URL, webUrl);
        addParams(entity.params, KEY_WB_TEXT, text);
        addTitleSummaryAndThumb(entity.params, title, summary, imgBitmap);
        return entity;
    }

    /**
     * @param title   标题
     * @param summary 摘要
     * @param imgBitmap  本地图片地址
     */
    private static void addTitleSummaryAndThumb(Bundle params, String title, String summary, Bitmap imgBitmap) {
        addParams(params, KEY_WB_TITLE, title);
        addParams(params, KEY_WB_SUMMARY, summary);
        addParams(params, KEY_WB_IMG_BITMAP, imgBitmap);
    }
}
