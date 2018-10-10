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

    protected DDSocial(Activity activity, String appId, String secretId) {
        super(activity, appId);
        this.secretId = secretId;

        if (TextUtils.isEmpty(appId)) {
            throw new RuntimeException("DingTalk's appId is empty!");
        }
        iddShareApi = DDShareApiFactory.createDDShareApi(activity, appId, true);
    }

    protected boolean uninstallInterrupt(SocialCallback callback) {
        callback.setTarget(ISocial.TARGET_WX);
        socialCallback = callback;
        if (!iddShareApi.isDDAppInstalled()) {
            if (socialCallback != null) {
                socialCallback.fail(ISocial.ErrCode.ERR_NOT_INSTALLED, getString(R.string.social_uninstall_dd));
            }
            return true;
        }

        return false;
    }

}
