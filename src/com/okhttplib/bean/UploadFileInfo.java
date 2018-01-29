package com.okhttplib.bean;


import com.okhttplib.callback.ProgressCallback;

/**
 * �ϴ��ļ���Ϣ��
 * @author zhousf
 */
public class UploadFileInfo {

    //�ϴ��ļ��ӿڵ�ַ
    private String url;
    //�ϴ����ļ�·���������ļ���
    private String filePathWithName;
    //�ӿڲ�������
    private String interfaceParamName;
    //�ϴ����Ȼص��ӿ�
    private ProgressCallback progressCallback;

    public UploadFileInfo(String filePathWithName, String interfaceParamName, ProgressCallback progressCallback) {
        this.filePathWithName = filePathWithName;
        this.interfaceParamName = interfaceParamName;
        this.progressCallback = progressCallback;
    }

    public UploadFileInfo(String url, String filePathWithName, String interfaceParamName, ProgressCallback progressCallback) {
        this.url = url;
        this.filePathWithName = filePathWithName;
        this.interfaceParamName = interfaceParamName;
        this.progressCallback = progressCallback;
    }

    public String getFilePathWithName() {
        return filePathWithName;
    }

    public void setFilePathWithName(String filePathWithName) {
        this.filePathWithName = filePathWithName;
    }

    public String getInterfaceParamName() {
        return interfaceParamName;
    }

    public void setInterfaceParamName(String interfaceParamName) {
        this.interfaceParamName = interfaceParamName;
    }

    public ProgressCallback getProgressCallback() {
        return progressCallback;
    }

    public void setProgressCallback(ProgressCallback progressCallback) {
        this.progressCallback = progressCallback;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
