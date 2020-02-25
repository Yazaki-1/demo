package com.example.lib.core.util;


public class AuthContextHelper {

    //创建一个Integer型的线程本地变量
    private static ThreadLocal<Object> auth = new ThreadLocal<Object>();

    public <T> T getAuth() {
        return (T) auth.get();
    }

    public <T> void setAuth(T auth) {
        AuthContextHelper.auth.set(auth);
    }

    public void clear(){
        AuthContextHelper.auth.remove();
    }
}
