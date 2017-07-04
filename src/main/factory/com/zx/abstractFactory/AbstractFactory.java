package com.zx.abstractFactory;

import java.util.Observable;
import java.util.Observer;

/**
 * 抽象工厂接口
 */
public interface AbstractFactory {
    //创建英雄
    Hero createHero();
    //创建地图
    Map createMap();
}

/**
 * 1系列产品具体类:生产1系列的多个产品
 */
class OneFactory implements AbstractFactory {
    @Override
    public Hero createHero() {
        return new XiaoChou();
    }
    @Override
    public Map createMap() {
        return new ZhaoHuanShiXiaGu();
    }
}

/**
 * 2系列产品具体类:生产2系列的多个产品
 */
class TwoFactory implements AbstractFactory {
    @Override
    public Hero createHero() {
        return new VN();
    }
    @Override
    public Map createMap() {
        return new ShenYuan();
    }
}


/**
 * 抽象产品A ：英雄
 */
interface Hero{
    void doSkill();
}

/**
 * 抽象产品B：地图
 */
interface Map {
    void init();
}

/**
 * 具体产品A1：XiaoChou (1系列)
 */
class XiaoChou implements Hero {
    @Override
    public void doSkill() {
        System.out.println("QQWREREQRQWE");
    }
}

/**
 * 具体产品A2：VN （2系列）
 */
class VN implements Hero {
    @Override
    public void doSkill() {
        System.out.println("AAAAAAAAA");
    }
}

/**
 * 具体产品B1：召唤师峡谷(1系列)
 */
class ZhaoHuanShiXiaGu implements Map {
    @Override
    public void init() {
        System.out.println("正在初始化 召唤师峡谷 地图");
    }
}

/**
 * 具体产品B2：深渊(2系列)
 */
class ShenYuan implements Map {
    @Override
    public void init() {
        System.out.println("正在初始化 深渊 地图");
    }
}
