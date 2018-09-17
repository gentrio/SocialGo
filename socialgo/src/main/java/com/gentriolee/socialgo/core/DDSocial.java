package com.gentriolee.socialgo.core;

import android.app.Activity;
import android.text.TextUtils;

import com.android.dingtalk.share.ddsharemodule.DDShareApiFactory;
import com.android.dingtalk.share.ddsharemodule.IDDShareApi;

/**
 * Created by gentriolee
 */

public class DDSocial {
    protected Activity activity;
    protected IDDShareApi iddShareApi;

    protected DDSocial(Activity activity, String appId) {
        this.activity = activity;

        if (TextUtils.isEmpty(appId)) {
            throw new RuntimeException("DingTalk's appId is empty!");
        }
        iddShareApi = DDShareApiFactory.createDDShareApi(activity, appId, true);
    }
}
