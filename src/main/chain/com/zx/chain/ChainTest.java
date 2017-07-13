package com.zx.chain;


/**
 * 职责链模式测试类
 */
public class ChainTest {
    public static void main(String[] args) {
        //创建若干请求处理类，并进行职责链关联
        IHandler handler1 = new Handler(1);
        IHandler handler2 = new Handler(2);
        IHandler handler3 = new Handler(3);
        IHandler handler4 = new Handler(4);
        IHandler handler5 = new Handler(5);
        //进行关联
        handler1.setSuccessor(handler2);
        handler2.setSuccessor(handler3);
        handler3.setSuccessor(handler4);
        handler4.setSuccessor(handler5);

        //创建一个请求
        Request request = new Request("请求1",4);

        //进行请求
        handler1.handler(request);

    }
}

/**
 * 请求处理类管理器 - 此类非必须，如果管理处理类或者知道是哪个处理类处理了请求，可以使用该类
 *
 * 在一种情况下，例如刚刚调用该管理器处理了一个请求，且处理成功；那么如果想要获取是哪个处理类处理了上一个请求，就可以在管理器中在定一个一个
 * 最近处理类(记录最近一个成功处理请求的处理类) 的属性，然后get即可。具体细节不赘述，简单。
 */
class HandlerManager {
    private IHandler head;//第一个处理类
    private IHandler last;//最后一个处理类

    //将处理类注册/添加到管理器中
    public void registerHandler(IHandler handler) {
        //如果没有任何处理类
        if (head == null) {
            //将首尾处理类都赋值为该处理类
            last = head = handler;
        } else {
            //否则
            //将之前的尾处理器的下一个链接到新增的处理器上
            last.setSuccessor(handler);
            //然后将尾指针指向新增的处理器
            last = handler;
        }
    }
    //处理请求
    public boolean handler(Request request) {
        return head.handler(request);
    }
}

/**
 * 请求类，封装请求，将该类交由请求处理类处理
 */
class Request {
    private String name;
    private Integer index;
    public Request(String name,Integer index) {
        this.name = name;
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public Integer getIndex() {
        return index;
    }
}

/**
 * 处理类接口
 */
interface IHandler {
    boolean handler(Request request);//处理请求
    void setSuccessor(IHandler nextHandler);//设置下一个处理类
}

/**
 * 处理类，实现处理类接口
 */
class Handler implements IHandler {
    private Integer index;//下标，用来区分处理类对象
    private IHandler nextHandler;//下一个处理类的接口
    //处理请求,
    @Override
    public boolean handler(Request request) {
        //如果处理类索引能够整除请求类索引，则进行处理
        if (this.index % request.getIndex() == 0) {
            System.out.println("请求：" + request.getName() +" 已经被处理类:" + index + " 处理!");
            return true;
        }
        //否则判断下一个处理类是否存在，如果不存在，直接返回false，否则就调用下一个处理类进行处理
        return nextHandler == null ? false : nextHandler.handler(request);
    }
    //设置下一个处理该请求的请求处理类
    @Override
    public void setSuccessor(IHandler nextHandler) {
        this.nextHandler = nextHandler;
    }
    //构造时设置下标
    public Handler(Integer index) {
        this.index = index;
    }
}

