package com.okhttplib.callback;

import com.okhttplib.HttpInfo;

import java.io.IOException;

/**
 * �첽����ص��ӿ�
 * @author zhousf
 */
public interface CallbackOk  extends BaseCallback{
    /**
     * �ûص��������л���UI�߳�
     */
    void onResponse(HttpInfo info) throws IOException;
}
