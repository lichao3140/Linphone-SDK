package com.lichao.lib;

import java.io.IOException;
import java.util.HashMap;
import android.content.Context;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.view.SurfaceView;
import org.linphone.core.LinphoneCallParams;
import org.linphone.core.LinphoneCore;
import org.linphone.core.LinphoneCoreException;
import org.linphone.mediastream.Log;
import org.linphone.mediastream.video.AndroidVideoWindowImpl;
import com.google.gson.Gson;
import com.lichao.lib.callback.PhoneCallback;
import com.lichao.lib.callback.RegistrationCallback;
import com.lichao.lib.linphone.LinphoneManager;
import com.lichao.lib.linphone.LinphoneUtils;
import com.lichao.lib.linphone.PhoneBean;
import com.lichao.lib.model.RegisterModel;
import com.lichao.lib.service.LinphoneService;
import com.lichao.lib.util.Constant;
import com.okhttplib.HttpInfo;
import com.okhttplib.OkHttpUtil;
import com.okhttplib.callback.Callback;
import static java.lang.Thread.sleep;

/**
 * LinPhone SDk方法实现
 */

public class QuhwaLinphone {
    private static ServiceWaitThread mServiceWaitThread;
    private static String mUsername, mPassword;
    private static String mServerIP = Constant.SIP_SERVER_IP;
    private static AndroidVideoWindowImpl mAndroidVideoWindow;
    private static SurfaceView mRenderingView, mPreviewView;

    /**
     * 开启服务
     * @param context 上下文
     */
    public static void startService(Context context) {
        if (!LinphoneService.isReady()) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setClass(context, LinphoneService.class);
            context.startService(intent);
        }
    }
    
    public static void loginByPhone(String phone) {
    	final HashMap<String, String> maps = new HashMap<String, String>();
    	final String username = phone;
    	final String password = "quhwapwd" + phone;
    	maps.put("username", username);
    	maps.put("password", password);
    	OkHttpUtil.getDefault().doPostAsync(
    			HttpInfo.Builder().setUrl(Constant.REG_SIP_URL).addParams(maps).build(), 
    			new Callback() {
					
					@Override
					public void onSuccess(HttpInfo info) throws IOException {
						String result = info.getRetDetail();
						RegisterModel registerModel = new Gson().fromJson(result, RegisterModel.class);
						int code = registerModel.getCode();
						if (code == 1) {
							String sip = registerModel.getUserInfo().getSipid();
							String pwd = registerModel.getUserInfo().getSipPasswd();
							setAccount(sip, pwd, mServerIP);
							login();
							Log.e("regiter result:" + "\ncode:" + code + "\nsip:" + sip + "\npwd:" + pwd);
						} else if (code == 7) {
							loginSIP(username, password);
						} else {
							Log.e("regiter failure:" + result);
						}
					}
					
					@Override
					public void onFailure(HttpInfo info) throws IOException {
						String result = info.getRetDetail();
						Log.e("connect failure:" + result);
					}
				});
    }
    
    public static void loginSIP(String username, String password) {
    	final HashMap<String, String> maps = new HashMap<String, String>();
    	maps.put("username", username);
    	maps.put("password", password);
    	OkHttpUtil.getDefault().doPostAsync(
    			HttpInfo.Builder().setUrl(Constant.LOGIN_SIP_URL).addParams(maps).build(),
    			new Callback() {
					
					@Override
					public void onSuccess(HttpInfo info) throws IOException {
						String result = info.getRetDetail();
						RegisterModel registerModel = new Gson().fromJson(result, RegisterModel.class);
						int code = registerModel.getCode();
						if (code == 1) {
							String sip = registerModel.getUserInfo().getSipid();
							String pwd = registerModel.getUserInfo().getSipPasswd();
							setAccount(sip, pwd, mServerIP);
							login();
							Log.e("login result:" + "\ncode:" + code + "\nsip:" + sip + "\npwd:" + pwd);
						} else {
							Log.e("login failure:" + result);
						}
					}
					
					@Override
					public void onFailure(HttpInfo info) throws IOException {
						String result = info.getRetDetail();
						Log.e("connect failure:" + result);
					}
				});
    }

    /**
     * 设置 sip 账户信息
     * @param username sip 账户
     * @param password 密码
     * @param serverIP sip 服务器
     */
    public static void setAccount(String username, String password, String serverIP) {
        mUsername = username;
        mPassword = password;
        mServerIP = serverIP;
    }

    /**
     * 添加注册状态、通话状态回调
     * @param phoneCallback 通话回调
     * @param registrationCallback 注册状态回调
     */
    public static void addCallback(RegistrationCallback registrationCallback,
                                   PhoneCallback phoneCallback) {
        if (LinphoneService.isReady()) {
            LinphoneService.addRegistrationCallback(registrationCallback);
            LinphoneService.addPhoneCallback(phoneCallback);
        } else {
            mServiceWaitThread = new ServiceWaitThread(registrationCallback, phoneCallback);
            mServiceWaitThread.start();
        }
    }

    /**
     * 登录到 SIP 服务器
     */
    public static void login() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!LinphoneService.isReady()) {
                    try {
                        sleep(80);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                loginToServer();
            }
        }).start();
    }

    /**
     * 呼叫指定号码
     * @param num 呼叫号码
     */
    public static void callTo(String num, boolean isVideoCall) {
        if (!LinphoneService.isReady() || !LinphoneManager.isInstanceiated()) {
            return;
        }
        if (!num.equals("")) {
            PhoneBean phone = new PhoneBean();
            phone.setUserName(num);
            phone.setHost(mServerIP);
            LinphoneUtils.getInstance().startSingleCallingTo(phone, isVideoCall);
        }
    }

    /**
     * 接听来电
     */
    public static void acceptCall() {
        try {
            LinphoneManager.getLc().acceptCall(LinphoneManager.getLc().getCurrentCall());
        } catch (LinphoneCoreException e) {
            e.printStackTrace();
        }
    }

    /**
     * 挂断当前通话
     */
    public static void hangUp() {
        LinphoneUtils.getInstance().hangUp();
    }

    /**
     * 切换静音
     * @param isMicMuted 是否静音
     */
    public static void toggleMicro(boolean isMicMuted) {
        LinphoneUtils.getInstance().toggleMicro(isMicMuted);
    }

    /**
     * 切换免提
     * @param isSpeakerEnabled 是否免提
     */
    public static void toggleSpeaker(boolean isSpeakerEnabled) {
        LinphoneUtils.getInstance().toggleSpeaker(isSpeakerEnabled);
    }
    
    /**
     * 发送文本消息
     * @param user 接收方用户名
     * @param msg 消息内容
     */
    public static void SendMessage(String user, String msg) {
    	 if (!LinphoneService.isReady() || !LinphoneManager.isInstanceiated()) {
             return;
         }
         if (!user.equals("")) {
             PhoneBean phone = new PhoneBean();
             phone.setUserName(user);
             phone.setHost(mServerIP);
             LinphoneUtils.getInstance().sendTextMessage(phone, msg);
         }
    }

    private static class ServiceWaitThread extends Thread {
        private PhoneCallback mPhoneCallback;
        private RegistrationCallback mRegistrationCallback;

        ServiceWaitThread(RegistrationCallback registrationCallback, PhoneCallback phoneCallback) {
            mRegistrationCallback = registrationCallback;
            mPhoneCallback = phoneCallback;
        }

        @Override
        public void run() {
            super.run();
            while (!LinphoneService.isReady()) {
                try {
                    sleep(80);
                } catch (InterruptedException e) {
                    throw new RuntimeException("waiting thread sleep() has been interrupted");
                }
            }
            LinphoneService.addPhoneCallback(mPhoneCallback);
            LinphoneService.addRegistrationCallback(mRegistrationCallback);
            mServiceWaitThread = null;
        }
    }

    /**
     * 登录 SIP 服务器
     */
    private static void loginToServer() {
        try {
            LinphoneUtils.getInstance().registerUserAuth(mUsername, mPassword, mServerIP);
        } catch (LinphoneCoreException e) {
            e.printStackTrace();
        }
    }

    public static boolean getVideoEnabled() {
        LinphoneCallParams remoteParams = LinphoneManager.getLc().getCurrentCall().getRemoteParams();
        return remoteParams != null && remoteParams.getVideoEnabled();
    }

    /**
     * 设置 SurfaceView
     * @param renderingView 远程 SurfaceView
     * @param previewView 本地 SurfaceView
     */
    public static void setAndroidVideoWindow(final SurfaceView[] renderingView, final SurfaceView[] previewView) {
        mRenderingView = renderingView[0];
        mPreviewView = previewView[0];
        fixZOrder(mRenderingView, mPreviewView);
        mAndroidVideoWindow = new AndroidVideoWindowImpl(renderingView[0], previewView[0], new AndroidVideoWindowImpl.VideoWindowListener() {
            @Override
            public void onVideoRenderingSurfaceReady(AndroidVideoWindowImpl androidVideoWindow, SurfaceView surfaceView) {
                setVideoWindow(androidVideoWindow);
                renderingView[0] = surfaceView;
            }

            @Override
            public void onVideoRenderingSurfaceDestroyed(AndroidVideoWindowImpl androidVideoWindow) {
                removeVideoWindow();
            }

            @Override
            public void onVideoPreviewSurfaceReady(AndroidVideoWindowImpl androidVideoWindow, SurfaceView surfaceView) {
                mPreviewView = surfaceView;
                setPreviewWindow(mPreviewView);
            }

            @Override
            public void onVideoPreviewSurfaceDestroyed(AndroidVideoWindowImpl androidVideoWindow) {
                removePreviewWindow();
            }
        });
    }

    /**
     * onResume
     */
    public static void onResume() {
        if (mRenderingView != null) {
            ((GLSurfaceView) mRenderingView).onResume();
        }

        if (mAndroidVideoWindow != null) {
            synchronized (mAndroidVideoWindow) {
                LinphoneManager.getLc().setVideoWindow(mAndroidVideoWindow);
            }
        }
    }

    /**
     * onPause
     */
    public static void onPause() {
        if (mAndroidVideoWindow != null) {
            synchronized (mAndroidVideoWindow) {
                LinphoneManager.getLc().setVideoWindow(null);
            }
        }

        if (mRenderingView != null) {
            ((GLSurfaceView) mRenderingView).onPause();
        }
    }

    /**
     * onDestroy
     */
    public static void onDestroy() {
        mPreviewView = null;
        mRenderingView = null;

        if (mAndroidVideoWindow != null) {
            mAndroidVideoWindow.release();
            mAndroidVideoWindow = null;
        }
    }

    private static void fixZOrder(SurfaceView rendering, SurfaceView preview) {
        rendering.setZOrderOnTop(false);
        preview.setZOrderOnTop(true);
        preview.setZOrderMediaOverlay(true); // Needed to be able to display control layout over
    }

    private static void setVideoWindow(Object o) {
        LinphoneManager.getLc().setVideoWindow(o);
    }

    private static void removeVideoWindow() {
        LinphoneCore linphoneCore = LinphoneManager.getLc();
        if (linphoneCore != null) {
            linphoneCore.setVideoWindow(null);
        }
    }

    private static void setPreviewWindow(Object o) {
        LinphoneManager.getLc().setPreviewWindow(o);
    }

    private static void removePreviewWindow() {
        LinphoneManager.getLc().setPreviewWindow(null);
    }

    /**
     * 获取 LinphoneCore
     * @return LinphoneCore
     */
    public static LinphoneCore getLC() {
        return LinphoneManager.getLc();
    }
}