# 第三方社会化工具业务库（第三方分享&第三方登录）

## 工程结构
* socialgo 第三方社会化库的配置中心
* sharego 分享核心
* authgo 授权登录核心
* sample 例子

## 初始化

### AndroidManifest.xml配置

```xml
<!--qq配置开始-->
<activity
    android:name="com.tencent.tauth.AuthActivity"
    android:launchMode="singleTask"
    android:noHistory="true">
    <intent-filter>
        <action android:name="android.intent.action.VIEW"/>

        <category android:name="android.intent.category.DEFAULT"/>
        <category android:name="android.intent.category.BROWSABLE"/>
        <!-- tencent + qqId -->
        <data android:scheme="tencent*********"/>
    </intent-filter>
</activity>
<activity
    android:name="com.tencent.connect.common.AssistActivity"
    android:configChanges="orientation|keyboardHidden|screenSize"
    android:theme="@android:style/Theme.Translucent.NoTitleBar"/>
<!--qq配置结束-->

<!-- 钉钉分享授权配置开始（该Activity自行创建放在当前工程的包下） -->
<activity
    android:name=".ddshare.DDShareActivity"
    android:exported="true"
    android:screenOrientation="portrait"
    android:theme="@android:style/Theme.Translucent.NoTitleBar" />
<!-- 钉钉分享授权配置结束 -->


<!-- 微信分享授权配置开始（该Activity自行创建放在当前工程的包下） -->
<activity
    android:name=".wxapi.WXEntryActivity"
    android:configChanges="keyboardHidden|orientation|screenSize"
    android:exported="true"
    android:launchMode="singleTask"
    android:screenOrientation="portrait"
    android:theme="@android:style/Theme.Translucent.NoTitleBar" />
<!-- 微信分享授权配置结束 --> 
```

### 工具类初始化

推荐在Application生命周期中初始化

```java
SocialConfig.Builder builder = new SocialConfig.Builder()
    .setQqAppId("")
    .setWxAppId("")
    .setWbAppId("")
    .setWbRedirectUrl("")
    .setDdAppId("")
    .build();
SocialConfig.init(getApplicationContext(), builder);
```

### 回调配置

#### 钉钉的回调在packageName.ddshare.DDShareActivity中配置

```java
@Override
public void onReq(BaseReq baseReq) {
    SocialGo.onDDAPIHandlerReq(baseReq);
}

@Override
public void onResp(BaseResp baseResp) {
    SocialGo.onDDAPIHandlerResp(baseResp);
    finish();
}
```

#### 微信的回调在packageName.wxapi.WXEntryActivity

```java
@Override
public void onReq(BaseReq baseReq) {
    SocialGo.onWXAPIHandlerReq(baseReq);
}

@Override
public void onResp(BaseResp baseResp) {
    SocialGo.onWXAPIHandlerResp(baseResp);
    finish();
}
```

#### QQ回调在调用的Activity中重写onActivityResult()

```java
@Override
protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    SocialGo.onActivityResult(requestCode, resultCode, data);
    super.onActivityResult(requestCode, resultCode, data);
}
```

#### 微博回调在调用的Activity中重写onActivityResult()和onNewIntent()

```java
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
```

## 调用

### 分享

```java
ShareGo.getInstance().shareWX()；
ShareGo.getInstance().launchWX();
ShareGo.getInstance().shareQQ();
ShareGo.getInstance().shareWB();
ShareGo.getInstance().shareDD();
```
### 授权登录

```java
AuthGo.getInstance().authWX();
AuthGo.getInstance().authQQ();
AuthGo.getInstance().authWB();
```

