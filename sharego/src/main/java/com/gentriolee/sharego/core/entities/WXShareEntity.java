package com.gentriolee.sharego.core.entities;

import android.graphics.Bitmap;

import com.gentriolee.socialgo.annotation.ParamsRequired;

import static com.gentriolee.socialgo.core.ISocial.TARGET_WX;
import static com.gentriolee.socialgo.core.ISocial.TARGET_WX_TIMELINE;

/**
 * Created by gentriolee
 */

public class WXShareEntity extends ShareEntity {

    /**
     * 微信小程序特有参数 公用参数在ShareEntity中
     */
    public static final String SHARE_MINI_APP_PATH = "miniAppPath";
    public static final String SHARE_MINI_APP_ID = "miniAppId";
    public static final String SHARE_MINI_APP_TYPE = "miniAppType";

    private WXShareEntity(int target) {
        super(target);
    }

    /**
     * 分享文本
     *
     * @param text       文本
     */
    public static ShareEntity createTextInfo(@ParamsRequired String text) {
        return createTextInfo(TARGET_WX, text);
    }

    /**
     * 分享文本到朋友圈
     *
     * @param text       文本
     */
    public static ShareEntity createTextInfoToWXTimeLine(@ParamsRequired String text) {
        return createTextInfo(TARGET_WX_TIMELINE, text);
    }

    /**
     * 分享图片
     *
     * @param imgBitmap     图片Bitmap
     */
    public static ShareEntity createImageInfo(@ParamsRequired Bitmap imgBitmap) {
        return createImageInfo(TARGET_WX, imgBitmap);
    }

    /**
     * 分享图片到朋友圈
     *
     * @param imgBitmap 图片Bitmap
     */
    public static ShareEntity createImageInfoToWXTimeLine(@ParamsRequired Bitmap imgBitmap) {
        return createImageInfo(TARGET_WX_TIMELINE, imgBitmap);
    }

    /**
     * 分享网页
     *
     * @param webUrl     视频url，不支持本地音乐
     * @param imgBitmap  本地图片地址，缩略图大小
     * @param title      网页标题
     * @param summary    网页摘要
     */
    public static ShareEntity createWebInfo(@ParamsRequired String title,
                                            @ParamsRequired String summary, @ParamsRequired String webUrl, @ParamsRequired Bitmap imgBitmap) {
        return createWebInfo(TARGET_WX, title, summary, webUrl, imgBitmap);
    }

    /**
     * 分享网页到朋友圈
     *
     * @param webUrl     视频url，不支持本地音乐
     * @param imgBitmap  本地图片地址，缩略图大小
     * @param title      网页标题
     * @param summary    网页摘要
     */
    public static ShareEntity createWebInfoToWXTimeLine(@ParamsRequired String title,
                                                        @ParamsRequired String summary, @ParamsRequired String webUrl, @ParamsRequired Bitmap imgBitmap) {
        return createWebInfo(TARGET_WX_TIMELINE, title, summary, webUrl, imgBitmap);
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
    public static ShareEntity createMiniAppInfo(@ParamsRequired boolean isRelease, @ParamsRequired String wxAppId,
                                                @ParamsRequired String path, @ParamsRequired String title, @ParamsRequired String summary,
                                                @ParamsRequired String webUrl, @ParamsRequired Bitmap imgBitmap) {
        WXShareEntity entity = new WXShareEntity(TARGET_WX);
        addParams(entity.params, SHARE_TYPE, SHARE_TYPE_MINI_APP);
        addParams(entity.params, SHARE_MINI_APP_TYPE, isRelease);
        addParams(entity.params, SHARE_LINK, webUrl);
        addParams(entity.params, SHARE_MINI_APP_PATH, path);
        addParams(entity.params, SHARE_MINI_APP_ID, wxAppId);
        addParams(entity.params, SHARE_TITLE, title);
        addParams(entity.params, SHARE_DESC, summary);
        addParams(entity.params, SHARE_IMAGE_BITMAP, imgBitmap);
        return entity;
    }
}

