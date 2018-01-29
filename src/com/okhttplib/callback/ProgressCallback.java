package com.okhttplib.callback;

import com.okhttplib.HttpInfo;

/**
 * ���Ȼص�
 * @author zhousf
 */
public class ProgressCallback extends ProgressCallbackAbs {

    /**
     * �첽UI�̣߳�����������
     * @param filePath �ļ�·��
     * @param info �����Ϣ��
     */
    @Override
    public void onResponseMain(String filePath, HttpInfo info) {

    }

    /**
     * ͬ����UI�̣߳�����������
     * @param filePath �ļ�·��
     * @param info �����Ϣ��
     */
    @Override
    public void onResponseSync(String filePath, HttpInfo info) {

    }

    /**
     * ��UI�̣߳����˸���ProgressBar�����ⲻҪ��������UI����
     * @param percent �Ѿ�д��İٷֱ�
     * @param bytesWritten �Ѿ�д����ֽ���
     * @param contentLength �ļ��ܳ���
     * @param done �Ƿ���ɼ���bytesWritten==contentLength
     */
    @Override
    public void onProgressAsync(int percent, long bytesWritten, long contentLength, boolean done) {

    }

    /**
     * UI�̣߳�����ֱ�Ӳ���UI
     * @param percent �Ѿ�д��İٷֱ�
     * @param bytesWritten �Ѿ�д����ֽ���
     * @param contentLength �ļ��ܳ���
     * @param done �Ƿ���ɼ���bytesWritten==contentLength
     */
    @Override
    public void onProgressMain(int percent, long bytesWritten, long contentLength, boolean done) {

    }


}
