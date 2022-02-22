package com.sea.hook;

import com.sea.library.common.logger.Logger;

import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

public class PHook {

    private static final String TAG = "PHook";

    static IHookBridge iHookBridge = null;

    static {
        ServiceLoader<IHookBridge> iHookBridgeLoader = ServiceLoader.load(IHookBridge.class);
        Iterator<IHookBridge> iterator = iHookBridgeLoader.iterator();
        boolean hasNext = iterator.hasNext();
        if ( ! hasNext ) {
            // multi dex 的时候, 发现会找不到配置, 目前通过设置classloader来处理
            ClassLoader classLoader = IHookBridge.class.getClassLoader();
            iHookBridgeLoader = ServiceLoader.load(IHookBridge.class, classLoader);
            iterator = iHookBridgeLoader.iterator();
        }
        Logger.e(TAG, "HookBridge init has instance: " + hasNext);
        hasNext = iterator.hasNext();
        while (hasNext) {
            iHookBridge = iterator.next();
            Class<? extends IHookBridge> clazz = iHookBridge.getClass();
            Logger.e(TAG, "HookBridge init find instance: " + clazz);
            hasNext = iterator.hasNext();
        }
    }

    public static void hookAllConstructors(Class<?> clazz, MethodHook callback) {
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        boolean bool = assertObject(constructors);
        if (bool) return;
        for (Constructor<?> constructor : constructors) {
            hookMethod(constructor, callback);
        }
    }

    public static void findAllAndHookMethod(Class<?> clazz, String methodName, MethodHook methodHookCallback) {
        List<Method> methodList = findMethodList(clazz, methodName, true, (Class<?>) null);
        boolean bool = assertObject(methodList);
        if (bool) return;
        for (Method method : methodList) {
            hookMethod(method, methodHookCallback);
        }
    }

    public static void findAndHookMethod(Class<?> clazz, String methodName, Object... parameterTypesAndCallback) {
        Object callback = null;
        Class<?>[] parameterTypes = null;
        if (parameterTypesAndCallback != null && parameterTypesAndCallback.length > 0) {
            int length = parameterTypesAndCallback.length - 1;
            parameterTypes = new Class<?>[length];
            for (int i = 0; i < length; i++) {
                parameterTypes[i] = (Class<?>) parameterTypesAndCallback[i];
            }
            callback = parameterTypesAndCallback[length];
        }
        boolean bool = assertObject(callback);
        if (bool) return;
        MethodHook methodCallback = (MethodHook) callback;

        List<Method> methodList = findMethodList(clazz, methodName, parameterTypes);
        bool = assertObject(methodList);
        if (bool) return;

        for (Method method : methodList) {
            hookMethod(method, methodCallback);
        }
    }

    public static void hookMethod(Member hookMethod, MethodHook callback) {
        boolean bool = assertObject(iHookBridge);
        if (bool) return;
        try {
            iHookBridge.hookMethod(hookMethod, callback);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private static List<Method> findMethodList(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
        return findMethodList(clazz, methodName, false, parameterTypes);
    }

    private static List<Method> findMethodList(Class<?> clazz, String methodName, boolean forceCheckMethodName, Class<?>... parameterTypes) {
        List<Method> list = new ArrayList<>();
        try {
            if (forceCheckMethodName) {
                Method[] methods = clazz.getDeclaredMethods();
                for (Method method : methods) {
                    boolean bool = method.getName().equals(methodName);
                    if (bool) {
                        list.add(method);
                    }
                }
            } else {
                Method method = clazz.getDeclaredMethod(methodName, parameterTypes);
                list.add(method);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private static boolean assertObject(Object object) {
        if (null == object) {
            Logger.e(TAG, "object is null");
            return true;
        }
        if (object instanceof List) {
            List<?> list = (List<?>) object;
            if (list.size() == 0) {
                Logger.e(TAG, "object list is null");
                return true;
            }
        }
        if (object instanceof Member[]) {
            Member[] members = (Member[]) object;
            if (members.length == 0) {
                Logger.e(TAG, "object array is null");
                return true;
            }
        }
        return false;
    }

}