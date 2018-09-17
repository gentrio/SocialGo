package com.gentriolee.authgo.core;

import android.app.Activity;
import android.content.Intent;

import com.gentriolee.authgo.core.callback.SocialAuthCallback;
import com.gentriolee.socialgo.core.WBSocial;
import com.sina.weibo.sdk.auth.AccessTokenKeeper;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WbAuthListener;
import com.sina.weibo.sdk.auth.WbConnectErrorMessage;
import com.sina.weibo.sdk.auth.sso.SsoHandler;

/**
 * Created by gentriolee
 */

public class WBAuth extends WBSocial implements IAuthGo, WbAuthListener {


    private SocialAuthCallback callback;

    WBAuth(Activity activity, String appId, String redirectUrl) {
        super(activity, appId, redirectUrl);
    }

    @Override
    public void auth(SocialAuthCallback callback) {
        this.callback = callback;

        ssoHandler = new SsoHandler(activity);
        ssoHandler.authorize(this);
    }

    @Override
    public void onSuccess(Oauth2AccessToken oauth2AccessToken) {
        if (oauth2AccessToken.isSessionValid()) {
            AccessTokenKeeper.writeAccessToken(activity, oauth2AccessToken);
            callback.authSuccess(oauth2AccessToken.getToken());
        } else {
//            callback.authFail(activity.getString(R.string.social_social_cancel));
        }
    }

    @Override
    public void cancel() {
//        callback.loginCancel(activity.getString(R.string.social_social_cancel));
    }

    @Override
    public void onFailure(WbConnectErrorMessage wbConnectErrorMessage) {
        callback.authFail(wbConnectErrorMessage.getErrorMessage());
    }

    void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }
}
