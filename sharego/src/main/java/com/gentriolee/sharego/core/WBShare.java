package com.gentriolee.sharego.core;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.gentriolee.sharego.core.callback.SocialShareCallback;
import com.gentriolee.sharego.core.entities.ShareEntity;
import com.gentriolee.sharego.core.entities.WBShareEntity;
import com.gentriolee.sharego.utils.ShareUtils;
import com.gentriolee.socialgo.core.WBSocial;
import com.gentriolee.socialgo.core.callback.SocialCallback;
import com.sina.weibo.sdk.api.BaseMediaObject;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.share.WbShareCallback;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.sina.weibo.sdk.utils.Utility;

/**
 * Created by gentriolee
 */

final class WBShare extends WBSocial implements IShare {

    private WbShareCallback wbShareCallback;

    WBShare(Activity activity, String appId, String redirectUrl, SocialCallback callback) {
        super(activity, appId, redirectUrl, callback);
    }

    @Override
    public void share(ShareEntity shareInfo) {
        if (unInitInterrupt()) {
            return;
        }

        initShareLister();
        shareHandler = new WbShareHandler(activity);
        shareHandler.registerApp();

        WeiboMultiMessage weiboMessage = getShareMessage(shareInfo.getParams());
        if (weiboMessage == null) {
            return;
        }
        shareHandler.shareMessage(weiboMessage, false);
    }

    private void initShareLister() {
        wbShareCallback = new WbShareCallback() {
            @Override
            public void onWbShareSuccess() {
                if (socialCallback instanceof SocialShareCallback) {
                    ((SocialShareCallback) socialCallback).success();
                }
            }

            @Override
            public void onWbShareCancel() {
                if (socialCallback != null && activity != null) {
                    socialCallback.cancel();
                }
            }

            @Override
            public void onWbShareFail() {
                if (socialCallback != null && activity != null) {
                    socialCallback.fail(ErrCode.ERR_SDK_INTERNAL, "");
                }
            }
        };
    }

    private WeiboMultiMessage getShareMessage(Bundle params) {
        WeiboMultiMessage msg = new WeiboMultiMessage();
        int type = params.getInt(WBShareEntity.SHARE_TYPE);
        BaseMediaObject mediaObject = null;
        switch (type) {
            case WBShareEntity.SHARE_TYPE_TEXT:
                msg.textObject = getTextObj(params);
                mediaObject = msg.textObject;
                break;
            case WBShareEntity.SHARE_TYPE_IMG:
                msg.imageObject = getImageObj(params);
                mediaObject = msg.imageObject;
                break;
            case WBShareEntity.SHARE_TYPE_WEB:
                msg.mediaObject = getWebPageObj(params);
                msg.textObject = getTextObj(params);
                mediaObject = msg.mediaObject;
                break;
        }
        if (mediaObject == null) {
            return null;
        }
        return msg;
    }

    private TextObject getTextObj(Bundle params) {
        TextObject textObj = new TextObject();
        textObj.text = params.getString(WBShareEntity.SHARE_TITLE);
        return textObj;
    }

    private ImageObject getImageObj(Bundle params) {
        ImageObject imgObj = new ImageObject();
        if (params.containsKey(WBShareEntity.SHARE_IMAGE_BITMAP)) {//分为本地文件和应用内资源图片
            Bitmap bitmap = params.getParcelable(WBShareEntity.SHARE_IMAGE_BITMAP);
            imgObj.setImageObject(bitmap);
        }
        return imgObj;
    }

    private WebpageObject getWebPageObj(Bundle params) {
        WebpageObject webpageObject = new WebpageObject();
        webpageObject.identify = Utility.generateGUID();
        webpageObject.actionUrl = params.getString(WBShareEntity.SHARE_LINK);
        if (addTitleSummaryAndThumb(webpageObject, params)) {
            return null;
        }
        return webpageObject;
    }

    /**
     * 当有设置缩略图但是找不到的时候阻止分享
     */
    private boolean addTitleSummaryAndThumb(BaseMediaObject msg, Bundle params) {
        if (params.containsKey(WBShareEntity.SHARE_TITLE)) {
            msg.title = params.getString(WBShareEntity.SHARE_TITLE);
        }

        if (params.containsKey(WBShareEntity.SHARE_DESC)) {
            msg.description = params.getString(WBShareEntity.SHARE_DESC);
        }

        if (params.containsKey(WBShareEntity.SHARE_IMAGE_BITMAP)) {
            Bitmap bitmap = params.getParcelable(WBShareEntity.SHARE_IMAGE_BITMAP);
            msg.thumbData = ShareUtils.smallBmpToByteArray(bitmap);
        }
        return false;
    }

    void onNewIntent(Intent intent) {
        if (shareHandler != null) {
            shareHandler.doResultIntent(intent, wbShareCallback);
        }
    }
}

