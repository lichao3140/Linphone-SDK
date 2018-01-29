package com.okhttplib;

import com.okhttplib.callback.BaseCallback;
import com.okhttplib.callback.ProgressCallback;

import okhttp3.OkHttpClient;

/**
 * �������󹤾߽ӿ�
 * @author zhousf
 */
public interface OkHttpUtilInterface {


    /**
     * ͬ��Post����
     * @param info ������Ϣ��
     * @return HttpInfo
     */
    HttpInfo doPostSync(HttpInfo info);

    /**
     * ͬ��Post����
     * @param info ������Ϣ��
     * @param callback ���Ȼص��ӿ�
     * @return HttpInfo
     */
    HttpInfo doPostSync(HttpInfo info, ProgressCallback callback);

    /**
     * �첽Post����
     * @param info ������Ϣ��
     * @param callback ����ص��ӿ�
     */
    void doPostAsync(HttpInfo info, BaseCallback callback);

    /**
     * �첽Post����
     * @param info ������Ϣ��
     * @param callback ���Ȼص��ӿ�
     */
    void doPostAsync(HttpInfo info, ProgressCallback callback);

    /**
     * ͬ��Get����
     * @param info ������Ϣ��
     */
    HttpInfo doGetSync(HttpInfo info);

    /**
     * �첽Get����
     * @param info ������Ϣ��
     * @param callback ����ص��ӿ�
     */
    void doGetAsync(HttpInfo info, BaseCallback callback);

    /**
     * �첽�ϴ��ļ�
     * @param info ������Ϣ��
     */
    void doUploadFileAsync(final HttpInfo info);

    /**
     * �����첽�ϴ��ļ�
     * @param info ������Ϣ��
     * @param callback ���Ȼص��ӿ�
     */
    void doUploadFileAsync(final HttpInfo info, ProgressCallback callback);

    /**
     * ͬ���ϴ��ļ�
     * @param info ������Ϣ��
     */
    void doUploadFileSync(final HttpInfo info);

    /**
     * ����ͬ���ϴ��ļ�
     * @param info ������Ϣ��
     * @param callback ���Ȼص��ӿ�
     */
    void doUploadFileSync(final HttpInfo info, ProgressCallback callback);

    /**
     * �첽�����ļ�
     * @param info ������Ϣ��
     */
    void doDownloadFileAsync(final HttpInfo info);


    /**
     * ͬ�������ļ�
     * @param info ������Ϣ��
     */
    void doDownloadFileSync(final HttpInfo info);


    /**
     * ȡ������
     * @param requestTag �����ʶ
     */
    void cancelRequest(Object requestTag);


    /**
     * ��ȡĬ�ϵ�HttpClient
     */
    OkHttpClient getDefaultClient();

}
