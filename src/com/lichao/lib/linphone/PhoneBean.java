package com.lichao.lib.linphone;

/**
 * sip �˺���Ϣ
 */

public class PhoneBean {
	/** �ǳ�  */
    private String displayName;
    /** �û���  */
    private String userName;
    /** ��������ַ  */
    private String host;
    /** ����  */
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