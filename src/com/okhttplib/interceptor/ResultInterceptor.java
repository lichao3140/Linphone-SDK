package com.okhttplib.interceptor;

import com.okhttplib.HttpInfo;

/**
 * ������������
 * @author zhousf
 */
public interface ResultInterceptor {

    HttpInfo intercept(HttpInfo info) throws Exception;

}
