package com.okhttplib;

import android.text.TextUtils;

import com.okhttplib.annotation.Encoding;
import com.okhttplib.bean.DownloadFileInfo;
import com.okhttplib.bean.UploadFileInfo;
import com.okhttplib.callback.ProgressCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Http����ʵ����
 * @author zhousf
 */
public class HttpInfo {

    //**�����������**/
    private String url;
    private Map<String,String> params;
    private byte[] paramBytes;
    private File paramFile;
    private List<UploadFileInfo> uploadFiles;
    private List<DownloadFileInfo> downloadFiles;
    private Map<String,String> heads;
    private @Encoding String responseEncoding ;//��������Ӧ����

    //**��Ӧ���ز�������**/
    private int retCode;//������
    private String retDetail;//���ؽ��
    private int netCode;//���緵����

    public HttpInfo(Builder builder) {
        this.url = builder.url;
        this.params = builder.params;
        this.paramBytes = builder.paramBytes;
        this.paramFile = builder.paramFile;
        this.uploadFiles = builder.uploadFiles;
        this.downloadFiles = builder.downloadFiles;
        this.heads = builder.heads;
        this.responseEncoding = builder.responseEncoding;
    }

    public static Builder Builder() {
        return new Builder();
    }


    public static final class Builder {

        private String url;//�����ַ
        private Map<String,String> params;//�������
        private byte[] paramBytes;//����������ֽ����飩
        private File paramFile;//����������ļ���
        private List<UploadFileInfo> uploadFiles;//�ϴ��ļ�����
        private List<DownloadFileInfo> downloadFiles;//�����ļ�����
        private Map<String,String> heads;//����ͷ����http head
        private @Encoding String responseEncoding ;//��������Ӧ����


        public Builder() {
        }

        public HttpInfo build(){
            return new HttpInfo(this);
        }

        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        /**
         * ��ӽӿڲ���
         * @param params ��������
         */
        public Builder addParams(Map<String, String> params) {
            if(null == params)
                return this;
            if(null == this.params){
                this.params = params;
            }else{
                this.params.putAll(params);
            }
            return this;
        }

        /**
         * ��ӽӿڲ���
         * @param key ������
         * @param value ����ֵ
         */
        public Builder addParam(String key, String value){
            if(null == this.params)
                this.params = new HashMap<String, String>();
            if(!TextUtils.isEmpty(key)){
                value = value == null ? "" : value;
                this.params.put(key,value);
            }
            return this;
        }

        /**
         * ��ӽӿڲ������ֽ�����/����������
         * �����POST����ʽ
         * MediaType.parse("application/octet-stream")
         * @param paramBytes ����ֵ
         */
        public Builder addParamBytes(byte[] paramBytes){
            this.paramBytes = paramBytes;
            return this;
        }

        /**
         * ��ӽӿڲ������ֽ�����/����������
         * �����POST����ʽ
         * MediaType.parse("application/octet-stream")
         * @param paramBytes ����ֵ
         */
        public Builder addParamBytes(String paramBytes){
            if(TextUtils.isEmpty(paramBytes)){
                throw new IllegalArgumentException("paramBytes must not be null");
            }
            this.paramBytes = paramBytes.getBytes();
            return this;
        }

        /**
         * ��ӽӿڲ������ļ���
         * �����POST����ʽ
         * �÷������ϴ��ļ��������ϴ��ļ����ñ�׼������addUploadFile
         * MediaType.parse("text/x-markdown; charset=utf-8")
         * @param file �ϴ��ļ�
         */
        public Builder addParamFile(File file){
            if(file == null || !file.exists()){
                throw new IllegalArgumentException("file must not be null");
            }
            this.paramFile = file;
            return this;
        }

        /**
         * ���Э��ͷ����
         * @param heads ͷ��������
         */
        public Builder addHeads(Map<String, String> heads) {
            if(null == heads)
                return this;
            if(null == this.heads){
                this.heads = heads;
            }else{
                this.heads.putAll(heads);
            }
            return this;
        }

        /**
         * ���Э��ͷ����
         * @param key ͷ������
         * @param value ͷ����ֵ
         */
        public Builder addHead(String key, String value){
            if(null == this.heads)
                this.heads = new HashMap<String, String>();
            if(!TextUtils.isEmpty(key)){
                value = value == null ? "" : value;
                this.heads.put(key,value);
            }
            return this;
        }

        /**
         * ����ϴ��ļ�
         * @param interfaceParamName �ӿڲ�������
         * @param filePathWithName �ϴ����ļ�·���������ļ���
         */
        public Builder addUploadFile(String interfaceParamName, String filePathWithName) {
            addUploadFile(interfaceParamName,filePathWithName,null);
            return this;
        }

        /**
         * ����ϴ��ļ�
         * @param interfaceParamName �ӿڲ�������
         * @param filePathWithName �ϴ����ļ�·���������ļ���
         * @param progressCallback �ϴ����Ȼص��ӿ�
         */
        public Builder addUploadFile(String interfaceParamName, String filePathWithName, ProgressCallback progressCallback) {
            if(null == this.uploadFiles){
                this.uploadFiles = new ArrayList<UploadFileInfo>();
            }
            if(!TextUtils.isEmpty(filePathWithName)){
                this.uploadFiles.add(new UploadFileInfo(filePathWithName,interfaceParamName,progressCallback));
            }
            return this;
        }

        /**
         * ����ϴ��ļ�
         * @param url �ϴ��ļ��ӿڵ�ַ
         * @param interfaceParamName �ӿڲ�������
         * @param filePathWithName �ϴ����ļ�·���������ļ���
         * @param progressCallback �ϴ����Ȼص��ӿ�
         */
        public Builder addUploadFile(String url, String interfaceParamName, String filePathWithName, ProgressCallback progressCallback) {
            if(null == this.uploadFiles){
                this.uploadFiles = new ArrayList<UploadFileInfo>();
            }
            if(!TextUtils.isEmpty(filePathWithName)){
                this.uploadFiles.add(new UploadFileInfo(url,filePathWithName,interfaceParamName,progressCallback));
            }
            return this;
        }

        public Builder addUploadFiles(List<UploadFileInfo> uploadFiles){
            if(null == uploadFiles)
                return this;
            if(null == this.uploadFiles){
                this.uploadFiles = uploadFiles;
            }else{
                this.uploadFiles.addAll(uploadFiles);
            }
            return this;
        }

        /**
         * ��������ļ�
         * @param url �����ļ��������ַ
         * @param saveFileName �ļ��������ƣ���������չ��
         */
        public Builder addDownloadFile(String url,String saveFileName){
            addDownloadFile(url,null,saveFileName,null);
            return this;
        }

        /**
         * ��������ļ�
         * @param url �����ļ��������ַ
         * @param saveFileName �ļ��������ƣ���������չ��
         * @param progressCallback ���ؽ��Ȼص��ӿ�
         */
        public Builder addDownloadFile(String url,String saveFileName,ProgressCallback progressCallback){
            addDownloadFile(url,null,saveFileName,progressCallback);
            return this;
        }

        /**
         * ��������ļ�
         * @param url �����ļ��������ַ
         * @param saveFileDir �ļ�����Ŀ¼·��������������
         * @param saveFileName �ļ��������ƣ���������չ��
         * @param progressCallback ���ؽ��Ȼص��ӿ�
         */
        public Builder addDownloadFile(String url,String saveFileDir,String saveFileName,ProgressCallback progressCallback){
            if(null == this.downloadFiles){
                this.downloadFiles = new ArrayList<DownloadFileInfo>();
            }
            if(!TextUtils.isEmpty(url)){
                this.downloadFiles.add(new DownloadFileInfo(url,saveFileDir,saveFileName,progressCallback));
            }
            return this;
        }

        public Builder addDownloadFile(DownloadFileInfo downloadFile){
            if(null == downloadFile)
                return this;
            if(null == this.downloadFiles){
                this.downloadFiles = new ArrayList<DownloadFileInfo>();
            }
            this.downloadFiles.add(downloadFile);
            return this;
        }

        public Builder addDownloadFiles(List<DownloadFileInfo> downloadFiles){
            if(null == downloadFiles)
                return this;
            if(null == this.downloadFiles){
                this.downloadFiles = downloadFiles;
            }else {
                this.downloadFiles.addAll(downloadFiles);
            }
            return this;
        }

        /**
         * ���÷�������Ӧ�����ʽ��Ĭ�ϣ�UTF-8��
         * @param responseEncoding �����ʽ
         */
        public Builder setResponseEncoding(@Encoding String responseEncoding) {
            this.responseEncoding = responseEncoding;
            return this;
        }
    }


    //**���󷵻س�������**/
    public final static int SUCCESS = 1;
    public final static int NonNetwork = 2;
    public final static int ProtocolException = 3;
    public final static int NoResult = 4;
    public final static int CheckURL = 5;
    public final static int CheckNet = 6;
    public final static int ConnectionTimeOut = 7;
    public final static int WriteAndReadTimeOut = 8;
    public final static int ConnectionInterruption = 9;
    public final static int NetworkOnMainThreadException = 10;
    public final static int Message = 11;
    public final static int GatewayTimeOut = 12;
    public final static int GatewayBad = 13;
    public final static int ServerNotFound = 14;


    public HttpInfo packInfo(int netCode,int retCode, String retDetail){
        this.netCode = netCode;
        this.retCode = retCode;
        switch (retCode){
            case NonNetwork:
                this.retDetail = "�����ж�";
                break;
            case SUCCESS:
                this.retDetail = "��������ɹ�";
                break;
            case ProtocolException:
                this.retDetail = "����Э�������Ƿ���ȷ";
                break;
            case NoResult:
                this.retDetail = "�޷���ȡ������Ϣ(�������ڲ�����)";
                break;
            case CheckURL:
                this.retDetail = "���������ַ�Ƿ���ȷ";
                break;
            case CheckNet:
                this.retDetail = "�������������Ƿ�����";
                break;
            case ConnectionTimeOut:
                this.retDetail = "���ӳ�ʱ";
                break;
            case WriteAndReadTimeOut:
                this.retDetail = "��д��ʱ";
                break;
            case ConnectionInterruption:
                this.retDetail = "�����ж�";
                break;
            case NetworkOnMainThreadException:
                this.retDetail = "��������UI�߳��н����������";
                break;
            case Message:
                this.retDetail = "";
                break;
            case GatewayTimeOut:
                this.retDetail = "���س�ʱ/δ�ҵ����棬�������������Ƿ�����";
                break;
            case GatewayBad:
                this.retDetail = "��������أ�����������·";
                break;
            case ServerNotFound:
                this.retDetail = "�������Ҳ�������ҳ��(�������ڲ�����)";
                break;
        }
        if(!TextUtils.isEmpty(retDetail)){
            this.retDetail = retDetail;
        }
        return this;
    }

    public int getRetCode() {
        return retCode;
    }

    public boolean isSuccessful(){
        return this.retCode == SUCCESS;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRetDetail() {
        return retDetail;
    }

    public void setRetDetail(String retDetail) {
        this.retDetail = retDetail;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public byte[] getParamBytes() {
        return paramBytes;
    }

    public File getParamFile() {
        return paramFile;
    }

    public List<UploadFileInfo> getUploadFiles() {
        return uploadFiles;
    }

    public List<DownloadFileInfo> getDownloadFiles() {
        return downloadFiles;
    }

    public Map<String, String> getHeads() {
        return heads;
    }

    public int getNetCode() {
        return netCode;
    }

    public String getResponseEncoding() {
        return responseEncoding;
    }

}
