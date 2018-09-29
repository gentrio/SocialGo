package com.gentriolee.sharego.core.entities;

import android.graphics.Bitmap;

import com.gentriolee.socialgo.annotation.ParamsRequired;

import static com.gentriolee.socialgo.core.ISocial.TARGET_DD;

/**
 * Created by gentriolee
 */

public class DDShareEntity extends ShareEntity {

    private DDShareEntity() {
        super(TARGET_DD);
    }

    /**
     * 分享文字
     * @param text 分享文本内容
     */
    public static ShareEntity createTextInfo(@ParamsRequired String text) {
        return createTextInfo(TARGET_DD, text);
    }

    /**
     * 分享图片
     *
     * @param imgBitmap     本地图片地址
     */
    public static ShareEntity createImageInfo(@ParamsRequired Bitmap imgBitmap) {
        return createImageInfo(TARGET_DD, imgBitmap);
    }

    /**
     * 分享网页
     *
     * @param title     网页标题
     * @param summary   网页摘要
     * @param webUrl    网页链接
     * @param imgBitmap 网页左边图标，本地路径
     */
    public static ShareEntity createWebInfo(@ParamsRequired String title, @ParamsRequired String summary, @ParamsRequired String webUrl, @ParamsRequired Bitmap imgBitmap) {
        return createWebInfo(TARGET_DD, title, summary, webUrl, imgBitmap);
    }
}

