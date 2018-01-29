package com.okhttplib.helper;

import android.os.Build;
import android.os.Message;
import android.os.NetworkOnMainThreadException;
import android.text.TextUtils;

import com.okhttplib.HttpInfo;
import com.okhttplib.annotation.BusinessType;
import com.okhttplib.annotation.RequestMethod;
import com.okhttplib.bean.CallbackMessage;
import com.okhttplib.bean.DownloadMessage;
import com.okhttplib.bean.UploadMessage;
import com.okhttplib.callback.BaseActivityLifecycleCallbacks;
import com.okhttplib.callback.BaseCallback;
import com.okhttplib.callback.ProgressCallback;
import com.okhttplib.handler.OkMainHandler;
import com.okhttplib.interceptor.ExceptionInterceptor;
import com.okhttplib.interceptor.ResultInterceptor;
import com.okhttplib.progress.ProgressRequestBody;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Http��������ҵ����
 * @author zhousf
 */
class HttpHelper extends BaseHelper{

    private List<ResultInterceptor> resultInterceptors;//������������
    private List<ExceptionInterceptor> exceptionInterceptors;//������·�쳣������
    private long startTime;

    HttpHelper(HelperInfo helperInfo) {
        super(helperInfo);
        resultInterceptors = helperInfo.getResultInterceptors();
        exceptionInterceptors = helperInfo.getExceptionInterceptors();
    }

    /**
     * ͬ������
     */
    HttpInfo doRequestSync(OkHttpHelper helper){
        Call call = null;
        final HttpInfo info = helper.getHttpInfo();
        Request request = helper.getRequest();
        String url = info.getUrl();
        if(!checkUrl(url)){
            return retInfo(info,HttpInfo.CheckURL);
        }
        request = request == null ? buildRequest(info,helper.getRequestMethod(),helper.getProgressCallback()) : request;
        showUrlLog(request);
        helper.setRequest(request);
        OkHttpClient httpClient = helper.getHttpClient();
        try {
            httpClient = httpClient == null ? super.httpClient : httpClient;
            call = httpClient.newCall(request);
            BaseActivityLifecycleCallbacks.putCall(requestTag,call);
            Response res = call.execute();
            return dealResponse(helper, res, call);
        } catch (IllegalArgumentException e){
            return retInfo(info,HttpInfo.ProtocolException);
        } catch (SocketTimeoutException e){
            if(null != e.getMessage()){
                if(e.getMessage().contains("failed to connect to"))
                    return retInfo(info,HttpInfo.ConnectionTimeOut);
                if(e.getMessage().equals("timeout"))
                    return retInfo(info,HttpInfo.WriteAndReadTimeOut);
            }
            return retInfo(info,HttpInfo.WriteAndReadTimeOut);
        } catch (UnknownHostException e) {
            return retInfo(info,HttpInfo.CheckURL);
        } catch(NetworkOnMainThreadException e){
            return retInfo(info,HttpInfo.NetworkOnMainThreadException);
        } catch(Exception e) {
            return retInfo(info,HttpInfo.NoResult,"["+e.getMessage()+"]");
        }finally {
            BaseActivityLifecycleCallbacks.cancel(requestTag,call);
        }
    }

    /**
     * �첽����
     */
    void doRequestAsync(final OkHttpHelper helper){
        final HttpInfo info = helper.getHttpInfo();
        final BaseCallback callback = helper.getCallback();
        Request request = helper.getRequest();
        String url = info.getUrl();
        if(!checkUrl(url)){
            //���̻߳ص�
            Message msg =  new CallbackMessage(OkMainHandler.RESPONSE_CALLBACK,
                    callback,
                    retInfo(info,HttpInfo.CheckURL),
                    requestTag,
                    null)
                    .build();
            OkMainHandler.getInstance().sendMessage(msg);
            return ;
        }
        request = request == null ? buildRequest(info,helper.getRequestMethod(),helper.getProgressCallback()) : request;
        showUrlLog(request);
        Call call = httpClient.newCall(request);
        BaseActivityLifecycleCallbacks.putCall(requestTag,call);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //���̻߳ص�
                Message msg =  new CallbackMessage(OkMainHandler.RESPONSE_CALLBACK,
                        callback,
                        retInfo(info,HttpInfo.NoResult,"["+e.getMessage()+"]"),
                        requestTag,
                        call)
                        .build();
                OkMainHandler.getInstance().sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response res) throws IOException {
                //���̻߳ص�
                Message msg =  new CallbackMessage(OkMainHandler.RESPONSE_CALLBACK,
                        callback,
                        dealResponse(helper,res,call),
                        requestTag,
                        call)
                        .build();
                OkMainHandler.getInstance().sendMessage(msg);
            }
        });
    }

    /**
     * �������URL
     */
    private boolean checkUrl(String url){
        HttpUrl parsed = HttpUrl.parse(url);
        return parsed != null && !TextUtils.isEmpty(url);
    }

    /**
     * ����Request
     */
    private Request buildRequest(HttpInfo info, @RequestMethod int method, ProgressCallback progressCallback){
        Request request;
        Request.Builder requestBuilder = new Request.Builder();
        final String url = info.getUrl();
        if(method == RequestMethod.POST){
            if(info.getParamBytes() != null){
                RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"),info.getParamBytes());
                requestBuilder.url(url).post(new ProgressRequestBody(fileBody,progressCallback,timeStamp,requestTag));
            } else if(info.getParamFile() != null){
                RequestBody fileBody = RequestBody.create(MediaType.parse("text/x-markdown; charset=utf-8"),info.getParamFile());
                requestBuilder.url(url).post(new ProgressRequestBody(fileBody,progressCallback,timeStamp,requestTag));
            } else{
                FormBody.Builder builder = new FormBody.Builder();
                if(null != info.getParams() && !info.getParams().isEmpty()){
                    StringBuilder log = new StringBuilder("PostParams: ");
                    String logInfo;
                    String value;
                    for (String key : info.getParams().keySet()) {
                        value = info.getParams().get(key);
                        value = value == null ? "" : value;
                        builder.add(key, value);
                        logInfo = key+"="+value+", ";
                        log.append(logInfo);
                    }
                    showLog(log.toString());
                }
                requestBuilder.url(url).post(builder.build());
            }
        } else if(method == RequestMethod.GET){
            StringBuilder params = new StringBuilder();
            params.append(url);
            if(null != info.getParams() && !info.getParams().isEmpty()){
                if(!url.contains("?") && !url.endsWith("?"))
                    params.append("?");
                String logInfo;
                String value;
                boolean isFirst = params.toString().endsWith("?");
                for (String name : info.getParams().keySet()) {
                    value = info.getParams().get(name);
                    value = value == null ? "" : value;
                    if(isFirst){
                        logInfo = name + "=" + value;
                        isFirst = false;
                    }else{
                        logInfo = "&" + name + "=" + value;
                    }
                    params.append(logInfo);
                }
            }
            requestBuilder.url(params.toString()).get();
        } else{
            requestBuilder.url(url).get();
        }
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB_MR2) {
            requestBuilder.addHeader("Connection", "close");
        }
        addHeadsToRequest(info,requestBuilder);
        request = requestBuilder.build();
        return request;
    }

    private void showUrlLog(Request request){
        startTime = System.nanoTime();
        showLog(String.format("%s-URL: %s %n",request.method(),request.url()));
    }

    /**
     * ����HTTP��Ӧ
     */
    private HttpInfo dealResponse(OkHttpHelper helper,Response res,Call call){
        showLog(String.format(Locale.getDefault(),"CostTime: %.3fs",(System.nanoTime()-startTime)/1e9d));
        final HttpInfo info = helper.getHttpInfo();
        BufferedReader bufferedReader = null ;
        String result = "";
        try {
            if(null != res){
                final int netCode = res.code();
                if(res.isSuccessful()){
                    if(helper.getBusinessType() == BusinessType.HttpOrHttps
                            || helper.getBusinessType() == BusinessType.UploadFile){
                        //��������Ӧ�����ʽ
                        String encoding = info.getResponseEncoding();
                        if(TextUtils.isEmpty(encoding)){
                            encoding = helper.getResponseEncoding();
                        }
                        bufferedReader = new BufferedReader(new InputStreamReader(res.body().byteStream(), encoding));
                        String line = "";
                        while ((line = bufferedReader.readLine()) != null) {
                            result += line;
                        }
                        return retInfo(info,netCode,HttpInfo.SUCCESS,result);
                    }else if(helper.getBusinessType() == BusinessType.DownloadFile){ //�����ļ�
                        return helper.getDownUpLoadHelper().downloadingFile(helper,res,call);
                    }
                }else{
                    showLog("HttpStatus: "+res.code());
                    if(netCode == 404){//����ҳ��·������
                        return retInfo(info,netCode,HttpInfo.ServerNotFound);
                    }else if(netCode == 416) {//������������Χ����
                        return retInfo(info, netCode, HttpInfo.Message, "����Http��������Χ����\n" + result);
                    }else if(netCode == 500) {//�������ڲ�����
                        return retInfo(info, netCode, HttpInfo.NoResult);
                    }else if(netCode == 502) {//���������
                        return retInfo(info, netCode, HttpInfo.GatewayBad);
                    }else if(netCode == 504) {//���س�ʱ
                        return retInfo(info,netCode,HttpInfo.GatewayTimeOut);
                    }else {
                        return retInfo(info,netCode,HttpInfo.CheckNet);
                    }
                }
            }
            return retInfo(info,HttpInfo.CheckURL);
        } catch (Exception e) {
            return retInfo(info,HttpInfo.NoResult,"["+e.getMessage()+"]");
        } finally {
            if(null != res){
                res.close();
            }
            if(null != bufferedReader){
                try {
                    bufferedReader.close();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

    HttpInfo retInfo(HttpInfo info, int code){
        return retInfo(info,code,code,null);
    }

    HttpInfo retInfo(HttpInfo info, int netCode, int code){
        return retInfo(info,netCode,code,null);
    }

    HttpInfo retInfo(HttpInfo info, int code, String resDetail){
        return retInfo(info,code,code,resDetail);
    }

    /**
     * ��װ������
     */
    HttpInfo retInfo(HttpInfo info, int netCode, int code, String resDetail){
        info.packInfo(netCode,code,unicodeToString(resDetail));
        //����������
        dealInterceptor(info);
        showLog("Response: "+info.getRetDetail());
        return info;
    }

    /**
     * unicode����ת��
     */
    private String unicodeToString(String str) {
        if(TextUtils.isEmpty(str))
            return "";
        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
        Matcher matcher = pattern.matcher(str);
        char ch;
        while (matcher.find()) {
            ch = (char) Integer.parseInt(matcher.group(2), 16);
            str = str.replace(matcher.group(1), ch + "");
        }
        return str;
    }

    /**
     * ����������
     */
    private void dealInterceptor(HttpInfo info){
        try {
            if(info.isSuccessful() && null != resultInterceptors){ //������������
                for(ResultInterceptor interceptor : resultInterceptors){
                    interceptor.intercept(info);
                }
            }else{ //������·�쳣������
                if(null != exceptionInterceptors){
                    for(ExceptionInterceptor interceptor : exceptionInterceptors){
                        interceptor.intercept(info);
                    }
                }
            }
        }catch (Exception e){
            showLog("�����������쳣��"+e.getMessage());
        }
    }

    /**
     * �������ص�
     */
    void responseCallback(HttpInfo info, ProgressCallback progressCallback, int code,boolean isDownload,String requestTag){
        //ͬ���ص�
        if(null != progressCallback)
            progressCallback.onResponseSync(info.getUrl(),info);
        //�첽���̻߳ص�
        if(isDownload){
            Message msg = new DownloadMessage(
                    code,
                    info.getUrl(),
                    info,
                    progressCallback,requestTag)
                    .build();
            OkMainHandler.getInstance().sendMessage(msg);
        }else{
            Message msg = new UploadMessage(
                    code,
                    info.getUrl(),
                    info,
                    progressCallback,requestTag)
                    .build();
            OkMainHandler.getInstance().sendMessage(msg);
        }
    }

    /**
     * �������ͷ����
     */
    Request.Builder addHeadsToRequest(HttpInfo info, Request.Builder requestBuilder){
        if(null != info.getHeads() && !info.getHeads().isEmpty()){
            for (String key : info.getHeads().keySet()) {
                requestBuilder.addHeader(key,info.getHeads().get(key));
            }
        }
        return requestBuilder;
    }




}
