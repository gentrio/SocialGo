package com.gentriolee.sharego.core;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.gentriolee.sharego.core.callback.SocialShareCallback;
import com.gentriolee.sharego.core.entities.ShareEntity;
import com.gentriolee.socialgo.core.QQSocial;
import com.tencent.connect.share.QzonePublish;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

/**
 * Created by gentriolee
 */

public class QQShare extends QQSocial implements IShare {

    private SocialShareCallback shareCallback;
    private IUiListener shareListener;
    private int target;

    QQShare(Activity activity, String appId) {
        super(activity, appId);
    }

    private void initShareListener() {
        shareListener = new NormalUIListener(activity, target, shareCallback) {
            @Override
            public void onComplete(Object o) {
                if (shareCallback != null) {
                    shareCallback.shareSuccess(target);
                }
            }
        };
    }

    /**
     * qq有个坑，采用onActivityResult的方式，如果留在qq的话，home键退出之后无法正确回调
     */
    @Override
    public void share(SocialShareCallback callback, ShareEntity shareInfo) {
        this.shareCallback = callback;
        this.target = shareInfo.getType();
        if (!tencent.isQQInstalled(activity)) {
            if (callback != null) {
                callback.shareFail(target, ErrCode.ERR_NOT_INSTALLED);
            }
            return;
        }
        initShareListener();
        if (shareInfo.getType() == ShareEntity.TYPE_QQ) {
            tencent.shareToQQ(activity, shareInfo.getParams(), shareListener);
        } else {
            if (shareInfo.getParams().containsKey(QzonePublish.PUBLISH_TO_QZONE_KEY_TYPE) &&
                    QzonePublish.PUBLISH_TO_QZONE_TYPE_PUBLISHMOOD == shareInfo.getParams().getInt(QzonePublish.PUBLISH_TO_QZONE_KEY_TYPE)) {
                tencent.publishToQzone(activity, shareInfo.getParams(), shareListener);
            } else {
                tencent.shareToQzone(activity, shareInfo.getParams(), shareListener);
            }
        }
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
        private SocialShareCallback callback;
        private Context context;
        private int target;

        NormalUIListener(Context context, int target, SocialShareCallback callback) {
            this.context = context;
            this.target = target;
            this.callback = callback;
        }

        @Override
        public void onError(UiError uiError) {
            if (callback != null) {
                callback.shareFail(target, ErrCode.ERR_SHARE_FAILD);
            }
        }

        @Override
        public void onCancel() {
            if (callback != null && context != null) {
                callback.shareCancel(target);
            }
        }
    }
}

