package com.okhttplib.interceptor;

import com.okhttplib.HttpInfo;

/**
 * ������·�쳣����ҵ���߼���������
 * @author zhousf
 */
public interface ExceptionInterceptor {

    HttpInfo intercept(HttpInfo info) throws Exception;

}
