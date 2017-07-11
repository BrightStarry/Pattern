package com.zx.a;


/**
 * 测试
 */
public class Test {
    public static void main(String[] args) {
        HeroDecorator heroDecorator = new HeroDecorator(new Hero());
        heroDecorator.doQ();
        heroDecorator.doW();
    }
}




/**
 * 超类 ： 装饰者模式所有类都是其子类
 */
interface Component {

    void doQ();
    void doW();
}

/**
 * 装饰者超类
 */
abstract class AbstractDecorator implements Component {
    //被装饰者,用它来复用方法
    protected Component component;
    //构造时传入装饰者
    public AbstractDecorator(Component component) {
        this.component = component;
    }
}

/**
 * 被装饰者
 */
class Hero implements Component {
    @Override
    public void doQ() {
        System.out.println("吃我的Q技能");
    }
    @Override
    public void doW() {
        System.out.println("一个W撸飞你");
    }
}

/*
    装饰者
 */
class HeroDecorator extends AbstractDecorator {
    //构造时调用父类的构造函数
    public HeroDecorator(Component component) {
        super(component);
    }
    @Override
    public void doQ() {
        System.out.println("前摇");
        component.doQ();
        System.out.println("后摇");
    }
    @Override
    public void doW() {
        System.out.println("前摇");
        component.doW();
        System.out.println("后摇");
    }
}