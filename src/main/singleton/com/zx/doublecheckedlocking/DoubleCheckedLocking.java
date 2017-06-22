package com.zx.doublecheckedlocking;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 双重检查锁模式
 */
public class DoubleCheckedLocking {
    //私有化构造方法
    private DoubleCheckedLocking(){}
    //声明静态实例
    private static DoubleCheckedLocking doubleCheckedLocking;
    //重入锁
    private static ReentrantLock lock = new ReentrantLock();
    //获取实例
    public static DoubleCheckedLocking getInstance(){
        if(doubleCheckedLocking != null){
            lock.lock();
            if(doubleCheckedLocking != null){
                doubleCheckedLocking = new DoubleCheckedLocking();
            }
            lock.unlock();
        }
        return doubleCheckedLocking;
    }
}
