package com.okhttplib.annotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * ����ȼ�
 * @author zhousf
 */
@IntDef({CacheLevel.FIRST_LEVEL, CacheLevel.SECOND_LEVEL, CacheLevel.THIRD_LEVEL, CacheLevel.FOURTH_LEVEL})
@Retention(RetentionPolicy.SOURCE)
public @interface CacheLevel {

    /**
     * �޻���
     */
    int FIRST_LEVEL = 1;

    /**
     * 15��(������Чʱ��)
     */
    int SECOND_LEVEL = 2;

    /**
     * 30��(������Чʱ��)
     */
    int THIRD_LEVEL = 3;

    /**
     * 60��(������Чʱ��)
     */
    int FOURTH_LEVEL = 4;
}