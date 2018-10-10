package com.gentriolee.authgo.core;

import com.gentriolee.socialgo.core.ISocial;
import com.gentriolee.socialgo.core.callback.SocialCallback;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by gentriolee
 */

public interface IAuth {

    OkHttpClient okHttpClient = new OkHttpClient.Builder().retryOnConnectionFailure(true)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS).build();

    void auth(SocialCallback callback);

    void login(SocialCallback callback);

    interface ErrCode extends ISocial.ErrCode {


    }
}
