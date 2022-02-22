package com.sea.hook;

import java.lang.reflect.Member;

public interface IHookBridge {

    int TYPE_EPIC = 0;
    int TYPE_SAND_HOOK = 1;

    int getHookType();

    void hookMethod(Member hookMethod, MethodHook callback);

}