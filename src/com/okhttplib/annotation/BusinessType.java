package com.okhttplib.annotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * ҵ������
 * @author zhousf
 */
@IntDef({BusinessType.HttpOrHttps, BusinessType.UploadFile, BusinessType.DownloadFile})
@Retention(RetentionPolicy.SOURCE)
public @interface BusinessType {

    /**
     * http/https����
     */
    int HttpOrHttps = 1;

    /**
     * �ļ��ϴ�
     */
    int UploadFile = 2;

    /**
     * �ļ�����
     */
    int DownloadFile = 3;
}
