package com.lichao.lib.callback;

/**
 * 注册回调
 */

public abstract class RegistrationCallback {
    public void registrationNone() {}

    public void registrationProgress() {}

    public void registrationOk() {}

    public void registrationCleared() {}

    public void registrationFailed() {}
}
