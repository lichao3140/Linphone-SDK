package com.okhttplib.callback;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import okhttp3.Call;


/**
 * Activity�������ڻص�
 * @author zhousf
 */
public class BaseActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {

    private static final String TAG = "ActivityLifecycle";

    /**
     * �Ƿ���ʾActivityLifecycle��־
     */
    private static boolean showLifecycleLog;

    /**
     * ���󼯺�: key=Activity value=Call����
     */
    private static Map<String,SparseArray<Call>> callsMap = new ConcurrentHashMap<String, SparseArray<Call>>();

    /**
     * �������󼯺�
     * @param tag �����ʶ
     * @param call ����
     */
    public static void putCall(String tag, Call call){
        if(null != tag){
            SparseArray<Call> callList = callsMap.get(tag);
            if(null == callList){
                callList = new SparseArray<Call>();
            }
            callList.put(call.hashCode(),call);
            callsMap.put(tag,callList);
            showLog(false,tag);
        }

    }

    /**
     * ȡ������
     * @param tag �����ʶ
     */
    private static void cancelCallByActivityDestroy(String tag){
        if(null == tag)
            return ;
        SparseArray<Call> callList = callsMap.get(tag);
        if(null != callList){
            final int len = callList.size();
            for(int i=0;i<len;i++){
                Call call = callList.valueAt(i);
                if(null != call &&!call.isCanceled())
                    call.cancel();
            }
            callList.clear();
            callsMap.remove(tag);
            showLog(true,tag);
        }
    }

    /**
     * �жϵ�ǰActivity�Ƿ��Ѿ�����
     * @param activity �����ʶ
     * @return true �Ѿ�����  false δ����
     */
    public static boolean isActivityDestroyed(Activity activity){
        String tag = activity.getClass().getName();
        return callsMap.get(tag) == null;
    }

    /**
     * �жϵ�ǰtat�Ƿ��Ѿ�����
     * @param tag �����ʶ
     * @return true �Ѿ�����  false δ����
     */
    public static boolean isActivityDestroyed(String tag){
        return !TextUtils.isEmpty(tag) && callsMap.get(tag) == null;
    }

    /**
     * ȡ������
     * @param tag �����ʶ
     */
    public static void cancel(String tag){
        cancel(tag,null);
    }

    /**
     * ȡ������
     * @param tag �����ʶ
     * @param originalCall call
     */
    public static void cancel(String tag, Call originalCall){
        if(TextUtils.isEmpty(tag)){
            return ;
        }
        if(null != originalCall){
            SparseArray<Call> callList = callsMap.get(tag);
            if(null != callList){
                Call c = callList.get(originalCall.hashCode());
                if(null != c && !c.isCanceled())
                    c.cancel();
                callList.delete(originalCall.hashCode());
                if(callList.size() == 0)
                    callsMap.remove(tag);
                showLog(true,tag);
            }
        }else{
            SparseArray<Call> callList = callsMap.get(tag);
            if(null != callList){
                for(int i=0 ;i<callList.size();i++){
                    Call call = callList.valueAt(i);
                    if(null != call && !call.isCanceled()){
                        call.cancel();
                        callList.delete(call.hashCode());
                    }
                    if(callList.size() == 0)
                        callsMap.remove(tag);
                    showLog(true,tag);
                }
            }
        }
    }

    private static void showLog(boolean isCancel, String tag){
        if(!showLifecycleLog){
            return;
        }
        String callDetail = isCancel ? "ȡ������": "��������";
        Log.d(TAG,callDetail+": "+tag);
    }


    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        cancelCallByActivityDestroy(activity.getClass().getName());
    }

    public static void setShowLifecycleLog(boolean showLifecycle) {
        showLifecycleLog = showLifecycle;
    }
}
