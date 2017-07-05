package com.zx.annotation.b;
/**
 * 下面演示的是用注解来改良策略模式，消除if-elseif-else
 * 具体如下：
 * 历史购买总额  0-1000    9折  A
 * 1000-2000 8折  B
 * >2000     7折  C
 */

import org.reflections.Reflections;

import java.io.File;
import java.io.FileFilter;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 测试类
 */
public class B {
    /**
     * 经过测试。。。 使用Reflections jar比原生的慢了10倍。
     * 本来我还以为是因为里面的set转类型转换导致的，而后测试了下，发现问提在于
     * 新建Reflections类，贼慢。
     * 不过如果是建立好以后可以一直放着，重复使用的话，其实影响不大
     */
    public static void main(String[] args) {
        long i = System.currentTimeMillis();
        Customer customer = new Customer();
        customer.buy(1366.33, 1000.0);
        System.out.println(customer.count());
        System.out.println(System.currentTimeMillis() - i);
    }
}

/**
 * 注解 用来注解历史总额的范围
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@interface HistoryMoneyRange {
    int max() default Integer.MAX_VALUE;

    int min() default Integer.MIN_VALUE;
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
    private final String PACKAGE_NAME = "com.zx.annotation.b";
    //类加载器，使用相同的类加载器
    private final ClassLoader classLoader = getClass().getClassLoader();
    //存储策略实现类的集合
    private Set<Class<? extends MoneyCount>> moneyCounts = new HashSet<>();

    /**
     * 根据金额来生成指定的策略实现类
     */
    public MoneyCount createMoneyCount(Customer customer) {
        //遍历策略实现类集合
        for (Class<? extends MoneyCount> moneyCount : moneyCounts) {
            //获取注解信息
            HistoryMoneyRange historyMoneyRange = moneyCount.getAnnotation(HistoryMoneyRange.class);
            //如果在这个范围内，则使用该策略
            if (customer.getHistoryMoney() >= historyMoneyRange.min() && customer.getHistoryMoney() < historyMoneyRange.max()) {
                try {
                    //创建实例
                    return moneyCount.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new RuntimeException("策略获取失败");
                }
            }
        }
        throw new RuntimeException("策略获取失败");
    }

    /**
     * 使用Reflections jar简化初始化代码
     * 此处有个疏忽，就是本来获取的应该是所有实现MoneyCount接口的类，我写成了所有被@HistoryMoneyRange注解的类
     * 如果直接写获取接口实现类，返回的直接就是Class<? extends T>类型了
     * 已经在c包中修正
     */
    private void initByReflections() {
        //新建扫描PACKAGE_NAME包的Reflections类
        Reflections reflections = new Reflections(PACKAGE_NAME);
        //获取该包下所有被HistoryMoneyRange注解的类的类对象的set集合
        //且，将该set集合的泛型转换
//        Set<Class<? extends MoneyCount>> typesAnnotatedWith = (Set<Class<? extends MoneyCount>>) reflections.getTypesAnnotatedWith(HistoryMoneyRange.class);这样编译不能通过
        Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(HistoryMoneyRange.class);
        for (Class<?> temp: typesAnnotatedWith) {
            moneyCounts.add((Class<? extends MoneyCount>)temp);
        }
    }

    /**
     * 初始化
     */
    private void init() {
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
//        init();
        initByReflections();
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
 * 策略接口
 */
interface MoneyCount {
    //使用本次购买总额，计算出应付金额(历史购买总额，通过调用不同的策略区分)
    Double count(Double thisMoney);
}

/**
 * A策略 历史0-1000
 */
@HistoryMoneyRange(min = 0, max = 1000)
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
@HistoryMoneyRange(min = 1000, max = 2000)
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
@HistoryMoneyRange(min = 2000)
class CMoneyCount implements MoneyCount {
    @Override
    public Double count(Double thisMoney) {
        //乘以折扣
        return thisMoney * 0.7;
    }
}
