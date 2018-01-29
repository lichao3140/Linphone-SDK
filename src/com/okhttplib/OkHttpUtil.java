package com.okhttplib;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.text.TextUtils;

import com.okhttplib.annotation.CacheLevel;
import com.okhttplib.annotation.CacheType;
import com.okhttplib.annotation.Encoding;
import com.okhttplib.annotation.RequestMethod;
import com.okhttplib.bean.DownloadFileInfo;
import com.okhttplib.bean.UploadFileInfo;
import com.okhttplib.callback.BaseActivityLifecycleCallbacks;
import com.okhttplib.callback.BaseCallback;
import com.okhttplib.callback.ProgressCallback;
import com.okhttplib.helper.HelperInfo;
import com.okhttplib.helper.OkHttpHelper;
import com.okhttplib.interceptor.ExceptionInterceptor;
import com.okhttplib.interceptor.ResultInterceptor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.CookieJar;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.okhttplib.annotation.CacheLevel.FIRST_LEVEL;
import static com.okhttplib.annotation.CacheLevel.FOURTH_LEVEL;
import static com.okhttplib.annotation.CacheLevel.SECOND_LEVEL;
import static com.okhttplib.annotation.CacheLevel.THIRD_LEVEL;
import static com.okhttplib.annotation.CacheType.CACHE_THEN_NETWORK;
import static com.okhttplib.annotation.CacheType.FORCE_CACHE;
import static com.okhttplib.annotation.CacheType.FORCE_NETWORK;
import static com.okhttplib.annotation.CacheType.NETWORK_THEN_CACHE;


/**
 * �������󹤾���
 * 1��ͬ��/�첽��GET/POST�������󣬻�����Ӧ
 * 2��http/https
 * 3����Activity/Fragment����ʱ�Զ�ȡ����Ӧ��������������
 * 4���Զ��л�UI�̣߳�����runOnUiThread
 * 5��Application���Զ���ȫ������/����ϵͳĬ������
 * 6���ļ���ͼƬ�ϴ�/�����ϴ���֧��ͬ��/�첽�ϴ���֧�ֽ�����ʾ
 * 7���ļ��ϵ����أ��������ص�ģ�����������ݿ��¼�ϵ�
 * 8����־�������쳣����
 * 9��֧�������������Լ��쳣��������
 * 10��֧��Cookie�־û�
 * 11��֧��Э��ͷ����Head����
 *
 * ����汾com.squareup.okhttp3:okhttp:3.7.0
 * @author zhousf
 */
public class OkHttpUtil implements OkHttpUtilInterface{

    private final String TAG = getClass().getSimpleName();
    private static Application application;
    private static Builder builderGlobal;
    private static OkHttpClient httpClient;
    private static ExecutorService executorService;
    private Builder builder;
    private int cacheSurvivalTime;//������ʱ�䣨�룩
    private @CacheType int cacheType;//��������

    /**
     * ��ʼ��������Application�е���
     * @param context ������
     */
    public static Builder init(Application context){
        application = context;
        application.registerActivityLifecycleCallbacks(new BaseActivityLifecycleCallbacks());
        return BuilderGlobal();
    }

    /**
     * ��ȡĬ����������
     * @return OkHttpUtil
     */
    public static OkHttpUtilInterface getDefault(){
        return new Builder(false).isDefault(true).build();
    }

    /**
     * ��ȡĬ���������ã��÷������Activity��fragment��������
     * @param requestTag �����ʶ
     * @return OkHttpUtil
     */
    public static OkHttpUtilInterface getDefault(Object requestTag){
        return new Builder(false).isDefault(true).build(requestTag);
    }

    /**
     * ͬ��Post����
     * @param info ������Ϣ��
     * @return HttpInfo
     */
    @Override
    public HttpInfo doPostSync(HttpInfo info){
        return OkHttpHelper.Builder()
                .httpInfo(info)
                .requestMethod(RequestMethod.POST)
                .helperInfo(packageHelperInfo())
                .build()
                .doRequestSync();
    }

    /**
     * ͬ��Post����
     * @param info ������Ϣ��
     * @param callback ���Ȼص��ӿ�
     * @return HttpInfo
     */
    @Override
    public HttpInfo doPostSync(HttpInfo info, ProgressCallback callback){
        return OkHttpHelper.Builder()
                .httpInfo(info)
                .requestMethod(RequestMethod.POST)
                .progressCallback(callback)
                .helperInfo(packageHelperInfo())
                .build()
                .doRequestSync();
    }

    /**
     * �첽Post����
     * @param info ������Ϣ��
     * @param callback �ص��ӿ�
     */
    @Override
    public void doPostAsync(HttpInfo info, BaseCallback callback){
        OkHttpHelper.Builder()
                .httpInfo(info)
                .requestMethod(RequestMethod.POST)
                .callback(callback)
                .helperInfo(packageHelperInfo())
                .build()
                .doRequestAsync();
    }

    /**
     * �첽Post����
     * @param info ������Ϣ��
     * @param callback ���Ȼص��ӿ�
     */
    @Override
    public void doPostAsync(HttpInfo info, ProgressCallback callback) {
        OkHttpHelper.Builder()
                .httpInfo(info)
                .requestMethod(RequestMethod.POST)
                .progressCallback(callback)
                .helperInfo(packageHelperInfo())
                .build()
                .doRequestAsync();
    }

    /**
     * ͬ��Get����
     * @param info ������Ϣ��
     * @return HttpInfo
     */
    @Override
    public HttpInfo doGetSync(HttpInfo info){
        return OkHttpHelper.Builder()
                .httpInfo(info)
                .requestMethod(RequestMethod.GET)
                .helperInfo(packageHelperInfo())
                .build()
                .doRequestSync();
    }

    /**
     * �첽Get����
     * @param info ������Ϣ��
     * @param callback �ص��ӿ�
     */
    @Override
    public void doGetAsync(HttpInfo info, BaseCallback callback) {
        OkHttpHelper.Builder()
                .httpInfo(info)
                .requestMethod(RequestMethod.GET)
                .callback(callback)
                .helperInfo(packageHelperInfo())
                .build()
                .doRequestAsync();
    }

    /**
     * �첽�ϴ��ļ�
     * @param info ������Ϣ��
     */
    @Override
    public void doUploadFileAsync(final HttpInfo info){
        List<UploadFileInfo> uploadFiles = info.getUploadFiles();
        for(final UploadFileInfo fileInfo : uploadFiles){
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    OkHttpHelper.Builder()
                            .httpInfo(info)
                            .uploadFileInfo(fileInfo)
                            .requestMethod(RequestMethod.POST)
                            .helperInfo(packageHelperInfo())
                            .build()
                            .uploadFile();
                }
            });
        }
    }

    /**
     * �����첽�ϴ��ļ�
     * @param info ������Ϣ��
     */
    @Override
    public void doUploadFileAsync(final HttpInfo info, final ProgressCallback callback){
        final List<UploadFileInfo> uploadFiles = info.getUploadFiles();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                OkHttpHelper.Builder()
                        .httpInfo(info)
                        .uploadFileInfoList(uploadFiles)
                        .requestMethod(RequestMethod.POST)
                        .progressCallback(callback)
                        .helperInfo(packageHelperInfo())
                        .build()
                        .uploadFile();
            }
        });
    }

    /**
     * ͬ���ϴ��ļ�
     * @param info ������Ϣ��
     */
    @Override
    public void doUploadFileSync(final HttpInfo info){
        List<UploadFileInfo> uploadFiles = info.getUploadFiles();
        for(final UploadFileInfo fileInfo : uploadFiles){
            OkHttpHelper.Builder()
                    .httpInfo(info)
                    .uploadFileInfo(fileInfo)
                    .requestMethod(RequestMethod.POST)
                    .helperInfo(packageHelperInfo())
                    .build()
                    .uploadFile();
        }
    }

    /**
     * ����ͬ���ϴ��ļ�
     * @param info ������Ϣ��
     */
    @Override
    public void doUploadFileSync(final HttpInfo info, final ProgressCallback callback){
        final List<UploadFileInfo> uploadFiles = info.getUploadFiles();
        OkHttpHelper.Builder()
                .httpInfo(info)
                .uploadFileInfoList(uploadFiles)
                .requestMethod(RequestMethod.POST)
                .progressCallback(callback)
                .helperInfo(packageHelperInfo())
                .build()
                .uploadFile();
    }

    /**
     * �첽�����ļ�
     * @param info ������Ϣ��
     */
    @Override
    public void doDownloadFileAsync(final HttpInfo info){
        List<DownloadFileInfo> downloadFiles = info.getDownloadFiles();
        for(final DownloadFileInfo fileInfo : downloadFiles){
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    OkHttpHelper.Builder()
                            .httpInfo(info)
                            .downloadFileInfo(fileInfo)
                            .requestMethod(RequestMethod.GET)
                            .clientBuilder(newBuilderFromCopy())
                            .helperInfo(packageHelperInfo())
                            .build()
                            .downloadFile();
                }
            });
        }
    }

    /**
     * ͬ�������ļ�
     * @param info ������Ϣ��
     */
    @Override
    public void doDownloadFileSync(final HttpInfo info){
        List<DownloadFileInfo> downloadFiles = info.getDownloadFiles();
        for(final DownloadFileInfo fileInfo : downloadFiles){
            OkHttpHelper.Builder()
                    .httpInfo(info)
                    .downloadFileInfo(fileInfo)
                    .requestMethod(RequestMethod.GET)
                    .clientBuilder(newBuilderFromCopy())
                    .helperInfo(packageHelperInfo())
                    .build()
                    .downloadFile();
        }
    }

    /**
     * ȡ������
     * @param requestTag �����ʶ
     */
    @Override
    public void cancelRequest(Object requestTag) {
        BaseActivityLifecycleCallbacks.cancel(parseRequestTag(requestTag));
    }

    @Override
    public OkHttpClient getDefaultClient() {
        return httpClient;
    }

    public void setDefaultClient(OkHttpClient client){
        httpClient = client;
    }

    /**
     * ��������������
     */
    private Interceptor NETWORK_INTERCEPTOR = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if(cacheSurvivalTime > 0){
                Response response = chain.proceed(request);
                return response.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", String.format(Locale.getDefault(),"max-age=%d", cacheSurvivalTime))
                        .build();
            }
            return chain.proceed(request);
        }
    };

    /**
     * ����Ӧ��������
     */
    private Interceptor NO_NETWORK_INTERCEPTOR = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request();
            Response response = null;
            switch (cacheType){
                case CACHE_THEN_NETWORK:
                case FORCE_CACHE:
                    originalRequest = originalRequest.newBuilder()
                            .cacheControl(CacheControl.FORCE_CACHE)
                            .build();
                    response =  chain.proceed(originalRequest).newBuilder()
                            .header("Cache-Control", "public, only-if-cached, max-stale=3600")
                            .removeHeader("Pragma")
                            .build();
                    break;
                case NETWORK_THEN_CACHE:
                    if(!isNetworkAvailable(application)){
                        originalRequest = originalRequest.newBuilder()
                                .cacheControl(CacheControl.FORCE_CACHE)
                                .build();
                        response =  chain.proceed(originalRequest).newBuilder()
                                .header("Cache-Control", "public, only-if-cached, max-stale=3600")
                                .removeHeader("Pragma")
                                .build();
                    }else{
                        response = chain.proceed(originalRequest);
                    }
                    break;
                case FORCE_NETWORK:
                    response = chain.proceed(originalRequest);
                    break;
            }
            return response;
        }
    };

    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo net = cm.getActiveNetworkInfo();
        return net != null && net.getState() == NetworkInfo.State.CONNECTED;
    }

    private OkHttpUtil(Builder builder) {
        //��ʼ������
        this.builder = builder;
        this.cacheType = builder.cacheType;
        this.cacheSurvivalTime = builder.cacheSurvivalTime;
        if(this.cacheSurvivalTime == 0){
            final int deviation = 5;
            switch (builder.cacheLevel){
                case FIRST_LEVEL:
                    this.cacheSurvivalTime = 0;
                    break;
                case SECOND_LEVEL:
                    this.cacheSurvivalTime = 15 + deviation;
                    break;
                case THIRD_LEVEL:
                    this.cacheSurvivalTime = 30 + deviation;
                    break;
                case FOURTH_LEVEL:
                    this.cacheSurvivalTime = 60 + deviation;
                    break;
            }
        }
        if(this.cacheSurvivalTime > 0)
            this.cacheType = CACHE_THEN_NETWORK;
        if(null == application)
            this.cacheType = FORCE_NETWORK;
        if(null == executorService)
            executorService = Executors.newCachedThreadPool();
        BaseActivityLifecycleCallbacks.setShowLifecycleLog(builder.showLifecycleLog);
        if(builder.isGlobalConfig){
            OkHttpHelper.Builder()
                    .helperInfo(packageHelperInfo())
                    .build();
        }
    }

    /**
     * ��װҵ������Ϣ
     */
    private HelperInfo packageHelperInfo(){
        HelperInfo helperInfo = new HelperInfo();
        helperInfo.setShowHttpLog(builder.showHttpLog);
        helperInfo.setRequestTag(builder.requestTag);
        int random = 1000 + (int)(Math.random()*999);
        String timeStamp = System.currentTimeMillis()+"_"+random;
        helperInfo.setTimeStamp(timeStamp);
        helperInfo.setExceptionInterceptors(builder.exceptionInterceptors);
        helperInfo.setResultInterceptors(builder.resultInterceptors);
        helperInfo.setDownloadFileDir(builder.downloadFileDir);
        helperInfo.setClientBuilder(newBuilderFromCopy());
        helperInfo.setOkHttpUtil(this);
        helperInfo.setDefault(builder.isDefault);
        helperInfo.setLogTAG(builder.httpLogTAG == null ? TAG : builder.httpLogTAG);
        helperInfo.setResponseEncoding(builder.responseEncoding);
        return helperInfo;
    }

    private OkHttpClient.Builder newBuilderFromCopy(){
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                .connectTimeout(builder.connectTimeout, TimeUnit.SECONDS)
                .readTimeout(builder.readTimeout, TimeUnit.SECONDS)
                .writeTimeout(builder.writeTimeout, TimeUnit.SECONDS)
                .cache(new Cache(builder.cachedDir,builder.maxCacheSize))
                .retryOnConnectionFailure(builder.retryOnConnectionFailure);
        clientBuilder.addInterceptor(NO_NETWORK_INTERCEPTOR);
        clientBuilder.addNetworkInterceptor(NETWORK_INTERCEPTOR);
        if(null != builder.networkInterceptors && !builder.networkInterceptors.isEmpty())
            clientBuilder.networkInterceptors().addAll(builder.networkInterceptors);
        if(null != builder.interceptors && !builder.interceptors.isEmpty())
            clientBuilder.interceptors().addAll(builder.interceptors);
        if(null != builder.cookieJar)
            clientBuilder.cookieJar(builder.cookieJar);
        return clientBuilder;
    }

    public static Builder Builder() {
        return new Builder(false);
    }

    private static Builder BuilderGlobal() {
        return new Builder(true).isDefault(true);
    }

    public static final class Builder {

        private int maxCacheSize;//�����С
        private File cachedDir;//����Ŀ¼
        private int connectTimeout;//���ӳ�ʱ
        private int readTimeout;//����ʱ
        private int writeTimeout;//д��ʱ
        private boolean retryOnConnectionFailure;//ʧ����������
        private List<Interceptor> networkInterceptors;//����������
        private List<Interceptor> interceptors;//Ӧ��������
        private List<ResultInterceptor> resultInterceptors;//������������
        private List<ExceptionInterceptor> exceptionInterceptors;//������·�쳣������
        private int cacheSurvivalTime;//������ʱ�䣨�룩
        private @CacheType int cacheType;//��������
        private @CacheLevel int cacheLevel;//���漶��
        private boolean isGlobalConfig;//�Ƿ�ȫ������
        private boolean showHttpLog;//�Ƿ���ʾHttp������־
        private String httpLogTAG;//��ʾHttp������־��ʶ
        private boolean showLifecycleLog;//�Ƿ���ʾActivityLifecycle��־
        private String downloadFileDir;//�����ļ�����Ŀ¼
        private String requestTag;
        private CookieJar cookieJar;
        private boolean isDefault;//�Ƿ�Ĭ������
        private @Encoding String responseEncoding = Encoding.UTF_8 ;//��������Ӧ����

        public Builder() {
        }

        public Builder(boolean isGlobal) {
            isGlobalConfig = isGlobal;
            //ϵͳĬ������
            initDefaultConfig();
            if(!isGlobal && null != builderGlobal){
                //ȫ���Զ�������
                initGlobalConfig(builderGlobal);
            }
        }

        public OkHttpUtilInterface build(){
            return build(null);
        }

        public OkHttpUtilInterface build(Object requestTag) {
            if(isGlobalConfig && null == builderGlobal){
                builderGlobal = this;
            }
            if(null != requestTag)
                setRequestTag(requestTag);
            return new OkHttpUtil(this);
        }

        /**
         * ϵͳĬ������
         */
        private void initDefaultConfig(){
            setMaxCacheSize(10 * 1024 * 1024);
            if(null != application){
                setCachedDir(application.getExternalCacheDir());
            }else{
                setCachedDir(Environment.getExternalStorageDirectory());
            }
            setConnectTimeout(30);
            setReadTimeout(30);
            setWriteTimeout(30);
            setRetryOnConnectionFailure(true);
            setCacheSurvivalTime(0);
            setCacheType(CACHE_THEN_NETWORK);
            setCacheLevel(FIRST_LEVEL);
            setNetworkInterceptors(null);
            setInterceptors(null);
            setResultInterceptors(null);
            setExceptionInterceptors(null);
            setShowHttpLog(true);
            setShowLifecycleLog(false);
            setDownloadFileDir(Environment.getExternalStorageDirectory().getPath()+"/okHttp_download/");
        }

        /**
         * ȫ���Զ�������
         * @param builder builder
         */
        private void initGlobalConfig(Builder builder){
            setMaxCacheSize(builder.maxCacheSize);
            setCachedDir(builder.cachedDir);
            setConnectTimeout(builder.connectTimeout);
            setReadTimeout(builder.readTimeout);
            setWriteTimeout(builder.writeTimeout);
            setRetryOnConnectionFailure(builder.retryOnConnectionFailure);
            setCacheSurvivalTime(builder.cacheSurvivalTime);
            setCacheType(builder.cacheType);
            setCacheLevel(builder.cacheLevel);
            setNetworkInterceptors(builder.networkInterceptors);
            setInterceptors(builder.interceptors);
            setResultInterceptors(builder.resultInterceptors);
            setExceptionInterceptors(builder.exceptionInterceptors);
            setShowHttpLog(builder.showHttpLog);
            setHttpLogTAG(builder.httpLogTAG);
            setShowLifecycleLog(builder.showLifecycleLog);
            if(!TextUtils.isEmpty(builder.downloadFileDir)){
                setDownloadFileDir(builder.downloadFileDir);
            }
            setCookieJar(builder.cookieJar);
            setResponseEncoding(builder.responseEncoding);
        }

        private Builder isDefault(boolean isDefault){
            this.isDefault = isDefault;
            return this;
        }

        //���û����С
        public Builder setMaxCacheSize(int maxCacheSize) {
            this.maxCacheSize = maxCacheSize;
            return this;
        }

        //���û���Ŀ¼
        public Builder setCachedDir(File cachedDir) {
            if(null != cachedDir)
                this.cachedDir = cachedDir;
            return this;
        }

        //�������ӳ�ʱ����λ���룩
        public Builder setConnectTimeout(int connectTimeout) {
            if(connectTimeout <= 0)
                throw new IllegalArgumentException("connectTimeout must be > 0");
            this.connectTimeout = connectTimeout;
            return this;
        }

        //���ö���ʱ����λ���룩
        public Builder setReadTimeout(int readTimeout) {
            if(readTimeout <= 0)
                throw new IllegalArgumentException("readTimeout must be > 0");
            this.readTimeout = readTimeout;
            return this;
        }

        //����д��ʱ����λ���룩
        public Builder setWriteTimeout(int writeTimeout) {
            if(writeTimeout <= 0)
                throw new IllegalArgumentException("writeTimeout must be > 0");
            this.writeTimeout = writeTimeout;
            return this;
        }

        //����ʧ����������
        public Builder setRetryOnConnectionFailure(boolean retryOnConnectionFailure) {
            this.retryOnConnectionFailure = retryOnConnectionFailure;
            return this;
        }

        //����������������ÿ��Http����ʱ����ִ�и�������
        public Builder setNetworkInterceptors(List<Interceptor> networkInterceptors) {
            if(null != networkInterceptors)
                this.networkInterceptors = networkInterceptors;
            return this;
        }

        //����Ӧ����������ÿ��Http����������ʱ����ִ�и�������
        public Builder setInterceptors(List<Interceptor> interceptors) {
            if(null != interceptors)
                this.interceptors = interceptors;
            return this;
        }

        //����������������
        public Builder setResultInterceptors(List<ResultInterceptor> resultInterceptors){
            if(null != resultInterceptors)
                this.resultInterceptors = resultInterceptors;
            return this;
        }

        public Builder addResultInterceptor(ResultInterceptor resultInterceptor){
            if(null != resultInterceptor){
                if(null == this.resultInterceptors)
                    this.resultInterceptors = new ArrayList<ResultInterceptor>();
                this.resultInterceptors.add(resultInterceptor);
            }
            return this;
        }

        //����������·�쳣������
        public Builder setExceptionInterceptors(List<ExceptionInterceptor> exceptionInterceptors){
            if(null != exceptionInterceptors){
                this.exceptionInterceptors = exceptionInterceptors;
            }
            return this;
        }

        public Builder addExceptionInterceptor(ExceptionInterceptor exceptionInterceptor){
            if(null != exceptionInterceptor){
                if(null == this.exceptionInterceptors)
                    this.exceptionInterceptors = new ArrayList<ExceptionInterceptor>();
                this.exceptionInterceptors.add(exceptionInterceptor);
            }
            return this;
        }

        //���û�����ʱ�䣨�룩
        public Builder setCacheSurvivalTime(int cacheSurvivalTime) {
            if(cacheSurvivalTime < 0)
                throw new IllegalArgumentException("cacheSurvivalTime must be >= 0");
            this.cacheSurvivalTime = cacheSurvivalTime;
            return this;
        }

        //���û�������
        public Builder setCacheType(@CacheType int cacheType) {
            this.cacheType = cacheType;
            return this;
        }

        //���û��漶��
        public Builder setCacheLevel(@CacheLevel int cacheLevel) {
            this.cacheLevel = cacheLevel;
            return this;
        }

        //������ʾHttp������־
        public Builder setShowHttpLog(boolean showHttpLog) {
            this.showHttpLog = showHttpLog;
            return this;
        }

        //����Http������־��ʶ
        public Builder setHttpLogTAG(String logTAG){
            this.httpLogTAG = logTAG;
            return this;
        }

        //������ʾActivityLifecycle��־
        public Builder setShowLifecycleLog(boolean showLifecycleLog) {
            this.showLifecycleLog = showLifecycleLog;
            return this;
        }

        //���������ʶ����Activity/Fragment�������ڰ󶨣�
        public Builder setRequestTag(Object requestTag) {
            this.requestTag = parseRequestTag(requestTag);
            return this;
        }

        //���������ļ�Ŀ¼
        public Builder setDownloadFileDir(String downloadFileDir) {
            this.downloadFileDir = downloadFileDir;
            return this;
        }

        //����cookie�־û�
        public Builder setCookieJar(CookieJar cookieJar) {
            if (null != cookieJar)
                this.cookieJar = cookieJar;
            return this;
        }

        //���÷�������Ӧ���루Ĭ�ϣ�UTF-8��
        public Builder setResponseEncoding(@Encoding String responseEncoding) {
            this.responseEncoding = responseEncoding;
            return this;
        }
    }

    private static String parseRequestTag(Object object){
        String requestTag = null;
        if(null != object){
            requestTag = object.getClass().getName();
            if(requestTag.contains("$")){
                requestTag = requestTag.substring(0,requestTag.indexOf("$"));
            }
            if(object instanceof String
                    || object instanceof Float
                    || object instanceof Double
                    || object instanceof Integer){
                requestTag = String.valueOf(object);
            }
        }
        return requestTag;
    }


}
