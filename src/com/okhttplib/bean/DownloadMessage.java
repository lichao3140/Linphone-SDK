package com.okhttplib.bean;

import com.okhttplib.HttpInfo;
import com.okhttplib.callback.ProgressCallback;

/**
 * ������Ӧ�ص���Ϣ��
 * @author zhousf
 */
public class DownloadMessage extends OkMessage{

    public String filePath;
    public HttpInfo info;
    public ProgressCallback progressCallback;

    public DownloadMessage(int what, String filePath, HttpInfo info, ProgressCallback progressCallback, String requestTag) {
        this.what = what;
        this.filePath = filePath;
        this.info = info;
        this.progressCallback = progressCallback;
        super.requestTag = requestTag;
    }
}