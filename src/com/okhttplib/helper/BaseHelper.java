package com.okhttplib.helper;

import android.util.Log;

import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.CookieJar;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;

/**
 * ҵ��������ࣺ��־�������ӡ��Https��֤
 * @author zhousf
 */
abstract class BaseHelper {

    OkHttpClient httpClient;
    protected String TAG;
    protected String timeStamp;
    protected boolean showHttpLog;
    protected String requestTag;//�����ʶ

    BaseHelper() {
    }

    BaseHelper(HelperInfo helperInfo) {
        TAG = helperInfo.getLogTAG();
        timeStamp = helperInfo.getTimeStamp();
        showHttpLog = helperInfo.isShowHttpLog();
        requestTag = helperInfo.getRequestTag();
        //�Ƿ����Ĭ�ϵĿͻ��˽�������
        OkHttpClient defaultClient = helperInfo.getOkHttpUtil().getDefaultClient();
        if(helperInfo.isDefault()){
           if(null == defaultClient){
               httpClient = initHttpClient(helperInfo,null);
               helperInfo.getOkHttpUtil().setDefaultClient(httpClient);
           }else{
               httpClient = initHttpClient(helperInfo,defaultClient.cookieJar());
           }
        }else{
            httpClient = initHttpClient(helperInfo,null);
        }
    }

    private OkHttpClient initHttpClient(HelperInfo helperInfo, CookieJar cookieJar){
        OkHttpClient.Builder clientBuilder = helperInfo.getClientBuilder();
        clientBuilder.protocols(Arrays.asList(Protocol.SPDY_3, Protocol.HTTP_1_1));
//        if(showHttpLog){
//            clientBuilder.addInterceptor(LOG_INTERCEPTOR);
//        }
        if(null != cookieJar)
            clientBuilder.cookieJar(cookieJar);
        setSslSocketFactory(clientBuilder);
        return clientBuilder.build();
    }


    /**
     * ��־������
     */
    private final Interceptor LOG_INTERCEPTOR = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request();
            long startTime = System.currentTimeMillis();
            showLog(String.format("%s-URL: %s %n",chain.request().method(),
                    chain.request().url()));
            Response res = chain.proceed(originalRequest);
            long endTime = System.currentTimeMillis();
            showLog(String.format("CostTime: %.1fs", (endTime-startTime) / 1000f));
            return res;
        }
    };

    /**
     * ����HTTPS��֤
     */
    private void setSslSocketFactory(OkHttpClient.Builder clientBuilder){
        clientBuilder.hostnameVerifier(DO_NOT_VERIFY);
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            X509TrustManager trustManager = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }
                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            };
            sc.init(null,new TrustManager[]{trustManager}, new SecureRandom());
            clientBuilder.sslSocketFactory(sc.getSocketFactory(),trustManager);
        } catch (Exception e) {
            showLog("Https��֤�쳣: "+e.getMessage());
        }
    }

    /**
     *��������֤
     */
    private final HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };


    /**
     * ��ӡ��־
     * @param msg ��־��Ϣ
     */
    void showLog(String msg){
        if(showHttpLog)
            Log.d(TAG+"["+timeStamp+"]", msg);
    }


}
