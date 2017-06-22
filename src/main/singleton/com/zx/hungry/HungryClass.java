package com.zx.hungry;

/**
 * 饿汉模式
 */
public class HungryClass {
    //私有化构造方法
    private HungryClass(){}
    //直接创建对应的实例
    private static HungryClass hungryClass = new HungryClass();
    //获取实例的方法
    public static HungryClass getInstance() {
        return hungryClass;
    }
}
