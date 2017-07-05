package com.zx.annotation.c;
/**
 * 下面的是 进一步扩展使用注解优化后的策略模式，使其可以支持多种优惠活动叠加
 * 具体如下：
 * 活动1 ：历史购买总额
 * 0-1000    9折  A
 * 1000-2000 8折  B
 * >2000     7折  C
 * 活动2 ：满减 本次金额
 * 0-1000 -100
 * 1000-2000 -300
 * >2000 -500
 * 活动3 : 金额数包含999，则-100
 */

import org.reflections.Reflections;

import java.io.File;
import java.io.FileFilter;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.URISyntaxException;
import java.util.*;

/**
 * 测试类
 */
public class C {
    /**
     * 经过测试。。。 使用Reflections jar比原生的慢了10倍。
     * 本来我还以为是因为里面的set转类型转换导致的，而后测试了下，发现问提在于
     * 新建Reflections类，贼慢。
     * 不过如果是建立好以后可以一直放着，重复使用的话，其实影响不大
     */
    public static void main(String[] args) {
        long i = System.currentTimeMillis();
        Customer customer = new Customer();
        customer.buy(1366.33, 998.0);
        System.out.println(customer.count());
        System.out.println(System.currentTimeMillis() - i);
    }
}

/**
 * 被嵌套注解 用来注解金额的范围，即可以用作本地消费金额范围，也可以做历史消费总额范围
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@interface MoneyRange {
    int max() default Integer.MAX_VALUE;

    int min() default Integer.MIN_VALUE;

    //用来作优先级排序 0-99 优先级从高到低
    //这个优先级其实可以嵌套类(HistoryMoneyRange或ThisMoneyRange)中，放这里的话，对业务来说，比较灵活；添加注解，稍微繁琐
    int order() default 0;
}

/**
 * 历史总额注解 嵌套MoneyRange注解
 * 被注解注解的策略类，当历史总额在注解标注的区间时，执行该策略
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@interface HistoryMoneyRange {
    MoneyRange value() default @MoneyRange;
}

/**
 * 本次金额注解， 嵌套MoneyRange注解
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@interface ThisMoneyRange {
    MoneyRange value() default @MoneyRange;
}

/**
 * 策略调用类
 */
class Customer {
    //历史购买总额
    private Double historyMoney;
    //本次购买金额
    private Double thisMoney;
    //策略接口
    private MoneyCount moneyCount;

    //用来模拟设置历史购买总额和本次购买金额,并根据历史总额设置不同的策略实现
    public void buy(Double historyMoney, Double thisMoney) {
        this.historyMoney = historyMoney;
        this.thisMoney = thisMoney;
        /**
         * 使用工厂创建实例
         */
        MoneyCountFactory moneyCountFactory = MoneyCountFactory.getInstance();
        //将创建出来的实例赋值给策略属性
        this.moneyCount = moneyCountFactory.createMoneyCount(this);
    }

    //调用策略方法
    public Double count() {
        return moneyCount.count(thisMoney);
    }

    public Double getHistoryMoney() {
        return historyMoney;
    }

    public Double getThisMoney() {
        return thisMoney;
    }
}

/**
 * 策略工厂
 * 单例
 */
class MoneyCountFactory {
    //要扫描的包名，也就是 MoneyCount接口的实现类所在的包
    private final String PACKAGE_NAME = "com.zx.annotation.c";
    //类加载器，使用相同的类加载器
    private final ClassLoader classLoader = getClass().getClassLoader();
    //存储策略实现类的集合
    private Set<Class<? extends MoneyCount>> moneyCounts;

    /**
     * 根据金额来生成指定的策略实现类
     */
    public MoneyCount createMoneyCount(Customer customer) {
        //根据两个金额判断出的本次要执行的策略(要执行那些优惠判断)
        //此处使用TreeMap是因为其可排序，使用SortedMap，是因为其可以设置优先级
        SortedMap<Integer, Class<? extends MoneyCount>> runMap = new TreeMap<>();
        //遍历策略实现类集合
        for (Class<? extends MoneyCount> moneyCount : moneyCounts) {
            //获取@HistoryMoneyRange注解的策略类
            HistoryMoneyRange historyMoneyRange = moneyCount.getAnnotation(HistoryMoneyRange.class);
            //如果在这个范围内，则使用该策略
            if (null != historyMoneyRange && customer.getHistoryMoney() >= historyMoneyRange.value().min() && customer.getHistoryMoney() < historyMoneyRange.value().max()) {
                //添加到要runMap中去
                runMap.put(historyMoneyRange.value().order(), moneyCount);
                continue;
            }
            //获取@ThisMoneyRange注解的策略类
            ThisMoneyRange thisMoneyRange = moneyCount.getAnnotation(ThisMoneyRange.class);
            //如果在该范围内，添加到runMap中
            if (null != thisMoneyRange && customer.getThisMoney() >= thisMoneyRange.value().min() && customer.getThisMoney() < thisMoneyRange.value().max()) {
                //添加到要runMap中去
                runMap.put(thisMoneyRange.value().order(), moneyCount);
            }
        }
        //返回代理类
        return MoneyCountProxy.getProxy(runMap);
    }

    /**
     * 使用Reflections jar简化初始化代码
     */
    private void initByReflections() {
        //新建扫描PACKAGE_NAME包的Reflections类
        Reflections reflections = new Reflections(PACKAGE_NAME);
        //获取该包下所有实现MoneyCount接口的类
        moneyCounts = reflections.getSubTypesOf(MoneyCount.class);
    }

    /**
     * 初始化
     */
    private void init() {
        moneyCounts = new HashSet<>();
        //获取包下所有的class文件
        File[] classFile = getClassFile();
        //获取MoneyCount接口 的类对象
        Class<MoneyCount> moneyCountClass = null;
        //使用相同的类加载器加载该接口，以及该接口的实现类
        try {
            moneyCountClass = (Class<MoneyCount>) classLoader.loadClass(MoneyCount.class.getName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("未找到策略接口");
        }
        //加载包下的所有类
        for (int i = 0; i < classFile.length; i++) {
            try {
                Class<?> tempClass = classLoader.loadClass(PACKAGE_NAME + "." + classFile[i].getName().replace(".class", ""));
                //判断tempClass是策略接口的实现类，并且不是策略接口自己
                if (moneyCountClass.isAssignableFrom(tempClass) && moneyCountClass != tempClass) {
                    //是的话，将该类对象放入 策略类集合
                    moneyCounts.add((Class<? extends MoneyCount>) tempClass);
                }
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("加载包下" + classFile[i].getName() + "时未找到");
            }
        }
    }

    /**
     * 获取要扫描的包下所有的class文件
     */
    private File[] getClassFile() {
        try {
            //从本地获取包文件
            File file = new File(classLoader.getResource(PACKAGE_NAME.replace(".", "/")).toURI());
            /**
             * 返回该路径下所有文件 FIle[]
             * 并进行过滤，过滤出class文件
             */
            return file.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    //如果文件后缀是.class,才返回
                    if (pathname.getName().endsWith(".class")) {
                        return true;
                    }
                    return false;
                }
            });
        } catch (URISyntaxException e) {
            throw new RuntimeException("策略实现类class文件获取错误");
        }
    }

    /**
     * 私有化构造方法在构造时执行初始化方法
     */
    private MoneyCountFactory() {
        init();
//        initByReflections();
    }

    /**
     * 静态内部类模式的单例
     */
    private static class MoneyCountFactoryInstance {
        private static MoneyCountFactory instance = new MoneyCountFactory();
    }

    /**
     * 获取单例实例
     */
    public static MoneyCountFactory getInstance() {
        return MoneyCountFactoryInstance.instance;
    }
}

/**
 * 策略接口动态代理类
 * 用来调用多个策略接口实现类的count()方法，进行多个活动优惠的计算
 */
class MoneyCountProxy implements InvocationHandler{
    //被代理对象集合
    //此处是Class<?>集合，也就是要执行的策略实现类的集合，可以直接通过类对象创建实例，调用其方法
    private SortedMap<Integer,Class<? extends MoneyCount>> runMap;

    //构造时传入runMap
    private MoneyCountProxy(SortedMap<Integer,Class<? extends MoneyCount>> runMap) {
        this.runMap = runMap;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //将参数转为单个的double,也就是要计算的金额
        Double result = new Double(args[0].toString());
        //如果运行的是count方法
        if (method.getName().equals("count")) {
            //遍历并调用所有要执行的策略的count()方法
            for (Class<? extends MoneyCount> temp : runMap.values()) {
                //一次执行策略算法
                result = (Double)method.invoke(temp.newInstance(),result);
                System.out.println("执行了"+temp.getName() + "余额：" + result);
            }
            return result;
        }
        return null;
    }

    //获取proxy对象
    public static MoneyCount getProxy(SortedMap<Integer,Class<? extends MoneyCount>> runMap){
        return (MoneyCount) Proxy.newProxyInstance(MoneyCountProxy.class.getClassLoader(),new Class<?>[]{MoneyCount.class},new MoneyCountProxy(runMap));
    }
}

/**
 * 策略接口
 */
interface MoneyCount {
    //使用本次购买总额，计算出应付金额(历史购买总额，通过调用不同的策略区分)
    Double count(Double thisMoney);
}

/**
 * A策略 历史0-1000
 */
@HistoryMoneyRange(@MoneyRange(max = 1000, order = 99))
class AMoneyCount implements MoneyCount {
    @Override
    public Double count(Double thisMoney) {

        //乘以折扣
        return thisMoney * 0.9;
    }
}

/**
 * B策略 历史1000-2000
 */
@HistoryMoneyRange(@MoneyRange(min = 1000, max = 2000, order = 99))
class BMoneyCount implements MoneyCount {
    @Override
    public Double count(Double thisMoney) {
        //乘以折扣
        return thisMoney * 0.8;
    }
}

/**
 * C策略 历史>2000
 */
@HistoryMoneyRange(@MoneyRange(min = 2000, order = 99))
class CMoneyCount implements MoneyCount {
    @Override
    public Double count(Double thisMoney) {
        //乘以折扣
        return thisMoney * 0.7;
    }
}

/**
 * D策略 活动2 0-1000
 */
@ThisMoneyRange(@MoneyRange(max = 1000, order = 1))
class DMoneyCount implements MoneyCount {
    @Override
    public Double count(Double thisMoney) {
        return thisMoney - 100;
    }
}

/**
 * E策略 活动2 1000-2000
 */
@ThisMoneyRange(@MoneyRange(min = 1000, max = 2000, order = 1))
class EMoneyCount implements MoneyCount {
    @Override
    public Double count(Double thisMoney) {
        return thisMoney - 300;
    }
}

/**
 * F策略 活动2 >2000
 */
@ThisMoneyRange(@MoneyRange(min = 2000, order = 1))
class FMoneyCount implements MoneyCount {
    @Override
    public Double count(Double thisMoney) {
        return thisMoney - 500;
    }
}

/**
 * G策略 活动3 金额数包含999 -100
 */
//这个注解全部使用默认值，也就是在Integer的范围都需要执行该策略,此处使用ThisMoneyRange注解嵌套也一样可以
@HistoryMoneyRange(@MoneyRange)
class GMoneyCount implements MoneyCount {
    @Override
    public Double count(Double thisMoney) {
        //只要包含999就好了
        return thisMoney.toString().contains("999") ? thisMoney - 100 : thisMoney;
    }
}
