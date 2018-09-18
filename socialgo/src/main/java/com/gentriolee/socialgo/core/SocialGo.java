package com.gentriolee.socialgo.core;

import android.content.Intent;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;

/**
 * Created by gentriolee
 */

public class SocialGo {

    public static ISocial shareGo;
    public static ISocial authGo;

    /**
     * 在主工程的DWXEntryActivity的onReq()中调用
     * @param baseReq
     */
    public static void onWXAPIHandlerReq(BaseReq baseReq) {
        if (shareGo != null) {
            shareGo.onWXAPIHandlerReq(baseReq);
        }
        if (authGo != null) {
            authGo.onWXAPIHandlerReq(baseReq);
        }
    }

    /**
     * 在主工程的WXEntryActivity的onResp()中调用
     * @param baseResp
     */
    public static void onWXAPIHandlerResp(BaseResp baseResp) {
        if (shareGo != null) {
            shareGo.onWXAPIHandlerResp(baseResp);
        }
        if (authGo != null) {
            authGo.onWXAPIHandlerResp(baseResp);
        }
    }

    /**
     * 在主工程的DDShareActivity的onReq()中调用
     * @param baseReq
     */
    public static void onDDAPIHandlerReq(com.android.dingtalk.share.ddsharemodule.message.BaseReq baseReq) {
        if (shareGo != null) {
            shareGo.onDDAPIHandlerReq(baseReq);
        }
        if (authGo != null) {
            authGo.onDDAPIHandlerReq(baseReq);
        }
    }

    /**
     * 在主工程的DDShareActivity的onResp()中调用
     * @param baseResp
     */
    public static void onDDAPIHandlerResp(com.android.dingtalk.share.ddsharemodule.message.BaseResp baseResp) {
        if (shareGo != null) {
            shareGo.onDDAPIHandlerResp(baseResp);
        }
        if (authGo != null) {
            authGo.onDDAPIHandlerResp(baseResp);
        }
    }

    /**
     * 在主工程唤起授权或分享的Activity的onActivityResult()中调用
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public static void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (shareGo != null) {
            shareGo.onActivityResult(requestCode, resultCode, data);
        }
        if (authGo != null) {
            authGo.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * 在主工程唤起授权或分享的Activity的onNewIntent()中调用
     * @param intent
     */
    public static void onNewIntent(Intent intent) {
        if (shareGo != null) {
            shareGo.onNewIntent(intent);
        }
        if (authGo != null) {
            authGo.onNewIntent(intent);
        }
    }
}
