package com.gentriolee.sample;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.gentriolee.authgo.core.AuthGo;
import com.gentriolee.authgo.core.callback.SocialAuthCallback;
import com.gentriolee.authgo.core.callback.SocialLoginCallback;
import com.gentriolee.authgo.core.entities.AuthResult;
import com.gentriolee.authgo.core.entities.BaseUser;
import com.gentriolee.sharego.core.ShareGo;
import com.gentriolee.sharego.core.callback.SocialShareCallback;
import com.gentriolee.sharego.core.entities.ShareEntity;
import com.gentriolee.sharego.core.entities.WXShareEntity;
import com.gentriolee.socialgo.core.SocialGo;

public class MainActivity extends AppCompatActivity {

    private static final String TITLE = "title";

    private static final String DESC = "DESC";

    private static final String LINK = "https://www.gentriolee.com";

    private TextView mUserInfoTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //两种使用方法 本质上没有差别 只是从理解上更直接
        shareOneMethod();
        shareTwoMethod();

        //授权
        authMethod();
        //登录
        loginMethod();
    }

    /**
     * 授权
     */
    private void authMethod() {
        findViewById(R.id.btn_wechat_auth).setOnClickListener(v -> {
            AuthGo.getInstance().authWX(this, mSocialAuthCallback);
        });
        findViewById(R.id.btn_qq_auth).setOnClickListener(v -> {
            AuthGo.getInstance().authQQ(this, mSocialAuthCallback);
        });
        findViewById(R.id.btn_weibo_auth).setOnClickListener(v -> {
            AuthGo.getInstance().authWB(this, mSocialAuthCallback);
        });
        findViewById(R.id.btn_ding_talk_auth).setOnClickListener(v -> {

        });
    }

    /**
     * 获取信息
     */
    private void loginMethod() {
        mUserInfoTv = findViewById(R.id.tv_user_info);
        findViewById(R.id.btn_wechat_login).setOnClickListener(v -> {
            AuthGo.getInstance().loginWX(this, mSocialLoginCallback);
        });
        findViewById(R.id.btn_qq_login).setOnClickListener(v -> {
            AuthGo.getInstance().loginQQ(this, mSocialLoginCallback);
        });
        findViewById(R.id.btn_weibo_login).setOnClickListener(v -> {
            AuthGo.getInstance().loginWB(this, mSocialLoginCallback);
        });
        findViewById(R.id.btn_ding_talk_login).setOnClickListener(v -> {

        });
    }

    /**
     * 分享类型和分享方式固定 可使用该方式
     */
    private void shareOneMethod() {
        findViewById(R.id.btn_wechat_share_text).setOnClickListener(v -> {
            try{
                ShareGo.getInstance().shareWX(MainActivity.this, WXShareEntity.createTextInfo(TITLE), mSocialShareCallback);
            }catch (Exception e) {
                String msg = "Sample 未提供第三方分享id进行测试";
                toast(msg);
            }
        });
    }

    /**
     * 分享类型和分享方式 动态改变 可使用该方式
     */
    private void shareTwoMethod() {
        RadioGroup shareTypeRg = findViewById(R.id.rg_type);
        RadioGroup shareTargetRg = findViewById(R.id.rg_target);
        findViewById(R.id.btn_share).setOnClickListener(v -> {
            int target = ShareGo.TARGET_WX;
            switch (shareTargetRg.getCheckedRadioButtonId()) {
                case R.id.rb_wechat:
                    target = ShareGo.TARGET_WX;
                    break;
                case R.id.rb_friend_circle:
                    target = ShareGo.TARGET_WX_TIMELINE;
                    break;
                case R.id.rb_qq:
                    target = ShareGo.TARGET_QQ;
                    break;
                case R.id.rb_qq_zone:
                    target = ShareGo.TARGET_QQ_ZONE;
                    break;
                case R.id.rb_weibo:
                    target = ShareGo.TARGET_WB;
                    break;
                case R.id.rb_ding_talk:
                    target = ShareGo.TARGET_DD;
                    break;
            }

            ShareEntity shareEntity = null;
            switch (shareTypeRg.getCheckedRadioButtonId()) {
                case R.id.rb_text:
                    shareEntity = ShareEntity.createTextInfo(target, TITLE);
                    break;
                case R.id.rb_img:
                    shareEntity = ShareEntity.createImageInfo(target, BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
                    break;
                case R.id.rb_link:
                    shareEntity = ShareEntity.createWebInfo(target, TITLE, DESC, LINK, BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
                    break;
                case R.id.rb_mini_app:
                    if (target == ShareGo.TARGET_WX) {
                        shareEntity = WXShareEntity.createMiniAppInfo(true, "", "", "", "", "", null);
                    }
                    break;
            }
            try{
                ShareGo.getInstance().share(MainActivity.this, shareEntity, mSocialShareCallback);
            }catch (Exception e) {
                String msg = "Sample 未提供第三方分享id进行测试";
                toast(msg);
            }
        });
    }

    private SocialShareCallback mSocialShareCallback = new SocialShareCallback() {
        @Override
        public void success() {
            String msg = "success:" + getTarget();
            toast(msg);
        }

        @Override
        public void fail(int errorCode, String defaultMsg) {
            String msg = "fail:" + getTarget() + " errCode:" + errorCode + " defaultMsg:" + defaultMsg;
            toast(msg);
        }

        @Override
        public void cancel() {
            String msg = "cancel:" + getTarget();
            toast(msg);
        }
    };

    private SocialAuthCallback mSocialAuthCallback = new SocialAuthCallback() {
        @Override
        public void success(AuthResult authResult) {
            String msg = "success:" + getTarget() + " code:" + authResult.getCode();
            toast(msg);
        }

        @Override
        public void fail(int errorCode, String defaultMsg) {
            String msg = "fail:" + getTarget() + " errCode:" + errorCode + " defaultMsg:" + defaultMsg;
            toast(msg);
        }

        @Override
        public void cancel() {
            String msg = "cancel:" + getTarget();
            toast(msg);
        }
    };

    private SocialLoginCallback mSocialLoginCallback = new SocialLoginCallback() {
        @Override
        public void success(BaseUser baseUser) {
            mUserInfoTv.setText(baseUser.toString());
        }

        @Override
        public void fail(int errorCode, String defaultMsg) {
            String msg = "fail:" + getTarget() + " errCode:" + errorCode + " defaultMsg:" + defaultMsg;
            toast(msg);
        }

        @Override
        public void cancel() {
            String msg = "cancel:" + getTarget();
            toast(msg);
        }
    };

    private void toast(String msg) {
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        SocialGo.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        SocialGo.onNewIntent(intent);
        super.onNewIntent(intent);
    }
}
