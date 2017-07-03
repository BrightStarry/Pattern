package com.zx.simple;



/**
 * 工厂类
 */
public class HeroFactory {
    private HeroFactory(){}
    public static Hero createHero(String heroName){
        if(heroName.equals("XiaoChou")){
            return new XiaoChou();
        } else if (heroName.equals("AKL")) {
            return new AKL();
        }
        return null;
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

