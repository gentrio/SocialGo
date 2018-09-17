package com.gentriolee.sharego.core.entities;

import android.graphics.Bitmap;

import com.gentriolee.sharego.constant.ShareImageConstant;
import com.gentriolee.sharego.utils.ShareFileUtils;
import com.gentriolee.socialgo.annotation.ParamsRequired;
import com.gentriolee.socialgo.config.SocialConfig;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzonePublish;
import com.tencent.connect.share.QzoneShare;

import java.util.ArrayList;

/**
 * Created by gentriolee
 */

public final class QQShareEntity extends ShareEntity {

    private QQShareEntity(int type) {
        super(type);
    }

    /**
     * 创建分享纯图片到qq
     *
     * @param imgBitmap 本地图片地址
     */
    public static ShareEntity createImageInfo(@ParamsRequired Bitmap imgBitmap) {
        ShareEntity entity = new ShareEntity(TYPE_QQ);
        addParams(entity.params, QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
        addParams(entity.params, QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, saveImgBitmap(imgBitmap));
        return entity;
    }

    /**
     * 创建分享纯图片到qq空间
     *
     * @param imgBitmap 本地图片地址
     */
    public static ShareEntity createImageInfoToQZone(@ParamsRequired Bitmap imgBitmap) {
        ShareEntity entity = new ShareEntity(TYPE_QQ_ZONE);
        addParams(entity.params, QzonePublish.PUBLISH_TO_QZONE_KEY_TYPE, QzonePublish.PUBLISH_TO_QZONE_TYPE_PUBLISHMOOD);
        addParams(entity.params, QzonePublish.PUBLISH_TO_QZONE_SUMMARY, "");
        ArrayList<String> imgs = new ArrayList<>();
        imgs.add(saveImgBitmap(imgBitmap));
        addParams(entity.params, QzonePublish.PUBLISH_TO_QZONE_IMAGE_URL, imgs);
        return entity;
    }

    /**
     * 创建分享音乐到qq
     *
     * @param title     标题，长度限制30个字符
     * @param targetUrl 跳转地址，
     * @param musicUrl  音乐地址，不支持本地音乐
     * @param imgBitmap    图片地址，本地路径或者url
     * @param summary   摘要，长度限制40个字
     */
    public static ShareEntity createMusicInfo(@ParamsRequired String title, @ParamsRequired String targetUrl,
                                              @ParamsRequired String musicUrl, Bitmap imgBitmap, String summary) {
        ShareEntity entity = new ShareEntity(TYPE_QQ);
        addParams(entity.params, QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_AUDIO);
        addParams(entity.params, QQShare.SHARE_TO_QQ_TITLE, title);
        addParams(entity.params, QQShare.SHARE_TO_QQ_TARGET_URL, targetUrl);
        addParams(entity.params, QQShare.SHARE_TO_QQ_AUDIO_URL, musicUrl);
        addParams(entity.params, QQShare.SHARE_TO_QQ_IMAGE_URL, saveImgBitmap(imgBitmap));
        addParams(entity.params, QQShare.SHARE_TO_QQ_SUMMARY, summary);
        return entity;
    }

    /**
     * 创建分享应用到qq
     *
     * @param title     标题，长度限制30个字符
     * @param imgBitmap 图片地址，本地路径或者url
     * @param summary   摘要，长度限制40个字
     */
    public static ShareEntity createAppInfo(@ParamsRequired String title, Bitmap imgBitmap, String summary) {
        ShareEntity entity = new ShareEntity(TYPE_QQ);
        addParams(entity.params, QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_APP);
        addParams(entity.params, QQShare.SHARE_TO_QQ_TITLE, title);
        addParams(entity.params, QQShare.SHARE_TO_QQ_IMAGE_URL, saveImgBitmap(imgBitmap));
        addParams(entity.params, QQShare.SHARE_TO_QQ_SUMMARY, summary);
        return entity;
    }

    /**
     * 创建分享图文类型到qq
     *
     * @param title     标题，长度限制30个字符
     * @param targetUrl 跳转地址
     * @param imgBitmap    图片地址，本地路径或者url
     * @param summary   摘要，长度限制40个字
     */
    public static ShareEntity createImageTextInfo(@ParamsRequired String title, @ParamsRequired String targetUrl,
                                                  Bitmap imgBitmap, String summary) {
        ShareEntity entity = new ShareEntity(TYPE_QQ);
        addParams(entity.params, QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        addParams(entity.params, QQShare.SHARE_TO_QQ_TITLE, title);
        addParams(entity.params, QQShare.SHARE_TO_QQ_TARGET_URL, targetUrl);
        addParams(entity.params, QQShare.SHARE_TO_QQ_IMAGE_URL, saveImgBitmap(imgBitmap));
        addParams(entity.params, QQShare.SHARE_TO_QQ_SUMMARY, summary);
        return entity;
    }

    /**
     * 创建分享图文到qq空间
     *
     * @param title     标题，长度限制200个字符
     * @param targetUrl 跳转地址
     * @param imgBitmap 图片地址，目前会第一张有效，待qq优化
     * @param summary   摘要，长度限制600个字
     */
    public static ShareEntity createImageTextInfoToQZone(@ParamsRequired String title, @ParamsRequired String targetUrl,
                                                         @ParamsRequired Bitmap imgBitmap, String summary) {
        ShareEntity entity = new ShareEntity(TYPE_QQ_ZONE);
        addParams(entity.params, QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        addParams(entity.params, QzoneShare.SHARE_TO_QQ_TITLE, title);
        addParams(entity.params, QQShare.SHARE_TO_QQ_TARGET_URL, targetUrl);
        ArrayList<String> imgs = new ArrayList<>();
        imgs.add(saveImgBitmap(imgBitmap));
        addParams(entity.params, QzoneShare.SHARE_TO_QQ_IMAGE_URL, imgs);
        addParams(entity.params, QzoneShare.SHARE_TO_QQ_SUMMARY, summary);
        return entity;
    }

    public static String saveImgBitmap(Bitmap bitmap) {
        final String directoryPath = ShareImageConstant.getShareQQImgPath(SocialConfig.getInstance().getContext());
        final String imgFileName = System.currentTimeMillis() + ShareImageConstant.SHARE_IMG_TEMP_ENDFIX;
        final String imgFilePath = directoryPath + imgFileName;
        ShareFileUtils.saveBitmap(directoryPath, imgFilePath, bitmap);
        ShareFileUtils.deleteOtherFile(directoryPath, imgFileName);
        return imgFilePath;
    }
}

