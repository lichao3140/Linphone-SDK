package com.okhttplib.bean;


import com.okhttplib.annotation.DownloadStatus;
import com.okhttplib.callback.ProgressCallback;

/**
 * �����ļ���Ϣ��
 * @author zhousf
 */
public class DownloadFileInfo {

    //�����ļ��ӿڵ�ַ
    private String url;
    //�ļ�����Ŀ¼
    private String saveFileDir;
    //�ļ���������
    private String saveFileName;
    //���ؽ��Ȼص��ӿ�
    private ProgressCallback progressCallback;
    //����״̬
    private String downloadStatus = DownloadStatus.INIT;
    //�������ֽ���
    private long completedSize;

    private String saveFileNameWithExtension;//�����ļ����ƣ�������չ��
    private String saveFileNameCopy;//�����ļ��������ƣ������ļ����Ƴ�ͻ
    private String saveFileNameEncrypt;//�����ļ����ƣ����ܺ�

    public DownloadFileInfo(String url, String saveFileName, ProgressCallback progressCallback) {
        this.url = url;
        this.saveFileName = saveFileName;
        this.progressCallback = progressCallback;
    }

    public DownloadFileInfo(String url, String saveFileDir, String saveFileName, ProgressCallback progressCallback) {
        this.url = url;
        this.saveFileDir = saveFileDir;
        this.saveFileName = saveFileName;
        this.progressCallback = progressCallback;
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

    public String getSaveFileDir() {
        return saveFileDir;
    }

    public void setSaveFileDir(String saveFileDir) {
        this.saveFileDir = saveFileDir;
    }

    public String getSaveFileName() {
        return saveFileName;
    }

    public void setSaveFileName(String saveFileName) {
        this.saveFileName = saveFileName;
    }

    public String getDownloadStatus() {
        return downloadStatus;
    }

    public void setDownloadStatus(@DownloadStatus String downloadStatus) {
        this.downloadStatus = downloadStatus;
    }

    public long getCompletedSize() {
        return completedSize;
    }

    public void setCompletedSize(long completedSize) {
        this.completedSize = completedSize;
    }


    public String getSaveFileNameWithExtension() {
        return saveFileNameWithExtension;
    }

    public void setSaveFileNameWithExtension(String saveFileNameWithExtension) {
        this.saveFileNameWithExtension = saveFileNameWithExtension;
    }

    public String getSaveFileNameCopy() {
        return saveFileNameCopy;
    }

    public void setSaveFileNameCopy(String saveFileNameCopy) {
        this.saveFileNameCopy = saveFileNameCopy;
    }

    public String getSaveFileNameEncrypt() {
        return saveFileNameEncrypt;
    }

    public void setSaveFileNameEncrypt(String saveFileNameEncrypt) {
        this.saveFileNameEncrypt = saveFileNameEncrypt;
    }

}
