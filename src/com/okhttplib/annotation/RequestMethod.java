package com.okhttplib.annotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * ���󷽷�
 * @author zhousf
 */
@IntDef({RequestMethod.POST,RequestMethod.GET,RequestMethod.PUT,RequestMethod.DELETE})
@Retention(RetentionPolicy.SOURCE)
public @interface RequestMethod {

    /**
     * GET
     */
    int POST = 1;

    /**
     * POST
     */
    int GET = 2;

    /**
     * PUT
     */
    int PUT = 3;

    /**
     * DELETE
     */
    int DELETE = 4;
}