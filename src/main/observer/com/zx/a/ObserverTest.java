package com.zx.a;

import java.util.Observable;
import java.util.Observer;

/**
 * 观察者测试类
 */
public class ObserverTest {
    public static void main(String[] args) {

    }
}
/**
 * 可观察对象/主题/被观察对象    继承Observable接口
 */
class SystemObservable extends Observable {
    private String message;//消息 数据

    /**
     * 设置消息方法
     */
    public void setMessage(String message){
        this.message = message;
        //设置发生变化方法为true
        setChanged();
        //通知所有观察者
        notifyObservers(message);
    }

    //get方法，当使用拉模型，需要观察者自己从该类(被观察者)中拉取需要的数据时，需要提供
    public String getMessage() {
        return message;
    }
}

/**
 * 观察者 ,实现Observer接口
 */
class CustomObserver implements Observer {
    private String message;//消息-从被观察者处接受的消息
    private Observable observable;//该观察者订阅的被观察者； 可以定义成数组，订阅多个被观察者

    @Override
    public void update(Observable o, Object arg) {
        //如果是 系统观察者
        if(o instanceof SystemObservable){
            //拉模型-自己从被观察者里拉取
            this.message = ((SystemObservable) o).getMessage();

            //推模型-直接从被观察者推送来的数据中获取
            this.message = String.valueOf(arg);
        }
        System.out.println(this.message);
    }
}
