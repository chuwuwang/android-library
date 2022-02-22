package com.sea.android.library;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.sea.hook.MethodHook;
import com.sea.hook.MethodParameter;
import com.sea.hook.PHook;
import com.sea.library.common.logger.Logger;

public class App extends Application {

    private static final String TAG = "App";

    @Override
    public void onCreate() {
        super.onCreate();
        hook();
    }

    private static void hook() {
        MethodHook methodHook = new MethodHook() {

            @Override
            public void beforeHookedMethod(MethodParameter param) throws Throwable {
                Logger.e(TAG, "beforeHookedMethod param: " + param);
            }

            @Override
            public void afterHookedMethod(MethodParameter param) throws Throwable {
                Logger.e(TAG, "afterHookedMethod param: " + param);
            }

        };
        PHook.findAndHookMethod(ImageView.class, "setImageBitmap", Bitmap.class, methodHook);
        PHook.findAndHookMethod(ImageView.class, "setImageResource", Integer.class, methodHook);
        PHook.findAndHookMethod(ImageView.class, "setImageDrawable", Drawable.class, methodHook);
    }

}