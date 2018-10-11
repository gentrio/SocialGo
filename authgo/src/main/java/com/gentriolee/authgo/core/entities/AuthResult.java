package com.gentriolee.authgo.core.entities;

/**
 * Created by gentriolee
 */

public class AuthResult {

    private String openId;

    private String code;

    public AuthResult(String openId, String code) {
        this.openId = openId;
        this.code = code;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "AuthResult{" +
                "openId='" + openId + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
