package com.zx.a;

import java.util.ArrayList;
import java.util.List;

/**
 * 中介者模式测试类-测试中文类
 * 聊天室群聊
 * 极其简陋、中介者应该处理更复杂的交互
 */
public class 中介者模式测试 {
    public static void main(String[] args) {

        中介者接口 聊天室 = new 聊天室();
        用户接口 用户A = new 用户(聊天室);
        用户接口 用户B = new 用户(聊天室);

        用户A.发送消息("aaa");
        用户B.发送消息("bbb");
    }
}

/**
 * 中介者接口
 */
interface 中介者接口 {
    void 发送消息(用户接口 用户, String 消息);
}
/**
 * 聊天室-中介者
 */
class 聊天室 implements 中介者接口 {
    public List<String> 消息显示 = new ArrayList<>();
    @Override
    public void 发送消息(用户接口 用户, String 消息) {
        消息显示.add(用户.名字 + " : " +  消息);
    }
}

/**
 * 用户接口
 */
abstract class 用户接口 {
    protected 中介者接口 中介者接口;
    String 名字;
    public abstract void 发送消息(String 消息);
    public 用户接口(中介者接口 中介者接口) {
        this.中介者接口 = 中介者接口;
    }
}

/**
 * 用户
 */
class 用户 extends  用户接口 {
    @Override
    public void 发送消息(String 消息) {
        中介者接口.发送消息(this,消息);
    }

    public 用户(中介者接口 中介者接口) {
        super(中介者接口);
    }

}

