package com.gentriolee.sharego.core.entities;

import android.graphics.Bitmap;

import com.gentriolee.socialgo.annotation.ParamsRequired;

import static com.gentriolee.socialgo.core.ISocial.TARGET_WB;

/**
 * Created by gentriolee
 */

public class WBShareEntity extends ShareEntity {

    private WBShareEntity() {
        super(TARGET_WB);
    }

    /**
     * @param text 分享文本内容
     */
    public static ShareEntity createTextInfo(@ParamsRequired String text) {
        return createTextInfo(TARGET_WB, text);
    }

    /**
     * 分享图文
     *
     * @param imgBitmap  本地图片
     */
    public static ShareEntity createImageInfo(@ParamsRequired Bitmap imgBitmap) {
        return createImageInfo(TARGET_WB, imgBitmap);
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
        return createWebInfo(TARGET_WB, title, summary, webUrl, imgBitmap);
    }
}
