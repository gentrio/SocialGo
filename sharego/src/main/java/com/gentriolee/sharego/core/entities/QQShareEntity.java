package com.gentriolee.sharego.core.entities;

import android.graphics.Bitmap;

import com.gentriolee.socialgo.annotation.ParamsRequired;

import static com.gentriolee.socialgo.core.ISocial.TARGET_QQ;
import static com.gentriolee.socialgo.core.ISocial.TARGET_QQ_ZONE;

/**
 * Created by gentriolee
 */

public final class QQShareEntity extends ShareEntity {

    private QQShareEntity(int target) {
        super(target);
    }

    /**
     * 分享纯文字到QQ
     * @param text
     * @return
     */
    public static ShareEntity createTextInfo(@ParamsRequired String text) {
        return createTextInfo(TARGET_QQ, text);
    }

    /**
     * 分享纯文字到QQ空间
     * @param text
     * @return
     */
    public static ShareEntity createTextInfoToQZone(@ParamsRequired String text) {
        return createTextInfo(TARGET_QQ_ZONE, text);
    }

    /**
     * 创建分享纯图片到qq
     *
     * @param imgBitmap 本地图片地址
     */
    public static ShareEntity createImageInfo(@ParamsRequired Bitmap imgBitmap) {
        return createImageInfo(TARGET_QQ, imgBitmap);
    }

    /**
     * 创建分享纯图片到qq空间
     *
     * @param imgBitmap 本地图片地址
     */
    public static ShareEntity createImageInfoToQZone(@ParamsRequired Bitmap imgBitmap) {
        return createImageInfo(TARGET_QQ_ZONE, imgBitmap);
    }

    /**
     * 创建分享链接类型到qq
     *
     * @param title     标题，长度限制30个字符
     * @param summary   摘要，长度限制40个字
     * @param targetUrl 跳转地址
     * @param imgBitmap 图片地址，本地路径或者url
     */
    public static ShareEntity createWebInfo(@ParamsRequired String title, @ParamsRequired String summary, @ParamsRequired String targetUrl,
                                            @ParamsRequired Bitmap imgBitmap) {
        return createWebInfo(TARGET_QQ, title, summary, targetUrl, imgBitmap);
    }

    /**
     * 创建分享链接到qq空间
     *
     * @param title     标题，长度限制200个字符
     * @param summary   摘要，长度限制600个字
     * @param targetUrl 跳转地址
     * @param imgBitmap 图片地址，目前会第一张有效，待qq优化
     */
    public static ShareEntity createWebInfoToQZone(@ParamsRequired String title, @ParamsRequired String summary, @ParamsRequired String targetUrl,
                                                   @ParamsRequired Bitmap imgBitmap) {
        return createWebInfo(TARGET_QQ_ZONE, title, summary, targetUrl, imgBitmap);
    }
}

