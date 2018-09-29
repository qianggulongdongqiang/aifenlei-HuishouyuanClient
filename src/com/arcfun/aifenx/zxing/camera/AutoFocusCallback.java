package com.arcfun.aifenx.zxing.camera;

import com.arcfun.aifenx.utils.LogUtils;

import android.hardware.Camera;
import android.os.Handler;
import android.os.Message;

final class AutoFocusCallback implements Camera.AutoFocusCallback {

    private static final String TAG = "AutoFocusCallback";

    private static final long AUTOFOCUS_DELAY = 1500L;

    private Handler autoFocusHandler;
    private int autoFocusMsg;

    void setHandler(Handler autoFocusHandler, int autoFocusMsg) {
        this.autoFocusHandler = autoFocusHandler;
        this.autoFocusMsg = autoFocusMsg;
    }

    @Override
    public void onAutoFocus(boolean success, Camera camera) {
        if (autoFocusHandler != null) {
            Message message = autoFocusHandler.obtainMessage(autoFocusMsg,
                    success);
            autoFocusHandler.sendMessageDelayed(message, AUTOFOCUS_DELAY);
            autoFocusHandler = null;
        } else {
            LogUtils.d(TAG, "Got auto-focus callback, but no handler for it");
        }
    }

}