package com.zx.a;

import java.util.*;
import java.util.ArrayList;

/**
 * 访问者模式测试类
 */
public class VisitorTest {
    public static void main(String[] args) {
        //大街
        Street street = new Street();
        //增加女人
        street.addWoman(new Lolita("小胖",21,"C","振华中学"));
        street.addWoman(new Sister("小花",90,"A",-999));
        //增加偷窥者
        Kui kui = new Kui();
        WangTaiShan wangTaiShan = new WangTaiShan();
        //开始偷窥
        street.peep(kui);
        System.out.println("--------------------");
        street.peep(wangTaiShan);
    }
}

/**
 * 大街-包含了若干萝莉和御姐- ObjectStructure
 */
class Street {
    //女人集合
    private List<Woman> womanList = new ArrayList<>();
    //增加女人
    public void addWoman(Woman woman) {
        womanList.add(woman);
    }
    //偷窥者偷窥女人
    public void peep(Peeper peeper) {
        //遍历偷窥所有女人
        womanList.forEach(woman -> {
            woman.accept(peeper);
        });
    }
}

/**
 * 元素接口-Element
 */
interface Woman {
    //被偷窥方法
    void accept(Peeper peeper);
}

/**
 * 萝莉元素-Element实现类
 */
class Lolita implements Woman {
    private String name;//年龄
    private Integer age;//年龄
    private String cup;//罩杯
    private String school;//学校
    public Lolita(String name, Integer age, String cup, String school) {
        this.name = name;
        this.age = age;
        this.cup = cup;
        this.school = school;
    }
    //接收偷窥方法
    @Override
    public void accept(Peeper peeper) {
        //使用偷窥者的偷窥方法
        peeper.peep(this);
    }
    //get方法
    public String getName() {
        return name;
    }
    public Integer getAge() {
        return age;
    }
    public String getCup() {
        return cup;
    }
    public String getSchool() {
        return school;
    }
}

/**
 * 御姐元素-Element实现类
 */
class Sister implements Woman {
    private String name;//名字
    private Integer age;//年龄
    private String cup;//罩杯
    private Integer money;//资产
    public Sister(String name, Integer age, String cup, Integer money) {
        this.name = name;
        this.age = age;
        this.cup = cup;
        this.money = money;
    }
    //接收偷窥方法
    @Override
    public void accept(Peeper peeper) {
        //调用偷窥者的偷窥方法
        peeper.peep(this);
    }
    //get方法
    public String getName() {
        return name;
    }
    public Integer getAge() {
        return age;
    }
    public String getCup() {
        return cup;
    }
    public Integer getMoney() {
        return money;
    }
}


/**
 * 偷窥者接口-Visitor
 */
interface Peeper {
    //偷窥萝莉
    void peep(Lolita lolita);
    //偷窥御姐
    void peep(Sister sister);
}

/**
 * 小亏-萝莉偷窥者-偷窥者接口实现类
 */
class Kui implements Peeper {
    @Override
    public void peep(Lolita lolita) {
        System.out.println("嘻嘻嘻嘻!可萌的萝莉我来了!" + lolita.getAge() + "岁，在" + lolita.getSchool() + "上学");
    }
    @Override
    public void peep(Sister sister) {
        System.out.println("御姐是什么，比我高的我不喜欢-小亏如是说到");
    }
}

/**
 * 小胖（王泰山）-御姐偷窥者-偷窥者接口实现类
 */
class WangTaiShan implements Peeper {
    @Override
    public void peep(Lolita lolita) {
        System.out.println("萝莉太小了，我怕把" + lolita.getName() + "压死");
    }
    @Override
    public void peep(Sister sister) {
        System.out.println(sister.getName()+ "，资产 " + sister.getMoney() + "，百富美，我喜欢，嘿嘿嘿");
    }
}
