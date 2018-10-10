package com.gentriolee.authgo.core;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;

import com.android.dingtalk.share.ddsharemodule.IDDAPIEventHandler;
import com.android.dingtalk.share.ddsharemodule.ShareConstant;
import com.android.dingtalk.share.ddsharemodule.message.BaseReq;
import com.android.dingtalk.share.ddsharemodule.message.BaseResp;
import com.android.dingtalk.share.ddsharemodule.message.SendAuth;
import com.gentriolee.authgo.core.callback.SocialAuthCallback;
import com.gentriolee.authgo.core.callback.SocialLoginCallback;
import com.gentriolee.authgo.core.entities.BaseToken;
import com.gentriolee.authgo.core.entities.WXUser;
import com.gentriolee.socialgo.annotation.Unsupported;
import com.gentriolee.socialgo.core.DDSocial;
import com.gentriolee.socialgo.core.callback.SocialCallback;

import org.json.JSONObject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by gentriolee
 */

@Unsupported
public class DDAuth extends DDSocial implements IAuth, IDDAPIEventHandler {

    private static final String BASE_URL = "https://oapi.dingtalk.com/sns/";

    DDAuth(Activity activity, String appId, String secretId) {
        super(activity, appId, secretId);
    }


    @Override
    public void auth(SocialCallback callback) {
        if (uninstallInterrupt(callback)) {
            return;
        }

        SendAuth.Req req = new SendAuth.Req();
        req.scope = SendAuth.Req.SNS_LOGIN;
        req.state = String.valueOf(System.currentTimeMillis());
        iddShareApi.sendReq(req);
    }

    @Override
    public void login(SocialCallback callback) {
        auth(callback);
    }

    @Override
    public void onReq(BaseReq baseReq) {
        //nothing
    }

    @SuppressLint("CheckResult")
    @Override
    public void onResp(BaseResp baseResp) {
        if (baseResp.getType() == ShareConstant.COMMAND_SENDAUTH_V2) {
            if (baseResp.mErrCode == BaseResp.ErrCode.ERR_OK) {
                String code = ((SendAuth.Resp) baseResp).code;
                getAccessToken(code).subscribe(new Consumer<BaseToken>() {
                    @Override
                    public void accept(BaseToken baseToken) throws Exception {
                        if (socialCallback instanceof SocialAuthCallback) {
                            ((SocialAuthCallback) socialCallback).success(baseToken.getAccess_token());
                        } else if (socialCallback instanceof SocialLoginCallback) {
                            fetchUserInfo(baseToken, ((SocialLoginCallback) socialCallback));
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

    private Observable<BaseToken> getAccessToken(final String code) {
        return Observable.create(new ObservableOnSubscribe<BaseToken>() {
            @Override
            public void subscribe(ObservableEmitter<BaseToken> emitter) throws Exception {
                Request request = new Request.Builder()
                        .url(ddTokenUrl()).build();
                try {
                    Response response = okHttpClient.newCall(request).execute();
                    if (response.isSuccessful()) {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        String accessToken = jsonObject.getString("access_token");

                        RequestBody body1 = new FormBody.Builder()
                                .add("tmp_auth_code", code).build();
                        Request request1 = new Request.Builder().url(ddTempAuthCodeUrl(accessToken)).post(body1).build();
                        Response response1 = okHttpClient.newCall(request1).execute();
                        if (response1.isSuccessful()) {
                            JSONObject jsonObject1 = new JSONObject(response.body().string());
                            String openid = jsonObject1.getString("openid");
                            String persistentCode = jsonObject1.getString("persistent_code");

                            // TODO: 2018/10/10  https://open-doc.dingtalk.com/microapp/native/ddvlch
                        }

//                        emitter.onNext(new BaseToken("", accessToken));
                    }
                } catch (Exception e) {
                    emitter.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    @SuppressLint("CheckResult")
    private void fetchUserInfo(final BaseToken baseToken, final SocialLoginCallback callback) {
        Observable.create(new ObservableOnSubscribe<WXUser>() {
            @Override
            public void subscribe(ObservableEmitter<WXUser> emitter) throws Exception {
                Request request = new Request.Builder()
                        .url(userInfoUrl(baseToken.getAccess_token(), baseToken.getOpenId())).build();
                try {
                    Response response = okHttpClient.newCall(request).execute();
                    if (response.isSuccessful()) {
                        WXUser user = WXUser.parse(response.body().string());
                        user.setBaseToken(baseToken);
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

    private String ddTokenUrl() {
        return BASE_URL
                + "gettoken?appid="
                + appId
                + "&appsecret="
                + secretId;
    }

    private String ddTempAuthCodeUrl(String token) {
        return BASE_URL
                + "get_persistent_code?access_token="
                + token;
    }

    private String ddSNSTokenUrl(String token) {
        return BASE_URL
                + "get_sns_token?access_token="
                + token;
    }

    private String userInfoUrl(String token, String openid) {
        return BASE_URL
                + "userinfo?access_token="
                + token
                + "&openid="
                + openid;
    }
}

