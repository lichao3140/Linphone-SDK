package com.lichao.lib.linphone;

/**
 * sip 账号信息
 */

public class PhoneBean {
	/** 昵称  */
    private String displayName;
    /** 用户名  */
    private String userName;
    /** 服务器地址  */
    private String host;
    /** 密码  */
    private String password;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}