package com.lichao.lib.bean;

import java.io.Serializable;

public class UserInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int id;
	
	private String username;
	
	private String password;
	
	private String type;
	
	private int status;
	
	private String mobile;
	
	private String sipid;
	
	private String sipPasswd;
	
	private String email;
	
	private long createTime;
	
	private String updateTime;
	
	private String remark;
	
	private String sessionKey;
	
	public UserInfo() {
		
	}

	public UserInfo(int id, String username, String password, String type,
			int status, String mobile, String email, long createTime,
			String updateTime, String remark, String sessionKey, String sipid,
			String sipPasswd) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.type = type;
		this.status = status;
		this.mobile = mobile;
		this.email = email;
		this.createTime = createTime;
		this.updateTime = updateTime;
		this.remark = remark;
		this.sessionKey = sessionKey;
		this.sipid = sipid;
		this.sipPasswd = sipPasswd;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}

	public String getSipid() {
		return sipid;
	}

	public void setSipid(String sipid) {
		this.sipid = sipid;
	}

	public String getSipPasswd() {
		return sipPasswd;
	}

	public void setSipPasswd(String sipPasswd) {
		this.sipPasswd = sipPasswd;
	}

	@Override
	public String toString() {
		return "UserInfo [id=" + id + ", username=" + username + ", password="
				+ password + ", type=" + type + ", status=" + status
				+ ", mobile=" + mobile + ", email=" + email + ", createTime="
				+ createTime + ", updateTime=" + updateTime + ", remark="
				+ remark + ", sessionKey=" + sessionKey + ", sipid=" + sipid
				+ ", sipPasswd=" + sipPasswd + "]";
	}
	
}