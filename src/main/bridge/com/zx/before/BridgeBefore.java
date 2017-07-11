package com.zx.before;

/**
 * 桥接模式测试类-使用模式前
 * 传统的有多纬度变化的类的关系
 */
public class BridgeBefore {
}

/**
 * 英雄接口
 */
abstract class Hero {
    //英雄展示自己
    public abstract void show();
    //英雄定位类型
    public abstract void type();
}

/**
 * AD英雄
 */
abstract class ADHero extends Hero{
    @Override
    public void show() {
        System.out.println("我是AD英雄");
    }

}
/**
 * AP英雄
 */
abstract class APHero extends Hero{
    @Override
    public void show() {
        System.out.println("我是AP英雄");
    }


}

/**
 * AD英雄-泰隆
 */
class ADTaiLong extends ADHero {
    @Override
    public void type() {
        System.out.println("刺客");
    }
}
/**
 * AD英雄-小丑
 */
class ADXiaoChou extends ADHero {
    @Override
    public void type() {
        System.out.println("打野");
    }
}
/**
 * AP英雄-妖姬
 */
class APYaoJi extends APHero {
    @Override
    public void type() {
        System.out.println("刺客");
    }
}
/**
 * AP英雄-豹女
 */
class APBaoNv extends APHero {
    @Override
    public void type() {
        System.out.println("打野");
    }
}


