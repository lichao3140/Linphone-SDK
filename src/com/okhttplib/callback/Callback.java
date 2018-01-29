package com.okhttplib.callback;

import com.okhttplib.HttpInfo;

import java.io.IOException;

/**
 * �첽����ص��ӿ�
 * @author zhousf
 */
public interface Callback extends BaseCallback{

    /**
     * ����ɹ����ûص��������л���UI�߳�
     */
    void onSuccess(HttpInfo info) throws IOException;

    /**
     * ����ʧ�ܣ��ûص��������л���UI�߳�
     */
    void onFailure(HttpInfo info) throws IOException;

}
