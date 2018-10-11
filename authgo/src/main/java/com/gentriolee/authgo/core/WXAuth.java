package com.gentriolee.authgo.core;

import android.annotation.SuppressLint;
import android.app.Activity;

import com.gentriolee.authgo.core.callback.SocialAuthCallback;
import com.gentriolee.authgo.core.callback.SocialLoginCallback;
import com.gentriolee.authgo.core.entities.AuthResult;
import com.gentriolee.authgo.core.entities.AuthResult;
import com.gentriolee.authgo.core.entities.WXUser;
import com.gentriolee.socialgo.core.WXSocial;
import com.gentriolee.socialgo.core.callback.SocialCallback;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

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

public class WXAuth extends WXSocial implements IAuth, IWXAPIEventHandler {

    private static final String BASE_URL = "https://api.weixin.qq.com/sns/";

    WXAuth(Activity activity, String appId, String secretId, SocialCallback callback) {
        super(activity, appId, secretId, callback);
    }

    @Override
    public void auth() {
        if (unInitInterrupt()) {
            return;
        }

        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = String.valueOf(System.currentTimeMillis());
        iwxapi.sendReq(req);
    }

    @Override
    public void login() {
        auth();
    }

    @Override
    public void onReq(BaseReq baseReq) {
        //nothing
    }

    @SuppressLint("CheckResult")
    @Override
    public void onResp(BaseResp baseResp) {
        if (baseResp.getType() == ConstantsAPI.COMMAND_SENDAUTH) {
            //授权
            if (baseResp.errCode == BaseResp.ErrCode.ERR_OK) {
                //授权码
                String code = ((SendAuth.Resp) baseResp).code;
                getAccessToken(code).subscribe(new Consumer<AuthResult>() {
                    @Override
                    public void accept(AuthResult authResult) throws Exception {
                        if (socialCallback instanceof SocialAuthCallback) {
                            ((SocialAuthCallback) socialCallback).success(authResult);
                        } else if (socialCallback instanceof SocialLoginCallback) {
                            fetchUserInfo(authResult, ((SocialLoginCallback) socialCallback));
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (socialCallback != null) {
                            socialCallback.fail(ErrCode.ERR_SDK_INTERNAL, throwable.getMessage());
                        }
                    }
                });
            } else {
                if (socialCallback != null) {
                    socialCallback.cancel();
                }
            }
        }
    }

    @SuppressLint("CheckResult")
    private void fetchUserInfo(final AuthResult authResult, final SocialLoginCallback callback) {
        Observable.create(new ObservableOnSubscribe<WXUser>() {
            @Override
            public void subscribe(ObservableEmitter<WXUser> emitter) throws Exception {
                Request request = new Request.Builder()
                        .url(userInfoUrl(authResult.getCode(), authResult.getOpenId())).build();
                try {
                    Response response = okHttpClient.newCall(request).execute();
                    if (response.isSuccessful()) {
                        WXUser user = WXUser.parse(response.body().string());
                        user.setAuthResult(authResult);
                        emitter.onNext(user);
                    }
                } catch (Exception e) {
                    emitter.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WXUser>() {
                    @Override
                    public void accept(WXUser wxUser) throws Exception {
                        callback.success(wxUser);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        socialCallback.fail(ErrCode.ERR_SDK_INTERNAL, throwable.getMessage());
                    }
                });
    }

    private Observable<AuthResult> getAccessToken(final String code) {
        return Observable.create(new ObservableOnSubscribe<AuthResult>() {
            @Override
            public void subscribe(ObservableEmitter<AuthResult> emitter) throws Exception {
                Request request = new Request.Builder()
                        .url(wxTokenUrl(code)).build();
                try {
                    Response response = okHttpClient.newCall(request).execute();
                    if (response.isSuccessful()) {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        String accessToken = jsonObject.getString("access_token");
                        String openid = jsonObject.getString("openid");
                        emitter.onNext(new AuthResult(openid, accessToken));
                    }
                } catch (Exception e) {
                    emitter.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    private String wxTokenUrl(String code) {
        return BASE_URL
                + "oauth2/access_token?appid="
                + appId
                + "&secret="
                + secretId
                + "&code="
                + code
                + "&grant_type=authorization_code";
    }

    private String userInfoUrl(String token, String openid) {
        return BASE_URL
                + "userinfo?access_token="
                + token
                + "&openid="
                + openid;
    }
}
