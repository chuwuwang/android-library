package com.sea.hook.sand;

import com.sea.hook.MethodHook;

import de.robv.android.xposed.XC_MethodHook;

public class SandHook_XC_MethodHook extends XC_MethodHook {

    private MethodHook methodHook;
    private SandMethodParameter methodParameter;

    public SandHook_XC_MethodHook(MethodHook methodHook) {
        super();
        this.methodHook = methodHook;
    }

    @Override
    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
        super.beforeHookedMethod(param);
        if (methodParameter == null) {
            methodParameter = new SandMethodParameter();
        }
        methodParameter.setMethodHookParam(param);
        methodHook.beforeHookedMethod(methodParameter);
    }

    @Override
    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
        super.afterHookedMethod(param);
        if (methodParameter == null) {
            methodParameter = new SandMethodParameter();
        }
        methodParameter.setMethodHookParam(param);
        methodHook.afterHookedMethod(methodParameter);
    }

}
