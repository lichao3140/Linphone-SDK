package com.lichao.lib.util;

public class Constant {
	public static final String SIP_SERVER_IP = "120.25.126.228";
	
	public static final int SERVER_PORT = 8080;
	
	public static final String IP_PORT = "http://" + SIP_SERVER_IP + ":" + SERVER_PORT;
	
	public static final String REG_SIP_URL = IP_PORT
			+ "/smarthomeservice/rest/user/sipRegister?";
	
	public static final String LOGIN_SIP_URL = IP_PORT
			+ "/smarthomeservice/rest/user/sipVerify?";
}
