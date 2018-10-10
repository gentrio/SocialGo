package com.gentriolee.socialgo.core;

import android.support.annotation.StringRes;

import com.gentriolee.socialgo.config.SocialConfig;

/**
 * Create by gentriolee
 */

public class BaseSocial {

    protected String appId;

    protected BaseSocial(String appId) {
        this.appId = appId;
    }

    protected final String getString(@StringRes int resId) {
        return SocialConfig.getInstance().getContext().getString(resId);
    }
}
