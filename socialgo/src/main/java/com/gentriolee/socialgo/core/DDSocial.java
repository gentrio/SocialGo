package com.gentriolee.socialgo.core;

import android.app.Activity;
import android.text.TextUtils;

import com.android.dingtalk.share.ddsharemodule.DDShareApiFactory;
import com.android.dingtalk.share.ddsharemodule.IDDShareApi;
import com.gentriolee.socialgo.R;
import com.gentriolee.socialgo.core.callback.SocialCallback;

/**
 * Created by gentriolee
 */

public class DDSocial extends BaseSocial{

    protected IDDShareApi iddShareApi;
    protected String secretId;

    protected DDSocial(Activity activity, String appId, String secretId, SocialCallback callback) {
        super(activity, appId, callback);
        this.secretId = secretId;

        if (callback != null) {
            callback.setTarget(ISocial.TARGET_DD);
        }

        if (TextUtils.isEmpty(appId) || TextUtils.isEmpty(secretId)) {
            if (callback != null) {
                callback.fail(ISocial.ErrCode.ERR_APPID_EMPTY, "DingTalk's appId or secretId is empty!");
            }
            return;
        }

        iddShareApi = DDShareApiFactory.createDDShareApi(activity, appId, true);
    }

    protected boolean unInitInterrupt() {
        if (iddShareApi == null) {
            return true;
        }

        if (!iddShareApi.isDDAppInstalled()) {
            if (socialCallback != null) {
                socialCallback.fail(ISocial.ErrCode.ERR_NOT_INSTALLED, getString(R.string.social_uninstall_dd));
            }
            return true;
        }

        return false;
    }

}
