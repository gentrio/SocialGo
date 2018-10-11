package com.gentriolee.sharego.core;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.gentriolee.sharego.R;
import com.gentriolee.sharego.core.callback.SocialShareCallback;
import com.gentriolee.sharego.core.entities.QQShareEntity;
import com.gentriolee.sharego.core.entities.ShareEntity;
import com.gentriolee.socialgo.core.ISocial;
import com.gentriolee.socialgo.core.QQSocial;
import com.gentriolee.socialgo.core.callback.SocialCallback;
import com.tencent.connect.share.QzonePublish;
import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.util.ArrayList;

import static com.tencent.connect.share.QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL;
import static com.tencent.connect.share.QQShare.SHARE_TO_QQ_IMAGE_URL;
import static com.tencent.connect.share.QQShare.SHARE_TO_QQ_KEY_TYPE;
import static com.tencent.connect.share.QQShare.SHARE_TO_QQ_SUMMARY;
import static com.tencent.connect.share.QQShare.SHARE_TO_QQ_TARGET_URL;
import static com.tencent.connect.share.QQShare.SHARE_TO_QQ_TITLE;
import static com.tencent.connect.share.QQShare.SHARE_TO_QQ_TYPE_DEFAULT;
import static com.tencent.connect.share.QQShare.SHARE_TO_QQ_TYPE_IMAGE;

/**
 * Created by gentriolee
 */

public class QQShare extends QQSocial implements IShare {

    private IUiListener shareListener;

    QQShare(Activity activity, String appId, SocialCallback callback) {
        super(activity, appId, callback);
    }

    private void initShareListener() {
        shareListener = new NormalUIListener(activity, socialCallback) {
            @Override
            public void onComplete(Object o) {
                if (socialCallback instanceof SocialShareCallback) {
                    ((SocialShareCallback) socialCallback).success();
                }
            }
        };
    }

    /**
     * qq有个坑，采用onActivityResult的方式，如果留在qq的话，home键退出之后无法正确回调
     */
    @Override
    public void share(ShareEntity shareInfo) {
        if (unInitInterrupt()) {
            return;
        }

        initShareListener();
        Bundle qShareParams = getQShareParams(shareInfo);
        if (shareInfo.getTarget() == ISocial.TARGET_QQ) {
            if (!specialShareQQText(shareInfo)) {
                tencent.shareToQQ(activity, qShareParams, shareListener);
            }
        } else {
            if (qShareParams.containsKey(QzonePublish.PUBLISH_TO_QZONE_KEY_TYPE) &&
                    QzonePublish.PUBLISH_TO_QZONE_TYPE_PUBLISHMOOD == qShareParams.getInt(QzonePublish.PUBLISH_TO_QZONE_KEY_TYPE)) {
                tencent.publishToQzone(activity, qShareParams, shareListener);
            } else {
                tencent.shareToQzone(activity, qShareParams, shareListener);
            }
        }
    }

    /**
     * 特殊处理分享到QQ好友纯文字 原生SDK不支持分享纯文字
     * @param shareEntity
     * @return
     */
    private boolean specialShareQQText(ShareEntity shareEntity) {
        Bundle originParams = shareEntity.getParams();
        if (originParams.containsKey(QQShareEntity.SHARE_TYPE) &&
                QQShareEntity.SHARE_TYPE_TEXT == originParams.getInt(QQShareEntity.SHARE_TYPE)) {
            try {
                //分享纯文字到qq好友
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, originParams.getString(ShareEntity.SHARE_TITLE));
                intent.setComponent(new ComponentName("com.tencent.mobileqq", "com.tencent.mobileqq.activity.JumpActivity"));
                activity.startActivity(intent);
            } catch (Exception e) {
                //nothing
            }
            return true;
        }
        return false;
    }

    /**
     * 转换成QQ分享的Params结构
     * @param shareEntity
     * @return
     */
    private Bundle getQShareParams(ShareEntity shareEntity) {
        if (shareEntity.getTarget() == ISocial.TARGET_QQ) {
            return getQQParams(shareEntity.getParams());
        } else {
            return getQZoneParams(shareEntity.getParams());
        }
    }

    /**
     * 分享到QQ好友结构转换
     * @param originParams
     * @return
     */
    private Bundle getQQParams(Bundle originParams) {
        Bundle params = new Bundle();
        if (originParams.containsKey(QQShareEntity.SHARE_TYPE)) {
            switch (originParams.getInt(QQShareEntity.SHARE_TYPE)) {
                case QQShareEntity.SHARE_TYPE_IMG:
                    params.putInt(SHARE_TO_QQ_KEY_TYPE, SHARE_TO_QQ_TYPE_IMAGE);
                    params.putString(SHARE_TO_QQ_IMAGE_LOCAL_URL, originParams.getString(ShareEntity.SHARE_IMAGE_BITMAP));
                    break;
                case QQShareEntity.SHARE_TYPE_WEB:
                    params.putInt(SHARE_TO_QQ_KEY_TYPE, SHARE_TO_QQ_TYPE_DEFAULT);
                    params.putString(SHARE_TO_QQ_TITLE, originParams.getString(ShareEntity.SHARE_TITLE));
                    params.putString(SHARE_TO_QQ_SUMMARY,originParams.getString(ShareEntity.SHARE_DESC));
                    params.putString(SHARE_TO_QQ_TARGET_URL,originParams.getString(ShareEntity.SHARE_LINK));
                    params.putString(SHARE_TO_QQ_IMAGE_URL, originParams.getString(ShareEntity.SHARE_IMAGE_BITMAP));
                    break;
            }
        }
        return params;
    }

    /**
     * 分享到QQ空间结构转换
     * @param originParams
     * @return
     */
    private Bundle getQZoneParams(Bundle originParams) {
        Bundle params = new Bundle();
        if (originParams.containsKey(QQShareEntity.SHARE_TYPE)) {
            switch (originParams.getInt(QQShareEntity.SHARE_TYPE)) {
                case QQShareEntity.SHARE_TYPE_TEXT:
                    params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzonePublish.PUBLISH_TO_QZONE_TYPE_PUBLISHMOOD);
                    params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, originParams.getString(ShareEntity.SHARE_TITLE));
                    break;
                case QQShareEntity.SHARE_TYPE_IMG:
                    params.putInt(QzonePublish.PUBLISH_TO_QZONE_KEY_TYPE, QzonePublish.PUBLISH_TO_QZONE_TYPE_PUBLISHMOOD);
                    ArrayList<String> imgs = new ArrayList<>();
                    imgs.add(originParams.getString(ShareEntity.SHARE_IMAGE_BITMAP));
                    params.putStringArrayList(QzonePublish.PUBLISH_TO_QZONE_IMAGE_URL, imgs);
                    break;
                case QQShareEntity.SHARE_TYPE_WEB:
                    params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
                    params.putString(QzoneShare.SHARE_TO_QQ_TITLE,originParams.getString(ShareEntity.SHARE_TITLE));
                    params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY,originParams.getString(ShareEntity.SHARE_DESC));
                    params.putString(SHARE_TO_QQ_TARGET_URL,originParams.getString(ShareEntity.SHARE_LINK));
                    ArrayList<String> webImgs = new ArrayList<>();
                    webImgs.add(originParams.getString(ShareEntity.SHARE_IMAGE_BITMAP));
                    params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, webImgs);
                    break;
            }
        }
        return params;
    }

    /**
     * 2、被调用，回调到{@link #shareListener}，然后调用{@link #shareListener}的onComplete方法
     */
    void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (shareListener != null) {
            Tencent.onActivityResultData(requestCode, resultCode, data, shareListener);
        }
    }

    private abstract static class NormalUIListener implements IUiListener {
        private SocialCallback callback;
        private Context context;

        NormalUIListener(Context context, SocialCallback callback) {
            this.context = context;
            this.callback = callback;
        }

        @Override
        public void onError(UiError uiError) {
            if (callback != null) {
                callback.fail(ErrCode.ERR_SDK_INTERNAL, uiError.errorMessage);
            }
        }

        @Override
        public void onCancel() {
            if (callback != null && context != null) {
                callback.cancel();
            }
        }
    }
}

