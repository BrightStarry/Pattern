package com.zx.lazy;

/**
 * 单例模式-懒汉模式类
 */
public class LazyClass {
    //私有化构造方法
    private LazyClass(){}
    //声明静态对象，不创建对应实例
    private static LazyClass lazyClass;
    //获取单例实例的方法
    public static LazyClass getInstance(){
        if(lazyClass == null)
            lazyClass = new LazyClass();
        return lazyClass;
    }
}
