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
     * 根据target 来授权
     * @param activity
     * @param target
     * @param callback
     */
    public void auth(Activity activity, int target, SocialAuthCallback callback) {
        switch (target) {
            case ISocial.TARGET_WX:
                authWX(activity, callback);
                break;
            case ISocial.TARGET_WB:
                authWB(activity, callback);
                break;
            case ISocial.TARGET_QQ:
                authQQ(activity, callback);
                break;
            case ISocial.TARGET_DD:
                authDD(activity, callback);
                break;
        }
    }

    /**
     * 根据target 来登录
     * @param activity
     * @param target
     * @param callback
     */
    public void login(Activity activity, int target, SocialLoginCallback callback) {
        switch (target) {
            case ISocial.TARGET_WX:
                loginWX(activity, callback);
                break;
            case ISocial.TARGET_WB:
                loginWB(activity, callback);
                break;
            case ISocial.TARGET_QQ:
                loginQQ(activity, callback);
                break;
            case ISocial.TARGET_DD:
                loginDD(activity, callback);
                break;
        }
    }

    /**
     * 微信授权
     * @param activity
     * @param callback
     */
    public void authWX(Activity activity, SocialAuthCallback callback) {
        wxAuth = new WXAuth(activity, builder.getWxAppId(), builder.getWxSecretId(), callback);
        wxAuth.auth();
    }

    /**
     * 微博授权
     * @param activity
     * @param callback
     */
    public void authWB(Activity activity, SocialAuthCallback callback) {
        wbAuth = new WBAuth(activity, builder.getWbAppId(), builder.getWbRedirectUrl(), callback);
        wbAuth.auth();
    }

    /**
     * QQ授权
     * @param activity
     * @param callback
     */
    public void authQQ(Activity activity, SocialAuthCallback callback) {
        qqAuth = new QQAuth(activity, builder.getQqAppId(), callback);
        qqAuth.auth();
    }

    /**
     * 钉钉授权
     * @param activity
     * @param callback
     */
    public void authDD(Activity activity, SocialAuthCallback callback) {
        ddAuth = new DDAuth(activity, builder.getDdAppId(), builder.getDdSecretId(), callback);
        ddAuth.auth();
    }

    /**
     * 微信登录
     * @param activity
     * @param callback
     */
    public void loginWX(Activity activity, SocialLoginCallback callback) {
        wxAuth = new WXAuth(activity, builder.getWxAppId(), builder.getWxSecretId(), callback);
        wxAuth.login();
    }

    /**
     * 微博登录
     * @param activity
     * @param callback
     */
    public void loginWB(Activity activity, SocialLoginCallback callback) {
        wbAuth = new WBAuth(activity, builder.getWbAppId(), builder.getWbRedirectUrl(), callback);
        wbAuth.login();
    }

    /**
     * QQ登录
     * @param activity
     * @param callback
     */
    public void loginQQ(Activity activity, SocialLoginCallback callback) {
        qqAuth = new QQAuth(activity, builder.getQqAppId(), callback);
        qqAuth.login();
    }

    /**
     * 钉钉登录
     * @param activity
     * @param callback
     */
    public void loginDD(Activity activity, SocialLoginCallback callback) {
        ddAuth = new DDAuth(activity, builder.getDdAppId(), builder.getDdSecretId(), callback);
        ddAuth.login();
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
        if (ddAuth != null) {
            ddAuth.onReq(baseReq);
        }
    }

    @Override
    @Unsupported
    public void onDDAPIHandlerResp(com.android.dingtalk.share.ddsharemodule.message.BaseResp baseResp) {
        if (ddAuth != null) {
            ddAuth.onResp(baseResp);
        }
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
