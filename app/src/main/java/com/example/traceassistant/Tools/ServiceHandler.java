package com.example.traceassistant.Tools;

import android.os.Handler;

/**
 * 存储全局单例handler
 */
public class ServiceHandler {
    private static Handler handler;
    private ServiceHandler(){}
    public static Handler getHandler() {
        return handler;
    }

    public static void setHandler(Handler _handler) {
        handler = _handler;
    }
}
