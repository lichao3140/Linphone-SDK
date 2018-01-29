package com.okhttplib.helper;

import com.okhttplib.OkHttpUtil;
import com.okhttplib.annotation.Encoding;
import com.okhttplib.interceptor.ExceptionInterceptor;
import com.okhttplib.interceptor.ResultInterceptor;

import java.util.List;

import okhttp3.OkHttpClient;

/**
 * ҵ������Ϣ��
 * @author zhousf
 */
public class HelperInfo {

    private String LogTAG;//��ӡ��־��ʶ
    private String timeStamp;//ʱ���
    private boolean showHttpLog;//�Ƿ���ʾHttp������־
    private OkHttpUtil okHttpUtil;
    private boolean isDefault;//�Ƿ�Ĭ������
    private OkHttpClient.Builder clientBuilder;
    private String requestTag;//�����ʶ
    private List<ResultInterceptor> resultInterceptors;//������������
    private List<ExceptionInterceptor> exceptionInterceptors;//������·�쳣������
    private String downloadFileDir;//�����ļ�����Ŀ¼
    private @Encoding String responseEncoding;//��������Ӧ����


    public String getLogTAG() {
        return LogTAG;
    }

    public void setLogTAG(String logTAG) {
        LogTAG = logTAG;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public boolean isShowHttpLog() {
        return showHttpLog;
    }

    public void setShowHttpLog(boolean showHttpLog) {
        this.showHttpLog = showHttpLog;
    }

    public String getRequestTag() {
        return requestTag;
    }

    public void setRequestTag(String requestTag) {
        this.requestTag = requestTag;
    }

    public List<ResultInterceptor> getResultInterceptors() {
        return resultInterceptors;
    }

    public void setResultInterceptors(List<ResultInterceptor> resultInterceptors) {
        this.resultInterceptors = resultInterceptors;
    }

    public List<ExceptionInterceptor> getExceptionInterceptors() {
        return exceptionInterceptors;
    }

    public void setExceptionInterceptors(List<ExceptionInterceptor> exceptionInterceptors) {
        this.exceptionInterceptors = exceptionInterceptors;
    }

    public String getDownloadFileDir() {
        return downloadFileDir;
    }

    public void setDownloadFileDir(String downloadFileDir) {
        this.downloadFileDir = downloadFileDir;
    }

    public OkHttpClient.Builder getClientBuilder() {
        return clientBuilder;
    }

    public void setClientBuilder(OkHttpClient.Builder clientBuilder) {
        this.clientBuilder = clientBuilder;
    }

    public OkHttpUtil getOkHttpUtil() {
        return okHttpUtil;
    }

    public void setOkHttpUtil(OkHttpUtil okHttpUtil) {
        this.okHttpUtil = okHttpUtil;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public String getResponseEncoding() {
        return responseEncoding;
    }

    public void setResponseEncoding(@Encoding String responseEncoding) {
        this.responseEncoding = responseEncoding;
    }
}
