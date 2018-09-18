package com.gentriolee.sample;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.gentriolee.sharego.core.ShareGo;
import com.gentriolee.sharego.core.callback.SocialShareCallback;
import com.gentriolee.sharego.core.entities.DDShareEntity;
import com.gentriolee.sharego.core.entities.QQShareEntity;
import com.gentriolee.sharego.core.entities.ShareEntity;
import com.gentriolee.sharego.core.entities.WBShareEntity;
import com.gentriolee.sharego.core.entities.WXShareEntity;
import com.gentriolee.socialgo.core.SocialGo;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Social_Sample";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        ShareGo shareGo = ShareGo.getInstance();

        findViewById(R.id.wechat).setOnClickListener(v -> {
            ShareEntity shareEntity = WXShareEntity.createImageInfo(false, bitmap);
            shareGo.shareWX(this, shareEntity, socialShareCallback);
        });

        findViewById(R.id.friend_circle).setOnClickListener(v -> {
            ShareEntity shareEntity = WXShareEntity.createImageInfo(true, bitmap);
            shareGo.shareWX(this, shareEntity, socialShareCallback);
        });

        findViewById(R.id.qq).setOnClickListener(v -> {
            ShareEntity shareEntity = QQShareEntity.createImageInfo(bitmap);
            shareGo.shareQQ(this, shareEntity, socialShareCallback);
        });

        findViewById(R.id.qzone).setOnClickListener(v -> {
            ShareEntity shareEntity = QQShareEntity.createImageInfoToQZone(bitmap);
            shareGo.shareQQ(this, shareEntity, socialShareCallback);
        });

        findViewById(R.id.weibo).setOnClickListener(v -> {
            ShareEntity shareEntity = WBShareEntity.createImageTextInfo(bitmap, "");
            shareGo.shareWB(this, shareEntity, socialShareCallback);
        });

        findViewById(R.id.ding_talk).setOnClickListener(v -> {
            ShareEntity shareEntity = DDShareEntity.createImageInfo(bitmap);
            shareGo.shareDD(this, shareEntity, socialShareCallback);
        });
    }

    private SocialShareCallback socialShareCallback = new SocialShareCallback() {
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
