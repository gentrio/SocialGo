package com.gentriolee.socialgo.core;

import android.app.Activity;
import android.text.TextUtils;

import com.gentriolee.socialgo.R;
import com.gentriolee.socialgo.core.callback.SocialCallback;
import com.tencent.tauth.Tencent;

/**
 * Created by gentriolee
 */

public class QQSocial extends BaseSocial{

    protected Tencent tencent;

    protected QQSocial(Activity activity, String appId) {
        super(activity, appId);

        if (TextUtils.isEmpty(appId)) {
            throw new RuntimeException("QQ's appId is empty!");
        }
        tencent = Tencent.createInstance(appId, activity.getApplicationContext());
    }

    protected boolean uninstallInterrupt(SocialCallback callback) {
        callback.setTarget(ISocial.TARGET_QQ);
        socialCallback = callback;
        if (!tencent.isQQInstalled(activity)) {
            if (socialCallback != null) {
                socialCallback.fail(ISocial.ErrCode.ERR_NOT_INSTALLED, getString(R.string.social_uninstall_qq));
            }
            return true;
        }

        return false;
    }
}
