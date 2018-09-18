package com.gentriolee.socialgo.core;

import android.app.Activity;
import android.text.TextUtils;

import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * Created by gentriolee
 */

public class WXSocial extends BaseSocial{

    protected Activity activity;
    protected IWXAPI iwxapi;

    protected WXSocial(Activity activity, String appId) {
        this.activity = activity;

        if (TextUtils.isEmpty(appId)) {
            throw new RuntimeException("Wechat's appId is empty!");
        }

        iwxapi = WXAPIFactory.createWXAPI(activity, appId, true);
        iwxapi.registerApp(appId);
    }
}
