package com.gentriolee.socialgo.core;

import android.app.Activity;
import android.text.TextUtils;

import com.tencent.tauth.Tencent;

/**
 * Created by gentriolee
 */

public class QQSocial {

    protected Activity activity;
    protected Tencent tencent;

    protected QQSocial(Activity activity, String appId) {
        this.activity = activity;

        if (TextUtils.isEmpty(appId)) {
            throw new RuntimeException("QQ's appId is empty!");
        }
        tencent = Tencent.createInstance(appId, activity.getApplicationContext());
    }
}
