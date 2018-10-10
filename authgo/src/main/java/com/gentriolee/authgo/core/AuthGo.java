package com.gentriolee.authgo.core;

import android.app.Activity;
import android.content.Intent;

import com.gentriolee.authgo.core.callback.SocialAuthCallback;
import com.gentriolee.authgo.core.callback.SocialLoginCallback;
import com.gentriolee.socialgo.annotation.Unsupported;
import com.gentriolee.socialgo.config.SocialConfig;
import com.gentriolee.socialgo.core.ISocial;
import com.gentriolee.socialgo.core.SocialGo;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;

/**
 * Created by gentriolee
 */

public class AuthGo implements ISocial {

    private static volatile AuthGo sInstance;

    private SocialConfig.Builder builder;

    private WXAuth wxAuth;
    private WBAuth wbAuth;
    private QQAuth qqAuth;
    private DDAuth ddAuth;

    private AuthGo() {
        builder = SocialConfig.getInstance().getBuilder();
    }

    public static AuthGo getInstance(){
        if (sInstance == null) {
            synchronized (AuthGo.class) {
                if (sInstance == null) {
                    sInstance = new AuthGo();
                }
            }
        }
        if (SocialGo.authGo == null) {
            SocialGo.authGo = sInstance;
        }
        return sInstance;
    }

    //<editor-fold desc="授权登录的方式">
    /**
     * 微信授权
     * @param activity
     * @param callback
     */
    public void authWX(Activity activity, SocialAuthCallback callback) {
        wxAuth = new WXAuth(activity, builder.getWxAppId(), builder.getWxSecretId());
        wxAuth.auth(callback);
    }

    /**
     * 微博授权
     * @param activity
     * @param callback
     */
    public void authWB(Activity activity, SocialAuthCallback callback) {
        wbAuth = new WBAuth(activity, builder.getWbAppId(), builder.getWbRedirectUrl());
        wbAuth.auth(callback);
    }

    /**
     * QQ授权
     * @param activity
     * @param callback
     */
    public void authQQ(Activity activity, SocialAuthCallback callback) {
        qqAuth = new QQAuth(activity, builder.getQqAppId());
        qqAuth.auth(callback);
    }

    /**
     * 钉钉授权
     * @param activity
     * @param callback
     */
    public void authDD(Activity activity, SocialAuthCallback callback) {
        ddAuth = new DDAuth(activity, builder.getDdAppId(), builder.getDdSecretId());
        ddAuth.auth(callback);
    }

    /**
     * 微信登录
     * @param activity
     * @param callback
     */
    public void loginWX(Activity activity, SocialLoginCallback callback) {
        wxAuth = new WXAuth(activity, builder.getWxAppId(), builder.getWxSecretId());
        wxAuth.login(callback);
    }

    /**
     * 微博登录
     * @param activity
     * @param callback
     */
    public void loginWB(Activity activity, SocialLoginCallback callback) {
        wbAuth = new WBAuth(activity, builder.getWbAppId(), builder.getWbRedirectUrl());
        wbAuth.login(callback);
    }

    /**
     * QQ登录
     * @param activity
     * @param callback
     */
    public void loginQQ(Activity activity, SocialLoginCallback callback) {
        qqAuth = new QQAuth(activity, builder.getQqAppId());
        qqAuth.login(callback);
    }

    /**
     * 钉钉登录
     * @param activity
     * @param callback
     */
    public void loginDD(Activity activity, SocialAuthCallback callback) {
        ddAuth = new DDAuth(activity, builder.getDdAppId(), builder.getDdSecretId());
        ddAuth.login(callback);
    }
    //</editor-fold>

    //<editor-fold desc="授权需要的回调处理">
    @Override
    public void onWXAPIHandlerReq(BaseReq baseReq) {
        if (wxAuth != null) {
            wxAuth.onReq(baseReq);
        }
    }

    @Override
    public void onWXAPIHandlerResp(BaseResp baseResp) {
        if (wxAuth != null) {
            wxAuth.onResp(baseResp);
        }
    }

    @Override
    @Unsupported
    public void onDDAPIHandlerReq(com.android.dingtalk.share.ddsharemodule.message.BaseReq baseReq) {
        //暂不支持钉钉授权登录
    }

    @Override
    @Unsupported
    public void onDDAPIHandlerResp(com.android.dingtalk.share.ddsharemodule.message.BaseResp baseResp) {
        //暂不支持钉钉授权登录
    }

    /**
     * qq登录和分享以及微博登录都需要在其当前的activity的onActivityResult中调用该方法
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (qqAuth != null) {
            qqAuth.onActivityResult(requestCode, resultCode, data);
        }
        if (wbAuth != null) {
            wbAuth.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        //nothing
    }
    //</editor-fold>
}
