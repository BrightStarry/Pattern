package com.zx.enums;

/**
 *     枚举 实现单例
 */
public class A {
    //私有化构造方法
    private A(){}
    //内部静态枚举
    private static enum Singleton{
        INSTANCE;
        private A instance;
        Singleton(){
            instance = new A();
        }
        private A getInstance(){
            return instance;
        }
    }
    //获取实例方法
    public A getInstance(){
        return Singleton.INSTANCE.getInstance();
    }
}



