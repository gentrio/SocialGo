package com.gentriolee.socialgo.core;

import android.app.Activity;
import android.text.TextUtils;

import com.gentriolee.socialgo.core.callback.SocialCallback;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.share.WbShareHandler;

/**
 * Created by gentriolee
 */

public class WBSocial extends BaseSocial{

    private static final String SCOPE = "email";

    protected SsoHandler ssoHandler;
    protected WbShareHandler shareHandler;
    protected String redirectUrl;

    protected WBSocial(Activity activity, String appId, String redirectUrl, SocialCallback callback) {
        super(activity, appId, callback);
        this.redirectUrl = redirectUrl;

        if (callback != null) {
            callback.setTarget(ISocial.TARGET_WB);
        }

        if (TextUtils.isEmpty(appId) || TextUtils.isEmpty(redirectUrl)) {
            if (callback != null) {
                callback.fail(ISocial.ErrCode.ERR_APPID_EMPTY, "WeBo's appId or redirectUrl is empty!");
            }
            return;
        }

        WbSdk.install(activity.getApplicationContext(), new AuthInfo(activity.getApplicationContext(), appId, redirectUrl, SCOPE));
    }

    protected boolean unInitInterrupt() {
        try {
            WbSdk.checkInit();
//            微博未安装时会使用网页版微博 可根据业务自行修改
//            if (!WbSdk.isWbInstall(activity)) {
//                if (socialCallback != null) {
//                    socialCallback.fail(ISocial.ErrCode.ERR_NOT_INSTALLED, getString(R.string.social_uninstall_wb));
//                }
//                return true;
//            }
        } catch (Exception e) {
            return true;
        }

        return false;
    }
}
