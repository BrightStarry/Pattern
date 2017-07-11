package com.zx.a;

import java.util.ArrayList;
import java.util.List;

/**
 * 命令模式测试类
 */
public class CommandTest {
    public static void main(String[] args) {
        //调用者(发布者)
        Caller caller = new Caller("调用者A");
        //调用类(MQ)
        Invoker invoker = new Invoker();
        //调用者发送命令
        caller.putACommand(invoker,new Executor(),"A命令");
        caller.putACommand(invoker,new Executor(),"B命令");
        caller.putACommand(invoker,new Executor(),"C命令");
        //调用类 分配并执行队列中的命令
        invoker.assign();
    }
}

/**
 * 调用者(订阅者)
 */
class Caller {
    //调用者名字
    private String name;
    //构造函数，传入调用者名字
    public Caller(String name) {
        this.name = name;
    }

    //发送A命令到调用类 ,指定要发送到的调用类(MQ)和命令的参数值,以及实现者
    public void putACommand(Invoker invoker,Executor executor,String value) {
        invoker.receive(new ACommand(executor,value));
    }
}

/**
 * 调用类(MQ)
 */
class Invoker {
    private List<Command> commandList = new ArrayList<>();

    //接收命令
    public void receive(Command command) {
        commandList.add(command);
    }

    //分配任务
    public void assign(){
        //遍历整个队列
        for (int i = 0; i < commandList.size(); i++) {
            //取出任务
            Command command = commandList.get(i);
            //执行任务
            command.execute();
        }
        //清空
        commandList.clear();
    }

}

/**
 * 命令接口
 */
abstract class Command {
    //实现者（订阅者）
    protected Executor executor;
    //命令参数
    protected String value;
    //调用实现者执行命令
    public abstract void execute();

    //构造时传入实现者
    public Command(Executor executor,String value) {
        this.executor = executor;
        this.value = value;
    }
}

/**
 * 具体命令A
 */
class ACommand extends Command {
    //调用实现者的 处理方法 处理实现命令
    @Override
    public void execute() {
        executor.handlerCommand(this);
    }
    //调用父类构造方法，指定实现者,以及命令参数
    public ACommand(Executor executor,String value) {
        super(executor,value);
    }
}


/**
 * 实现者(订阅者)
 */
class Executor {
    public void handlerCommand(Command command){
        System.out.println("开始实现处理命令");
    }
}
