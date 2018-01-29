package com.okhttplib.util;

import android.text.TextUtils;

import okhttp3.MediaType;

/**
 *  ý�����͹�����
 *  @author zhousf
 */
public class MediaTypeUtil {

    /**
     * ��������url����ý������
     * @param url �����ַ
     */
    public static MediaType fetchFileMediaType(String url){
        if(!TextUtils.isEmpty(url) && url.contains(".")){
            String extension = url.substring(url.lastIndexOf(".") + 1);
            if("png".equals(extension)){
                extension = "image/png";
            }else if("jpg".equals(extension)){
                extension = "image/jpg";
            }else if("jpeg".equals(extension)){
                extension = "image/jpeg";
            }else if("gif".equals(extension)){
                extension = "image/gif";
            }else if("bmp".equals(extension)){
                extension = "image/bmp";
            }else if("tiff".equals(extension)){
                extension = "image/tiff";
            }else if("ico".equals(extension)){
                extension = "image/ico";
            }else{
                extension = "multipart/form-data";
            }
            return MediaType.parse(extension);
        }
        return null;
    }

}
