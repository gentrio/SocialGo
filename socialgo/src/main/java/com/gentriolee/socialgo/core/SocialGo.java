package com.gentriolee.socialgo.core;

import android.content.Intent;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;

/**
 * Created by gentriolee
 */

public class SocialGo {

    public static ISocialGo shareHelper;
    public static ISocialGo authHelper;

    /**
     * 在主工程的DWXEntryActivity的onReq()中调用
     * @param baseReq
     */
    public static void onWXAPIHandlerReq(BaseReq baseReq) {
        if (shareHelper != null) {
            shareHelper.onWXAPIHandlerReq(baseReq);
        }
        if (authHelper != null) {
            authHelper.onWXAPIHandlerReq(baseReq);
        }
    }

    /**
     * 在主工程的WXEntryActivity的onResp()中调用
     * @param baseResp
     */
    public static void onWXAPIHandlerResp(BaseResp baseResp) {
        if (shareHelper != null) {
            shareHelper.onWXAPIHandlerResp(baseResp);
        }
        if (authHelper != null) {
            authHelper.onWXAPIHandlerResp(baseResp);
        }
    }

    /**
     * 在主工程的DDShareActivity的onReq()中调用
     * @param baseReq
     */
    public static void onDDAPIHandlerReq(com.android.dingtalk.share.ddsharemodule.message.BaseReq baseReq) {
        if (shareHelper != null) {
            shareHelper.onDDAPIHandlerReq(baseReq);
        }
        if (authHelper != null) {
            authHelper.onDDAPIHandlerReq(baseReq);
        }
    }

    /**
     * 在主工程的DDShareActivity的onResp()中调用
     * @param baseResp
     */
    public static void onDDAPIHandlerResp(com.android.dingtalk.share.ddsharemodule.message.BaseResp baseResp) {
        if (shareHelper != null) {
            shareHelper.onDDAPIHandlerResp(baseResp);
        }
        if (authHelper != null) {
            authHelper.onDDAPIHandlerResp(baseResp);
        }
    }

    /**
     * 在主工程唤起授权或分享的Activity的onActivityResult()中调用
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public static void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (shareHelper != null) {
            shareHelper.onActivityResult(requestCode, resultCode, data);
        }
        if (authHelper != null) {
            authHelper.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * 在主工程唤起授权或分享的Activity的onNewIntent()中调用
     * @param intent
     */
    public static void onNewIntent(Intent intent) {
        if (shareHelper != null) {
            shareHelper.onNewIntent(intent);
        }
        if (authHelper != null) {
            authHelper.onNewIntent(intent);
        }
    }
}
