package com.gentriolee.socialgo.core;

import android.app.Activity;
import android.text.TextUtils;

import com.gentriolee.socialgo.R;
import com.gentriolee.socialgo.core.callback.SocialCallback;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * Created by gentriolee
 */

public class WXSocial extends BaseSocial{

    protected IWXAPI iwxapi;
    protected String secretId;

    protected WXSocial(Activity activity, String appId, String secretId, SocialCallback callback) {
        super(activity, appId, callback);
        this.secretId = secretId;

        if (callback != null) {
            callback.setTarget(ISocial.TARGET_WX);
        }

        if (TextUtils.isEmpty(appId) || TextUtils.isEmpty(secretId)) {
            if (callback != null) {
                callback.fail(ISocial.ErrCode.ERR_APPID_EMPTY, "Wechat's appId or secretId is empty!");
            }
            return;
        }

        iwxapi = WXAPIFactory.createWXAPI(activity, appId, true);
        iwxapi.registerApp(appId);
    }

    protected boolean unInitInterrupt() {
        if (iwxapi == null) {
            return true;
        }

        if (!iwxapi.isWXAppInstalled()) {
            if (socialCallback != null) {
                socialCallback.fail(ISocial.ErrCode.ERR_NOT_INSTALLED, getString(R.string.social_uninstall_wx));
            }
            return true;
        }

        return false;
    }
}
