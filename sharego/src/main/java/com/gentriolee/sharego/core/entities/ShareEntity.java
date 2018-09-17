package com.gentriolee.sharego.core.entities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import java.util.ArrayList;

/**
 * Created by gentriolee
 */

public class ShareEntity {

    /**
     * 微信 = 1 << 2
     * 朋友圈 = 1 << 3
     * QQ = 1 << 4
     * QQ空间 = 1<< 5
     * 微博 = 1<< 6
     * 钉钉 = 1 << 7
     */

    public static final int TYPE_WX = 1 << 2;
    public static final int TYPE_WX_TIMELINE = 1 << 3;
    public static final int TYPE_QQ = 1 << 4;
    public static final int TYPE_QQ_ZONE = 1 << 5;
    public static final int TYPE_WB = 1 << 6;
    public static final int TYPE_DD = 1 << 7;
    /**
     * 分享图片
     * 分享链接
     * 分享图片&链接
     */
    public static final int TYPE_IMG = 1;
    public static final int TYPE_LINK = 1 << 1;

    private int type;
    Bundle params;

    public ShareEntity(int type) {
        this.type = type;
        this.params = new Bundle();
    }

    public Bundle getParams() {
        return params;
    }

    public void setParams(Bundle params) {
        this.params = params;
    }

    public int getType() {
        return type;
    }

    protected static void addParams(Bundle params, String key, String value) {
        if (params == null || TextUtils.isEmpty(key) || TextUtils.isEmpty(value)) {
            return;
        }
        params.putString(key, value);
    }

    protected static void addParams(Bundle params, String key, Bitmap value) {
        if (params == null || TextUtils.isEmpty(key) || value == null) {
            return;
        }
        params.putParcelable(key, value);
    }

    protected static void addParams(Bundle params, String key, int value) {
        if (params == null || TextUtils.isEmpty(key)) {
            return;
        }
        params.putInt(key, value);
    }

    protected static void addParams(Bundle params, String key, boolean value) {
        if (params == null || TextUtils.isEmpty(key)) {
            return;
        }
        params.putBoolean(key, value);
    }

    protected static void addParams(Bundle params, String key, ArrayList<String> value) {
        if (params == null || TextUtils.isEmpty(key) || value == null || value.size() == 0) {
            return;
        }
        params.putStringArrayList(key, value);
    }
}

