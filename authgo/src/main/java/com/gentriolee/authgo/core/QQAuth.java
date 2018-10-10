package com.gentriolee.authgo.core;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;

import com.gentriolee.authgo.R;
import com.gentriolee.authgo.core.callback.SocialAuthCallback;
import com.gentriolee.authgo.core.callback.SocialLoginCallback;
import com.gentriolee.authgo.core.entities.BaseToken;
import com.gentriolee.authgo.core.entities.QQUser;
import com.gentriolee.socialgo.core.ISocial;
import com.gentriolee.socialgo.core.QQSocial;
import com.gentriolee.socialgo.core.callback.SocialCallback;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

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

public class QQAuth extends QQSocial implements IAuth, IUiListener {

    private static final String BASE_URL = "https://graph.qq.com/user/get_user_info";

    QQAuth(Activity activity, String appId) {
        super(activity, appId);
    }

    @Override
    public void auth(SocialCallback callback) {
        if (uninstallInterrupt(callback)) {
            return;
        }

        if (!tencent.isSessionValid()) {
            tencent.login(activity, "all", this);
        } else {
            if (socialCallback instanceof SocialAuthCallback) {
                ((SocialAuthCallback) socialCallback).success(tencent.getAccessToken());
            }
        }
    }

    @Override
    public void login(SocialCallback callback) {
        auth(callback);
    }

    @Override
    public void onComplete(Object obj) {
        try {
            JSONObject jsonObject = new JSONObject(obj.toString());
            String token = jsonObject.getString("access_token");
            String openid = jsonObject.getString("openid");
            if (socialCallback instanceof SocialAuthCallback) {
                ((SocialAuthCallback) socialCallback).success(token);
            } else if (socialCallback instanceof SocialLoginCallback) {
                BaseToken baseToken = new BaseToken(openid, token);
                fetchUserInfo(baseToken, ((SocialLoginCallback) socialCallback));
            }
        } catch (Exception e) {
            socialCallback.fail(ErrCode.ERR_SDK_INTERNAL, e.getMessage());
        }
    }

    @SuppressLint("CheckResult")
    private void fetchUserInfo(final BaseToken baseToken, final SocialLoginCallback callback) {
        Observable.create(new ObservableOnSubscribe<QQUser>() {
            @Override
            public void subscribe(ObservableEmitter<QQUser> emitter) throws Exception {
                Request request = new Request.Builder()
                        .url(userInfoUrl(baseToken.getAccess_token(), baseToken.getOpenId())).build();
                try {
                    Response response = okHttpClient.newCall(request).execute();
                    if (response.isSuccessful()) {
                        QQUser user = QQUser.parse(response.body().string());
                        user.setBaseToken(baseToken);
                        emitter.onNext(user);
                    }
                } catch (Exception e) {
                    emitter.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<QQUser>() {
                    @Override
                    public void accept(QQUser qqUser) throws Exception {
                        callback.success(qqUser);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        socialCallback.fail(ErrCode.ERR_SDK_INTERNAL, throwable.getMessage());
                    }
                });
    }

    @Override
    public void onError(UiError uiError) {
        if (socialCallback != null) {
            socialCallback.fail(ErrCode.ERR_SDK_INTERNAL, uiError.errorMessage);
        }
    }

    @Override
    public void onCancel() {
        if (socialCallback != null) {
            socialCallback.cancel();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Tencent.onActivityResultData(requestCode, resultCode, data, this);
    }

    private String userInfoUrl(String token, String openid) {
        return BASE_URL
                + "?access_token="
                + token
                + "&oauth_consumer_key="
                + appId
                + "&openid="
                + openid;
    }
}

