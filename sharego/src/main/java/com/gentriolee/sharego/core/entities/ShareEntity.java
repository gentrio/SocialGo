package com.gentriolee.sharego.core.entities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;

import java.util.ArrayList;

/**
 * Created by gentriolee
 */

public class ShareEntity {

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

