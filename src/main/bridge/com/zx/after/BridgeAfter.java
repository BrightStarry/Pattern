package com.zx.after;

/**
 * 使用桥接模式 重构 com.zx.before包
 */
public class BridgeAfter {
    public static void main(String[] args) {

    }
}

/**
 * 抽象英雄类
 */
abstract class Hero {
    //show方法接口
    protected Show show;
    //type方法接口
    protected Type type;
    //show方法，由实现类进行具体实现
    public void show(){
        show.show();
    }
    //type方法，由实现类进行具体实现
    public void type(){
        type.type();
    }
    //抽象方法，留给具体英雄类自己实现
    public abstract void aMethod();

    //构造方法传入接口实现类
    public Hero(Show show,Type type){
        this.show = show;
        this.type = type;
    }

    public void setShow(Show show) {
        this.show = show;
    }

    public void setType(Type type) {
        this.type = type;
    }
}

/**
 * 具体英雄类
 */
class AHero extends Hero{
    //
    @Override
    public void aMethod() {

    }
    //调用父类的构造方法
    public AHero(Show show,Type type) {
        super(show,type);
    }

}

/**
 * show方法接口
 */
interface Show {
    void show();
}
/**
 * AD英雄show方法
 */
class ADShow implements Show{
    @Override
    public void show() {
        System.out.println("我是AD英雄");
    }
}
/**
 * AP英雄show方法
 */
class APShow implements Show{
    @Override
    public void show() {
        System.out.println("我是AP英雄");
    }
}

/**
 * type方法接口
 */
interface Type {
    void type();
}

/**
 * AD英雄type方法
 */
class ADAttack implements Type {
    @Override
    public void type() {
        System.out.println("平A");
    }
}
/**
 * AP英雄type方法
 */
class APAttack implements Type {
    @Override
    public void type() {
        System.out.println("技能");
    }
}

