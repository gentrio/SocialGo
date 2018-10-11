package com.gentriolee.authgo.core;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;

import com.gentriolee.authgo.core.callback.SocialAuthCallback;
import com.gentriolee.authgo.core.callback.SocialLoginCallback;
import com.gentriolee.authgo.core.entities.AuthResult;
import com.gentriolee.authgo.core.entities.WBUser;
import com.gentriolee.socialgo.core.WBSocial;
import com.gentriolee.socialgo.core.callback.SocialCallback;
import com.sina.weibo.sdk.auth.AccessTokenKeeper;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WbAuthListener;
import com.sina.weibo.sdk.auth.WbConnectErrorMessage;
import com.sina.weibo.sdk.auth.sso.SsoHandler;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by gentriolee
 */

public class WBAuth extends WBSocial implements IAuth, WbAuthListener {

    private static final String BASE_URL = "https://api.weibo.com/2/users/show.json";

    WBAuth(Activity activity, String appId, String redirectUrl, SocialCallback callback) {
        super(activity, appId, redirectUrl, callback);
    }

    @Override
    public void auth() {
        if (unInitInterrupt()) {
            return;
        }

        ssoHandler = new SsoHandler(activity);
        ssoHandler.authorize(this);
    }

    @Override
    public void login() {
        auth();
    }

    @Override
    public void onSuccess(Oauth2AccessToken oauth2AccessToken) {
        if (oauth2AccessToken.isSessionValid()) {
            AccessTokenKeeper.writeAccessToken(activity, oauth2AccessToken);
            AuthResult authResult = new AuthResult(oauth2AccessToken.getUid(), oauth2AccessToken.getToken());
            if (socialCallback instanceof SocialAuthCallback) {
                ((SocialAuthCallback) socialCallback).success(authResult);
            } else if (socialCallback instanceof SocialLoginCallback) {
                fetchUserInfo(authResult, ((SocialLoginCallback) socialCallback));
            }
        } else {
            if (socialCallback != null) {
                socialCallback.fail(ErrCode.ERR_SDK_INTERNAL, "");
            }
        }
    }

    @SuppressLint("CheckResult")
    private void fetchUserInfo(final AuthResult authResult, final SocialLoginCallback callback) {
        Observable.create(new ObservableOnSubscribe<WBUser>() {
            @Override
            public void subscribe(ObservableEmitter<WBUser> emitter) throws Exception {
                Request request = new Request.Builder()
                        .url(userInfoUrl(authResult.getCode(), authResult.getOpenId())).build();
                try {
                    Response response = okHttpClient.newCall(request).execute();
                    if (response.isSuccessful()) {
                        WBUser user = WBUser.parse(response.body().string());
                        user.setAuthResult(authResult);
                        emitter.onNext(user);
                    }
                } catch (Exception e) {
                    emitter.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WBUser>() {
                    @Override
                    public void accept(WBUser wbUser) throws Exception {
                        callback.success(wbUser);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        socialCallback.fail(ErrCode.ERR_SDK_INTERNAL, throwable.getMessage());
                    }
                });
    }

    @Override
    public void cancel() {
        if (socialCallback != null) {
            socialCallback.cancel();
        }
    }

    @Override
    public void onFailure(WbConnectErrorMessage wbConnectErrorMessage) {
        if (socialCallback != null) {
            socialCallback.fail(ErrCode.ERR_SDK_INTERNAL, wbConnectErrorMessage.getErrorMessage());
        }
    }

    void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    private String userInfoUrl(String token, String openid) {
        return BASE_URL
                + "?access_token=" + token
                + "&uid=" + openid;
    }
}
