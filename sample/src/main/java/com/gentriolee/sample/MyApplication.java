package com.gentriolee.sample;

import android.app.Application;

import com.gentriolee.socialgo.config.SocialConfig;

/**
 * Create by gentriolee
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        initSocial();
    }

    /**
     * 初始化
     */
    private void initSocial() {
        SocialConfig.Builder builder = new SocialConfig.Builder()
                .setQqAppId("")
                .setWxAppId("")
                .setWbAppId("")
                .setWbRedirectUrl("")
                .setDdAppId("")
                .build();
        SocialConfig.init(getApplicationContext(), builder);
    }
}
