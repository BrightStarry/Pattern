package com.zx.staticclass;

/**
 * 静态内部类模式
 */
public class StaticClassClass {
    //私有化构造方法
    private StaticClassClass(){}
    //静态内部类
    private static class StaticClassClassInternal{
        private static StaticClassClass instance = new StaticClassClass();
    }
    //获取实例
    public static StaticClassClass getInstance(){
        return StaticClassClassInternal.instance;
    }
}
