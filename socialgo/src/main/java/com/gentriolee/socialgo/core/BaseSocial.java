package com.gentriolee.socialgo.core;

import android.app.Activity;
import android.support.annotation.StringRes;

import com.gentriolee.socialgo.config.SocialConfig;
import com.gentriolee.socialgo.core.callback.SocialCallback;

/**
 * Create by gentriolee
 */

public class BaseSocial {

    protected SocialCallback socialCallback;
    protected Activity activity;
    protected String appId;

    protected BaseSocial(Activity activity, String appId, SocialCallback callback) {
        this.activity = activity;
        this.appId = appId;
        this.socialCallback = callback;
    }

    protected final String getString(@StringRes int resId) {
        return SocialConfig.getInstance().getContext().getString(resId);
    }
}
