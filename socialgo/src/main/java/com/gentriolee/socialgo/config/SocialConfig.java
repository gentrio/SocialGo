package com.gentriolee.socialgo.config;

import android.content.Context;
import android.text.TextUtils;

/**
 * Created by gentriolee
 */

public class SocialConfig {

    private static volatile SocialConfig sInstance;

    private Builder builder;

    private Context context;

    private SocialConfig(Context context, Builder builder) {
        this.context = context;
        this.builder = builder;
    }

    public Context getContext() {
        return context;
    }

    public Builder getBuilder() {
        return builder;
    }

    /**
     * Application中最先调用
     *
     * @param builder
     */
    public static void init(Context context, SocialConfig.Builder builder) {
        if (sInstance == null) {
            synchronized (SocialConfig.class) {
                if (sInstance == null) {
                    sInstance = new SocialConfig(context, builder);
                }
            }
        }
    }

    public static SocialConfig getInstance() {
        return sInstance;
    }

    public boolean supportWX() {
        return !TextUtils.isEmpty(builder.getWxAppId());
    }

    public boolean supportQQ() {
        return !TextUtils.isEmpty(builder.getQqAppId());
    }

    public boolean supportWB() {
        return !(TextUtils.isEmpty(builder.getWbAppId()) || TextUtils.isEmpty(builder.getWbRedirectUrl()));
    }

    public boolean supportDD() {
        return !(TextUtils.isEmpty(builder.getDdAppId()));
    }

    public static final class Builder {
        private String qqAppId;

        private String wxAppId;
        private String wxSecretId;

        private String wbAppId;
        private String wbRedirectUrl;

        private String ddAppId;

        public String getQqAppId() {
            return qqAppId;
        }

        public Builder setQqAppId(String qqAppId) {
            this.qqAppId = qqAppId;
            return this;
        }

        public String getWxAppId() {
            return wxAppId;
        }

        public Builder setWxAppId(String wxAppId,String wxSecretId) {
            this.wxAppId = wxAppId;
            this.wxSecretId = wxSecretId;
            return this;
        }

        public String getWxSecretId() {
            return wxSecretId;
        }

        public String getWbAppId() {
            return wbAppId;
        }

        public Builder setWbAppId(String wbAppId, String wbRedirectUrl) {
            this.wbAppId = wbAppId;
            this.wbRedirectUrl = wbRedirectUrl;
            return this;
        }

        public String getWbRedirectUrl() {
            return wbRedirectUrl;
        }

        public String getDdAppId() {
            return ddAppId;
        }

        public Builder setDdAppId(String ddAppId) {
            this.ddAppId = ddAppId;
            return this;
        }

        public Builder build() {
            return this;
        }
    }

}

