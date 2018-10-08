package com.gentriolee.sample;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.gentriolee.sharego.core.ShareGo;
import com.gentriolee.sharego.core.callback.SocialShareCallback;
import com.gentriolee.sharego.core.entities.ShareEntity;
import com.gentriolee.sharego.core.entities.WXShareEntity;
import com.gentriolee.socialgo.core.SocialGo;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Social_Sample";

    private static final String TITLE = "title";

    private static final String DESC = "DESC";

    private static final String LINK = "https://www.gentriolee.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //两种使用方法 本质上没有差别 只是从理解上更直接
        shareOneMethod();
        shareTwoMethod();
    }

    /**
     * 分享类型和分享方式固定 可使用该方式
     */
    private void shareOneMethod() {
        findViewById(R.id.btn_wechat_share_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    ShareGo.getInstance().shareWX(MainActivity.this, WXShareEntity.createTextInfo(TITLE), mSocialShareCallback);
                }catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Sample 未提供第三方分享id进行测试", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 分享类型和分享方式 动态改变 可使用该方式
     */
    private void shareTwoMethod() {
        RadioGroup shareTypeRg = findViewById(R.id.rg_type);
        RadioGroup shareTargetRg = findViewById(R.id.rg_target);
        findViewById(R.id.btn_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    Toast.makeText(MainActivity.this, "Sample 未提供第三方分享id进行测试", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private SocialShareCallback mSocialShareCallback = new SocialShareCallback() {
        @Override
        public void shareSuccess() {
            Log.d(TAG, "success:" + String.valueOf(getShareType()));
        }

        @Override
        public void shareFail(int errorCode, String defaultMsg) {
            Log.d(TAG, "fail:" + String.valueOf(getShareType()) + " errCode:" + errorCode + " defaultMsg:" + defaultMsg);
        }

        @Override
        public void shareCancel() {
            Log.d(TAG, "cancel:" + String.valueOf(getShareType()));
        }
    };

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
