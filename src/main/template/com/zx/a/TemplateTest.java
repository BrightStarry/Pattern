package com.zx.a;

/**
 * 模版方法测试
 */
public class TemplateTest {
    public static void main(String[] args) {
        AKLIntro aklIntro = new AKLIntro();
        System.out.println(aklIntro.createIntro());
    }
}

/**
 * 模版接口
 */
interface HeroIntroFactory{
    //生成英雄简介的方法
    String createIntro();
}
/**
 * 模版类
 */
abstract class CreateIntroTemplate implements HeroIntroFactory{
    /**
     * 生成英雄简介的方法
     */
    @Override
    public String createIntro() {
        //所有简介开头都需如此
        StringBuilder sb = new StringBuilder("在遥远的东方，有一片大陆，世人称之为符文大陆");
        //然后是英雄出场的介绍
        sb.append(come());
        //然后是英雄的一生
        sb.append(body());
        //统一的结尾
        sb.append("这便是他的故事，这般结束！");
        return sb.toString();
    }
    /**
     * 英雄出场介绍,留给子类实现，也可以不实现
     */
    abstract String come();
    /**
     * 英雄的一生故事，留给子类实现，也可以不是实现
     */
    abstract String body();
}
/**
 *模版子类
 */
class AKLIntro extends CreateIntroTemplate{

    @Override
    String come() {
        return "是夜，皎皎月光流动，只见一黑衣蒙面的身影一闪而过。";
    }

    @Override
    String body() {
        return "阿卡丽经历生死、爱恨，终此一生，未曾离开符文大陆。";
    }
}
