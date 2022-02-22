package com.sea.hook;

import java.lang.reflect.Member;

public interface MethodParameter {

    Member getMethod();

    Object getThisObject();

    Object[] getArgs();

    Object getResult();

    void setResult(Object result);

    Throwable getThrowable();

    boolean hasThrowable();

    void setThrowable(Throwable throwable);

    Object getResultOrThrowable() throws Throwable;

}