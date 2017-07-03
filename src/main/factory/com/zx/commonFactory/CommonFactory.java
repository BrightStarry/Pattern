package com.zx.commonFactory;

/**
 * 普通工厂模式 测试
 */
public class CommonFactory {
    //没什么好测的。。。
}

/**
 * 抽象工厂类
 */
interface HeroFactory {
    Hero createHero();
}
/**
 * VN工厂类
 */
class VNFactory implements HeroFactory{
    @Override
    public Hero createHero() {
        return new VN();
    }
}
/**
 * XiaoChou工厂类
 */
class XiaoChouFactory implements HeroFactory{
    @Override
    public Hero createHero() {
        return new XiaoChou();
    }
}
/**
 * AKL工厂类
 */
class AKLFactory implements HeroFactory{
    @Override
    public Hero createHero() {
        return new AKL();
    }
}


/**
 * 产品接口： 英雄接口
 */
interface Hero {
    void doQ();
    void doW();
    void doE();
    void doR();
}

/**
 * 具体产品类： 阿卡丽
 */
class AKL implements  Hero{
    @Override
    public void doQ() {
        System.out.println("阿卡丽Q");
    }

    @Override
    public void doW() {
        System.out.println("阿卡丽W");
    }

    @Override
    public void doE() {
        System.out.println("阿卡丽E");
    }

    @Override
    public void doR() {
        System.out.println("阿卡丽R");
    }
}
/**
 * 具体产品类: 小丑
 */
class XiaoChou implements Hero {
    @Override
    public void doQ() {
        System.out.println("小丑Q");
    }

    @Override
    public void doW() {
        System.out.println("小丑W");
    }

    @Override
    public void doE() {
        System.out.println("小丑E");
    }

    @Override
    public void doR() {
        System.out.println("小丑R");
    }
}

/**
 * 具体产品类: VN
 */
class VN implements Hero {
    @Override
    public void doQ() {
        System.out.println("VNQ");
    }

    @Override
    public void doW() {
        System.out.println("VNW");
    }

    @Override
    public void doE() {
        System.out.println("VNE");
    }

    @Override
    public void doR() {
        System.out.println("VNR");
    }
}
