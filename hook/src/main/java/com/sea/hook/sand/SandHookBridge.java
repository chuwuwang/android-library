package com.sea.hook.sand;

import com.sea.hook.IHookBridge;
import com.sea.hook.MethodHook;

import java.lang.reflect.Member;

import de.robv.android.xposed.XposedBridge;

public class SandHookBridge implements IHookBridge {

    @Override
    public int getHookType() {
        return IHookBridge.TYPE_SAND_HOOK;
    }

    @Override
    public void hookMethod(Member hookMethod, MethodHook callback) {
        SandHook_XC_MethodHook instance = new SandHook_XC_MethodHook(callback);
        XposedBridge.hookMethod(hookMethod, instance);
    }

}
