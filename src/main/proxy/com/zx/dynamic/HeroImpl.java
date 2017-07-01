package com.zx.dynamic;

import sun.misc.ProxyGenerator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * 测试类
 */
class Test{
    public static void main(String[] args) throws IOException {
        //创建一个代理调用类，并传入一个被代理类对象
        HeroProxy heroProxy = new HeroProxy(new HeroImpl());
        //使用代理调用类创建一个动态代理类
        final Hero hero = heroProxy.getGeroProxy();

        //调用动态代理类的方法
        hero.doA();
        hero.doQ();
        hero.doR();

        //保存到本地
        saveClassToLocal(hero);


        //当时无意中试了下JDK8的stream 先放着，下次再学
//        List<Integer> nums = new ArrayList<>();
//        nums.add(1);
//        nums.add(2);
//        nums.add(null);
//        nums.add(4);
//        long count = nums.stream().filter(num -> num != null).count();
//        System.out.println(count);

    }

    /**
     * 将生成的动态代理类的字节码文件保存到本地
     * 可以反编译，来查看动态代理类的内部细节
     */
    public static void saveClassToLocal(Hero proxy) throws IOException {
        byte[] proxyClass = ProxyGenerator.generateProxyClass(proxy.getClass()
                .getSimpleName(), proxy.getClass().getInterfaces());
        //将字节码文件保存到D盘，文件名为$Proxy0.class
        FileOutputStream outputStream = new FileOutputStream(new File(
                "d:\\$Proxy0.class"));
        outputStream.write(proxyClass);
        outputStream.flush();
        outputStream.close();
    }
}



/**
 * 英雄接口,被代理接口
 */
interface Hero{
    //释放各种技能
    String doQ();
    void doW();
    void doE();
    void doR();
    boolean doA();
}
/**
 * 英雄接口实现类，被代理类
 */
public  class HeroImpl implements Hero {
    public String doQ() {
        System.out.println("一阵烟雾飘过，人消失在原地");
        return "result test";
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
    public boolean doA() {
        System.out.println("普通攻击");
        return false;//只有在被代理的时候才返回ture
    }


}
/**
 * 动态代理类
 */
class HeroProxy implements InvocationHandler{
    //被代理对象
    private Hero hero;

    //注入被代理对象实例
    public HeroProxy(Hero hero){
        this.hero = hero;
    }

    /**
     *获取代理类
     */
    public Hero getGeroProxy(){
        return (Hero) Proxy.newProxyInstance(getClass().getClassLoader(),hero.getClass().getInterfaces(),this);
    }
    /**
     * 调用被代理类的方法并
     * Object proxy：被代理类实例
     * Method method:被代理接口的方法
     * Object[] args:被代理接口的方法的参数，基本类型会被包装
     * 返回的参数，不能为空，如果原始方法是基本类型，必须返回包装类；如果为空，抛出NullPointException;如果类型无法转换，抛出ClassCastException;
     * 被代理类的所有重写了被代理接口的方法都会进入
     */
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        /*System.out.println("进入invoke方法");
        //如果Hero这个Class和Proxy这个Class相同，或者Proxy是Hero的父接口或父类，且，方法是被代理类接口的doA()方法，给其增加前摇和后摇
        if(Hero.class.isAssignableFrom(proxy.getClass()) && method.getName().equals("doA")){
            System.out.println("执行前摇");
            //调用该方法
            method.invoke(hero,args);
            System.out.println("执行后摇");
            return true;
        }
        //如果是其他方法,直接调用并返回
        return method.invoke(hero, args);*/

        /**
         * 上面的代码可以改成如下：
         */
        System.out.println("进入invoke方法");
        if(Hero.class.isAssignableFrom(proxy.getClass()) && method.getName().equals("doA")){
            before();
        }
        //调用该方法
        Object result = method.invoke(hero, args);
        if(Hero.class.isAssignableFrom(proxy.getClass()) && method.getName().equals("doA")){
            after();
        }
        return result;
    }

    private void before(){
        System.out.println("执行前摇");
    }
    private void after(){
        System.out.println("执行后摇");
    }
}
