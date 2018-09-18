package com.gentriolee.sharego.core.entities;

import android.graphics.Bitmap;
import android.os.Bundle;

import com.gentriolee.socialgo.annotation.ParamsRequired;

import static com.gentriolee.socialgo.core.SocialType.TYPE_WX;
import static com.gentriolee.socialgo.core.SocialType.TYPE_WX_TIMELINE;

/**
 * Created by gentriolee
 */

public class WXShareEntity extends ShareEntity {

    public static final String KEY_WX_TYPE = "key_wx_type";
    /**
     * 依次为：文本，图片，音乐，视频，网页
     */
    public static final int TYPE_TEXT = 0;
    public static final int TYPE_IMG = 1;
    public static final int TYPE_MUSIC = 2;
    public static final int TYPE_VIDEO = 3;
    public static final int TYPE_WEB = 4;
    public static final int TYPE_MINI_APP = 5;

    public static final String KEY_WX_TITLE = "key_wx_title";
    public static final String KEY_WX_SUMMARY = "key_wx_summary";
    public static final String KEY_WX_TEXT = "key_wx_text";
    public static final String KEY_WX_IMG_BITMAP = "key_wx_img_bitmap";
    public static final String KEY_WX_MUSIC_URL = "key_wx_music_url";
    public static final String KEY_WX_VIDEO_URL = "key_wx_video_url";
    public static final String KEY_WX_WEB_URL = "key_wx_web_url";
    public static final String KEY_WX_MINI_PROGRAM_URL = "key_wx_mini_program_url";
    public static final String KEY_WX_MINI_PROGRAM_ID = "key_wx_mini_program_id";
    public static final String KEY_WX_MINI_PROGRAM_TYPE = "key_wx_mini_program_type";

    public WXShareEntity(int type) {
        super(type);
    }

    /**
     * 分享文本
     *
     * @param isTimeLine 是否分享到朋友圈，false为微信好友列表，true为朋友圈
     * @param text       文本
     */
    public static ShareEntity createTextInfo(@ParamsRequired boolean isTimeLine, @ParamsRequired String text) {
        ShareEntity entity = new ShareEntity(isTimeLine ? TYPE_WX_TIMELINE : TYPE_WX);
        addParams(entity.params, KEY_WX_TYPE, TYPE_TEXT);
        addParams(entity.params, KEY_WX_TEXT, text);
        return entity;
    }

    /**
     * 分享图片
     *
     * @param isTimeLine 是否分享到朋友圈，false为微信好友列表，true为朋友圈
     * @param imgBitmap     图片Bitmap
     */
    public static ShareEntity createImageInfo(@ParamsRequired boolean isTimeLine, @ParamsRequired Bitmap imgBitmap) {
        ShareEntity entity = new ShareEntity(isTimeLine ? TYPE_WX_TIMELINE : TYPE_WX);
        addParams(entity.params, KEY_WX_TYPE, TYPE_IMG);
        addParams(entity.params, KEY_WX_IMG_BITMAP, imgBitmap);
        return entity;
    }

    /**
     * 分享音乐
     *
     * @param isTimeLine 是否分享到朋友圈，false为微信好友列表，true为朋友圈
     * @param musicUrl   音乐url，不支持本地音乐
     * @param imgBitmap     本地图片地址，缩略图大小
     * @param title      音乐标题
     * @param summary    音乐摘要
     */
    public static ShareEntity createMusicInfo(@ParamsRequired boolean isTimeLine, @ParamsRequired String musicUrl, Bitmap imgBitmap, String title, String summary) {
        ShareEntity entity = new ShareEntity(isTimeLine ? TYPE_WX_TIMELINE : TYPE_WX);
        addParams(entity.params, KEY_WX_TYPE, TYPE_MUSIC);
        addParams(entity.params, KEY_WX_MUSIC_URL, musicUrl);
        addTitleSummaryAndThumb(entity.params, title, summary, imgBitmap);
        return entity;
    }

    /**
     * 分享视频
     *
     * @param isTimeLine 是否分享到朋友圈，false为微信好友列表，true为朋友圈
     * @param videoUrl   视频url，不支持本地音乐
     * @param imgBitmap     本地图片地址，缩略图大小
     * @param title      视频标题
     * @param summary    视频摘要
     */
    public static ShareEntity createVideoInfo(@ParamsRequired boolean isTimeLine, @ParamsRequired String videoUrl, Bitmap imgBitmap, String title, String summary) {
        ShareEntity entity = new ShareEntity(isTimeLine ? TYPE_WX_TIMELINE : TYPE_WX);
        addParams(entity.params, KEY_WX_TYPE, TYPE_VIDEO);
        addParams(entity.params, KEY_WX_VIDEO_URL, videoUrl);
        addTitleSummaryAndThumb(entity.params, title, summary, imgBitmap);
        return entity;
    }

    /**
     * 分享网页
     *
     * @param isTimeLine 是否分享到朋友圈，false为微信好友列表，true为朋友圈
     * @param webUrl     视频url，不支持本地音乐
     * @param imgBitmap     本地图片地址，缩略图大小
     * @param title      网页标题
     * @param summary    网页摘要
     */
    public static ShareEntity createWebPageInfo(@ParamsRequired boolean isTimeLine, @ParamsRequired String webUrl, Bitmap imgBitmap, String title, String summary) {
        ShareEntity entity = new ShareEntity(isTimeLine ? TYPE_WX_TIMELINE : TYPE_WX);
        addParams(entity.params, KEY_WX_TYPE, TYPE_WEB);
        addParams(entity.params, KEY_WX_WEB_URL, webUrl);
        addTitleSummaryAndThumb(entity.params, title, summary, imgBitmap);
        return entity;
    }

    /**
     * 分享小程序
     *
     * @param wxAppId
     * @param webUrl
     * @param path
     * @param imgBitmap
     * @param title
     * @param summary
     * @return
     */
    public static ShareEntity createMiniAppInfo(String wxAppId, boolean isRelease, String webUrl, String path, Bitmap imgBitmap, String title, String summary) {
        ShareEntity entity = new ShareEntity(TYPE_WX);
        addParams(entity.params, KEY_WX_TYPE, TYPE_MINI_APP);
        addParams(entity.params, KEY_WX_MINI_PROGRAM_TYPE, isRelease);
        addParams(entity.params, KEY_WX_WEB_URL, webUrl);
        addParams(entity.params, KEY_WX_MINI_PROGRAM_URL, path);
        addParams(entity.params, KEY_WX_MINI_PROGRAM_ID, wxAppId);
        addTitleSummaryAndThumb(entity.params, title, summary, imgBitmap);
        return entity;
    }

    /**
     * @param title   标题
     * @param summary 摘要
     * @param imgBitmap  图片Bitmap
     */
    private static void addTitleSummaryAndThumb(Bundle params, String title, String summary, Bitmap imgBitmap) {
        addParams(params, KEY_WX_TITLE, title);
        addParams(params, KEY_WX_SUMMARY, summary);
        addParams(params, KEY_WX_IMG_BITMAP, imgBitmap);
    }
}

