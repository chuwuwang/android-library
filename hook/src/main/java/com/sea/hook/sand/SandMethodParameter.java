package com.sea.hook.sand;

import com.sea.hook.MethodParameter;

import java.lang.reflect.Member;

import de.robv.android.xposed.XC_MethodHook;

public class SandMethodParameter implements MethodParameter {

    private XC_MethodHook.MethodHookParam methodHookParam;

    public SandMethodParameter() {
    }

    public void setMethodHookParam(XC_MethodHook.MethodHookParam methodHookParam) {
        this.methodHookParam = methodHookParam;
    }

    @Override
    public Member getMethod() {
        return methodHookParam.method;
    }

    @Override
    public Object getThisObject() {
        return methodHookParam.thisObject;
    }

    @Override
    public Object[] getArgs() {
        return methodHookParam.args;
    }

    @Override
    public Object getResult() {
        return methodHookParam.getResult();
    }

    @Override
    public void setResult(Object result) {
        methodHookParam.setResult(result);
    }

    @Override
    public Throwable getThrowable() {
        return methodHookParam.getThrowable();
    }

    @Override
    public boolean hasThrowable() {
        return methodHookParam.hasThrowable();
    }

    @Override
    public void setThrowable(Throwable throwable) {
        methodHookParam.setThrowable(throwable);
    }

    @Override
    public Object getResultOrThrowable() throws Throwable {
        return methodHookParam.getResultOrThrowable();
    }

}