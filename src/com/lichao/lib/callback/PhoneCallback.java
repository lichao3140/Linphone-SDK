package com.lichao.lib.callback;

import org.linphone.core.LinphoneCall;

/**
 * ״̬�ص�
 */

public abstract class PhoneCallback {
    /**
     * ����״̬
     * @param linphoneCall
     */
    public void incomingCall(LinphoneCall linphoneCall) {}

    /**
     * ���г�ʼ��
     */
    public void outgoingInit() {}

    /**
     * �绰��ͨ
     */
    public void callConnected() {}

    /**
     * �绰�Ҷ�
     */
    public void callEnd() {}

    /**
     * �ͷ�ͨ��
     */
    public void callReleased() {}

    /**
     * ����ʧ��
     */
    public void error() {}
}