package com.gentriolee.sharego.core.entities;

/**
 * Created by gentriolee
 */

public class WXLaunchEntity {

    private boolean isRelease = true;

    private String userName;

    private String path;

    public boolean isRelease() {
        return isRelease;
    }

    public void setRelease(boolean release) {
        isRelease = release;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
