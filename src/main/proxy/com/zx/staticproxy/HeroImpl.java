package com.zx.staticproxy;

/**
 * 测试类
 */
class Test{
    public static void main(String[] args) {
        //被代理类,小丑
        Hero clown = new HeroImpl();
        //代理类
        HeroProxy heroProxy = new HeroProxy(clown);
        //释放连招
        heroProxy.basicSkill();
    }
}
/**
 * 英雄接口,被代理接口
 */
interface Hero{
    //释放各种技能
    void doQ();
    void doW();
    void doE();
    void doR();
    void doA();
}
/**
 * 英雄接口实现类，被代理类
 */
public class HeroImpl implements Hero{
    public void doQ() {
        System.out.println("一阵烟雾飘过，人消失在原地");
    }
    public void doW() {
        System.out.println("一个盒子落下");
    }
    public void doE() {
        System.out.println("飞刀从它手里飞出");
    }
    public void doR() {
        System.out.println("它瞬间变成两人");
    }
    public void doA() {
        System.out.println("普通攻击");
    }
}
/**
 * 代理类，代理Hero接口
 * 拓展英雄类，让其自动释放技能
 */
class HeroProxy {
    //使用组合，代理类中都需要被代理类的实例
    private Hero hero;
    //构造方法是注入被代理类
    public HeroProxy(Hero hero){
        this.hero = hero;
    }
    //普通连招方法
    public void basicSkill(){
        System.out.println("开始释放普通连招");
        hero.doQ();
        hero.doA();
        hero.doW();
        hero.doE();
        System.out.println("结束普通连招");
    }
}



