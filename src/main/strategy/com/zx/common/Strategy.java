package com.zx.common;


/**
 * 测试类
 */
class Test {
    public static void main(String[] args) {
        Strategyer strategyer = new Strategyer(new AStratey());
        strategyer.run();
    }
}


/**
 * 策略接口
 */
public interface Strategy {
    void a();
}
/**
 * 策略具体实现A
 */
class AStratey implements Strategy{
    @Override
    public void a() {
        System.out.println("A");
    }
}
/**
 * 策略具体实现B
 */
class BStratey implements Strategy{
    @Override
    public void a() {
        System.out.println("B");
    }
}
/**
 * 策略调用者
 */
class Strategyer {
    //将策略接口作为属性
    private Strategy strategy;
    //在创建时传入要使用的策略
    public Strategyer(Strategy strategy){
        this.strategy = strategy;
    }
    //替换策略实现
    public void setStrategy(Strategy strategy){
        this.strategy =strategy;
    }
    //要运行的具体方法，在方法中需要调用不同的策略实现
    public void run(){
        strategy.a();
    }
}
