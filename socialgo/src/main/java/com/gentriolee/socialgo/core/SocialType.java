package com.gentriolee.socialgo.core;

/**
 * Create by gentriolee
 */

public class SocialType {

    /**
     * 微信 = 1
     * 朋友圈 = 1 << 1
     * QQ = 1 << 2
     * QQ空间 = 1 << 3
     * 微博 = 1 << 4
     * 钉钉 = 1 << 5
     */

    public static final int TYPE_WX = 1;
    public static final int TYPE_WX_TIMELINE = 1 << 1;
    public static final int TYPE_QQ = 1 << 2;
    public static final int TYPE_QQ_ZONE = 1 << 3;
    public static final int TYPE_WB = 1 << 4;
    public static final int TYPE_DD = 1 << 5;
}
