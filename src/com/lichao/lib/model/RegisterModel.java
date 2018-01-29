package com.lichao.lib.model;

import java.io.Serializable;

import com.lichao.lib.bean.UserInfo;

public class RegisterModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private int code;

	private String message;
	
	private UserInfo data;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public UserInfo getUserInfo() {
		return data;
	}

	public void setUserInfo(UserInfo data) {
		this.data = data;
	}
	
}
