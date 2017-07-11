package com.zx.test;

import java.util.HashMap;
import java.util.Map;

/**
 * 亨元模式测试类
 */
public class FlyweightTest {
    public static void main(String[] args) {
        //对象管理器
        HeroManager heroManager = new HeroManager();
        Role xiaoChou = new Role(heroManager.getHero("小丑"));
        Role baoNv = new Role(heroManager.getHero("豹女"));
        xiaoChou.doQ();
        baoNv.doQ();
    }
}

/**
 * 亨元模式管理器 - 用来复用对象（英雄基类）-单例（就不写了）
 */
class HeroManager {
    //存储要复用的对象的集合
    private Map<String,Hero> heroMap = new HashMap<>();

    //获取英雄实例-根据英雄名字
    public Hero getHero(String name) {
        //尝试从map中获取对象
        Hero hero = heroMap.get(name);
        //如果为空，表示从未创建过该对象，开始创建
        if (null == hero) {
            if (name.equals("豹女")) {
                hero = new Hero("豹女",new BaoNvSkill());
            } else if (name.equals("小丑")) {
                hero = new Hero("小丑",new XiaoChouSkill());
            }
            //放入集合
            heroMap.put(name,hero);
        }
        return hero;
    }
}

/**
 * 英雄基类，要复用的对象-无法改变的属性
 */
class Hero {
    private String name;//英雄名字
    private Skill skill;//每个英雄有各自的技能实现
    //每个英雄的技能实现 使用接口的实现  （策略模式）
    public void doQ(){
        skill.doQ();
    }
    public void doW() {
        skill.doW();
    }
    public Hero(String name,Skill skill) {
        this.name = name;
        this.skill = skill;
    }
}

/**
 * 角色类-组合英雄基类和它会改变的一些属性
 */
class Role {
    private Hero hero;//英雄基类
    private Integer hp;//血量
    private Integer mp;//蓝量
    //释放技能
    public void doQ(){
        hero.doQ();
    }
    public void doW() {
        hero.doW();
    }
    //创建时传入Hero英雄基类
    public Role(Hero hero) {
        this.hero = hero;
    }
    public Integer getHp() {
        return hp;
    }
    public void setHp(Integer hp) {
        this.hp = hp;
    }
    public Integer getMp() {
        return mp;
    }
    public void setMp(Integer mp) {
        this.mp = mp;
    }
}

/**
 * 英雄技能接口-每个英雄有单独实现类
 */
interface Skill {
    void doQ();
    void doW();
}

/**
 * 小丑技能接口
 */
class XiaoChouSkill implements Skill {
    @Override
    public void doQ() {
        System.out.println("小丑的Q");
    }
    @Override
    public void doW() {
        System.out.println("小丑的W");
    }
}

/**
 * 豹女技能接口
 */
class BaoNvSkill implements Skill {
    @Override
    public void doQ() {
        System.out.println("豹女的Q");
    }
    @Override
    public void doW() {
        System.out.println("豹女的W");
    }
}


