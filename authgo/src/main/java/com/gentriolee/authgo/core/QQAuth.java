package com.gentriolee.authgo.core;

import android.app.Activity;
import android.content.Intent;

import com.gentriolee.authgo.core.callback.SocialAuthCallback;
import com.gentriolee.authgo.core.entities.QQLoginResultEntity;
import com.gentriolee.socialgo.core.QQSocial;
import com.google.gson.Gson;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

/**
 * Created by gentriolee
 */

public class QQAuth extends QQSocial implements IAuthGo, IUiListener {

    private SocialAuthCallback callback;

    QQAuth(Activity activity, String appId) {
        super(activity, appId);
    }

    @Override
    public void auth(SocialAuthCallback callback) {
        this.callback = callback;
        if (!tencent.isSessionValid()) {
            tencent.login(activity, "all", this);
        } else {
            callback.authSuccess(tencent.getAccessToken());
        }
    }

    @Override
    public void onComplete(Object obj) {
        try {
            QQLoginResultEntity qqLoginResultEntity = new Gson().fromJson(obj.toString(), QQLoginResultEntity.class);
            callback.authSuccess(qqLoginResultEntity.getAccess_token());
        } catch (Exception e) {
        }
    }

    @Override
    public void onError(UiError uiError) {
        callback.authFail(uiError.errorMessage);
    }

    @Override
    public void onCancel() {
//        callback.authCancel(activity.getString(R.string.social_social_cancel));
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Tencent.onActivityResultData(requestCode, resultCode, data, this);
    }

}

