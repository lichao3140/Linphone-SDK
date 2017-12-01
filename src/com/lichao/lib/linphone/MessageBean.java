package com.lichao.lib.linphone;

public class MessageBean {
	private String cmd;
	private String result;
	
	public MessageBean(String cmd, String result) {
		super();
		this.cmd = cmd;
		this.result = result;
	}

	public String getCmd() {
		return cmd;
	}
	
	public void setCmd(String cmd) {
		this.cmd = cmd;
	}
	
	public String getResult() {
		return result;
	}
	
	public void setResult(String result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return "MessageBean [cmd=" + cmd + ", result=" + result + "]";
	}
	
}
