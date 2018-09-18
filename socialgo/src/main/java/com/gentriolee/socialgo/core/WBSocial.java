package com.gentriolee.socialgo.core;

import android.app.Activity;
import android.text.TextUtils;

import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.share.WbShareHandler;

/**
 * Created by gentriolee
 */

public class WBSocial extends BaseSocial{

    private static final String SCOPE = "email";

    protected Activity activity;
    protected SsoHandler ssoHandler;
    protected WbShareHandler shareHandler;

    protected WBSocial(Activity activity, String appId, String redirectUrl) {
        this.activity = activity;

        if (TextUtils.isEmpty(appId) || TextUtils.isEmpty(redirectUrl)) {
            throw new RuntimeException("WeBo's appId or redirectUrl is empty!");
        }
        WbSdk.install(activity.getApplicationContext(), new AuthInfo(activity.getApplicationContext(), appId, redirectUrl, SCOPE));
    }
}
