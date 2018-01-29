package com.okhttplib.interceptor;

import com.okhttplib.HttpInfo;

/**
 * ÇëÇó½á¹ûÀ¹½ØÆ÷
 * @author zhousf
 */
public interface ResultInterceptor {

    HttpInfo intercept(HttpInfo info) throws Exception;

}
