package com.okhttplib.annotation;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * ���������������ʽ��淶
 * @author zhousf
 */
@StringDef({
        Encoding.ISO_8859_1,
        Encoding.UTF_8,
        Encoding.GBK,
        Encoding.GBK18030})
@Retention(RetentionPolicy.SOURCE)
public @interface Encoding {

    /**
     * ���ʱ�׼����淶
     */
    String ISO_8859_1 = "ISO-8859-1";

    /**
     * �����
     */
    String UTF_8 = "UTF-8";

    /**
     * ����������չ�淶
     * ����GBK_2312
     */
    String GBK = "GBK";

    /**
     * ��Ϣ�������ı����ַ���
     * ����GBK
     */
    String GBK18030 = "GBK18030";

}