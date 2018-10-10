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

    protected WXSocial(Activity activity, String appId, String secretId) {
        super(activity, appId);
        this.secretId = secretId;

        if (TextUtils.isEmpty(appId)) {
            throw new RuntimeException("Wechat's appId is empty!");
        }

        iwxapi = WXAPIFactory.createWXAPI(activity, appId, true);
        iwxapi.registerApp(appId);
    }

    protected boolean uninstallInterrupt(SocialCallback callback) {
        callback.setTarget(ISocial.TARGET_WX);
        socialCallback = callback;
        if (!iwxapi.isWXAppInstalled()) {
            if (socialCallback != null) {
                socialCallback.fail(ISocial.ErrCode.ERR_NOT_INSTALLED, getString(R.string.social_uninstall_wx));
            }
            return true;
        }

        return false;
    }
}
