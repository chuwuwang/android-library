package com.sea.hook;

public interface MethodHook {

    void beforeHookedMethod(MethodParameter param) throws Throwable;

    void afterHookedMethod(MethodParameter param) throws Throwable;

}