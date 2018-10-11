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

    void auth();

    void login();

    interface ErrCode extends ISocial.ErrCode {

        //授权的ErrCode 从200开始
    }
}
