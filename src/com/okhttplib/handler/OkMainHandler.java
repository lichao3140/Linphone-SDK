package com.okhttplib.handler;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.okhttplib.HttpInfo;
import com.okhttplib.bean.CallbackMessage;
import com.okhttplib.bean.DownloadMessage;
import com.okhttplib.bean.ProgressMessage;
import com.okhttplib.bean.UploadMessage;
import com.okhttplib.callback.BaseActivityLifecycleCallbacks;
import com.okhttplib.callback.BaseCallback;
import com.okhttplib.callback.CallbackOk;

import okhttp3.Call;

/**
 * ���߳�Handler
 * @author zhousf
 */
public class OkMainHandler extends Handler {

    private static OkMainHandler singleton;

    public static OkMainHandler getInstance(){
        if(null == singleton){
            synchronized (OkMainHandler.class){
                if(null == singleton)
                    singleton = new OkMainHandler();
            }
        }
        return singleton;
    }

    private OkMainHandler() {
        super(Looper.getMainLooper());
    }

    /**
     * ��������ص���ʶ
     */
    public static final int RESPONSE_CALLBACK = 0x01;

    /**
     * ���Ȼص���ʶ
     */
    public static final int PROGRESS_CALLBACK = 0x02;

    /**
     * �ϴ�����ص���ʶ
     */
    public static final int RESPONSE_UPLOAD_CALLBACK = 0x03;

    /**
     * ���ؽ���ص���ʶ
     */
    public static final int RESPONSE_DOWNLOAD_CALLBACK = 0x04;


    @Override
    public void handleMessage(Message msg) {
        final int what = msg.what;
        try {
            switch (what){
                case RESPONSE_CALLBACK://��������
                    CallbackMessage callMsg = (CallbackMessage) msg.obj;
                    if(null != callMsg.callback){
                        //��ʼ�ص�
                        if(!BaseActivityLifecycleCallbacks.isActivityDestroyed(callMsg.requestTag)){
                            BaseCallback callback = callMsg.callback;
                            if(callback instanceof CallbackOk){
                                ((CallbackOk)callback).onResponse(callMsg.info);
                            } else if(callback instanceof com.okhttplib.callback.Callback){
                                HttpInfo info = callMsg.info;
                                if(info.isSuccessful()){
                                    ((com.okhttplib.callback.Callback)callback).onSuccess(info);
                                }else{
                                    ((com.okhttplib.callback.Callback)callback).onFailure(info);
                                }
                            }
                        }
                    }
                    Call call = callMsg.call;
                    if (call != null) {
                        if(!call.isCanceled()){
                            call.cancel();
                        }
                        BaseActivityLifecycleCallbacks.cancel(callMsg.requestTag,call);
                    }
                    break;
                case PROGRESS_CALLBACK://���Ȼص�
                    ProgressMessage proMsg = (ProgressMessage) msg.obj;
                    if(null != proMsg.progressCallback){
                        if(!BaseActivityLifecycleCallbacks.isActivityDestroyed(proMsg.requestTag)){
                            proMsg.progressCallback.onProgressMain(proMsg.percent,proMsg.bytesWritten,proMsg.contentLength,proMsg.done);
                        }
                    }
                    break;
                case RESPONSE_UPLOAD_CALLBACK://�ϴ�����ص�
                    UploadMessage uploadMsg = (UploadMessage) msg.obj;
                    if(null != uploadMsg.progressCallback){
                        if(!BaseActivityLifecycleCallbacks.isActivityDestroyed(uploadMsg.requestTag)){
                            uploadMsg.progressCallback.onResponseMain(uploadMsg.filePath,uploadMsg.info);
                        }
                    }
                    break;
                case RESPONSE_DOWNLOAD_CALLBACK://���ؽ���ص�
                    DownloadMessage downloadMsg = (DownloadMessage) msg.obj;
                    if(null != downloadMsg){
                        if(!BaseActivityLifecycleCallbacks.isActivityDestroyed(downloadMsg.requestTag)){
                            downloadMsg.progressCallback.onResponseMain(downloadMsg.filePath,downloadMsg.info);
                        }
                    }
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }



}
