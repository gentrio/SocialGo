package com.gentriolee.authgo.core;

import android.app.Activity;
import android.content.Intent;

import com.gentriolee.authgo.core.callback.SocialAuthCallback;
import com.gentriolee.authgo.core.entities.QQLoginResultEntity;
import com.gentriolee.socialgo.core.ISocial;
import com.gentriolee.socialgo.core.QQSocial;
import com.google.gson.Gson;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

/**
 * Created by gentriolee
 */

public class QQAuth extends QQSocial implements IAuth, IUiListener {

    private SocialAuthCallback authCallback;

    QQAuth(Activity activity, String appId) {
        super(activity, appId);
    }

    @Override
    public void auth(SocialAuthCallback callback) {
        callback.setTarget(ISocial.TARGET_QQ);
        this.authCallback = callback;
        if (!tencent.isSessionValid()) {
            tencent.login(activity, "all", this);
        } else {
            if (authCallback != null) {
                authCallback.authSuccess(tencent.getAccessToken());
            }
        }
    }

    @Override
    public void onComplete(Object obj) {
        try {
            QQLoginResultEntity qqLoginResultEntity = new Gson().fromJson(obj.toString(), QQLoginResultEntity.class);
            if (authCallback != null) {
                authCallback.authSuccess(qqLoginResultEntity.getAccess_token());
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void onError(UiError uiError) {
        if (authCallback != null) {
            authCallback.authFail(ErrCode.ERR_SDK_INTERNAL, uiError.errorMessage);
        }
    }

    @Override
    public void onCancel() {
        if (authCallback != null) {
            authCallback.authCancel();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Tencent.onActivityResultData(requestCode, resultCode, data, this);
    }

}

