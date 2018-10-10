package com.gentriolee.authgo.core.entities;

/**
 * Created by gentriolee
 */

public class BaseToken {

    private String openId;

    private String access_token;

    public BaseToken(String openId, String access_token) {
        this.openId = openId;
        this.access_token = access_token;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    @Override
    public String toString() {
        return "BaseToken{" +
                "openId='" + openId + '\'' +
                ", access_token='" + access_token + '\'' +
                '}';
    }
}
