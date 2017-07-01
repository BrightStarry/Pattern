###设计模式斩魂之路
之前对设计模式的学习，写的东西感觉不太好，重新写下；

####设计模式六大规则
* 单一职责：每个类只负责单一的功能；
~~~
    十分重要的规则，可以将各个功能模块解耦，方便复用，扩展等；
~~~

* 里氏替换原则：子类可以替换父类，并且可以正常工作;
~~~
    该规则表明，子类一般不应该重写父类的方法，而是扩展父类的功能；
~~~

* 接口隔离原则:接口拥有的行为应该尽可能的小;
~~~
    否则，可能会出现，一个接口实现类，有一些方法是空的，并没有实现;
~~~

* 依赖倒置原则：高层模块不应该依赖低层模块，二者都应该依赖与抽象，抽象不应依赖细节（实现），细节（实现）应该依赖抽象;
~~~
    依赖抽象可以实现解耦，其抽象的实现并不会影响各个模块间的调用；
~~~

* 最小知道原则：一个类不需要知道其他类的细节;
~~~
    可以解耦，且每个类自身高内聚；
    高内聚指将一个类的细节全部写在该类内部，不透露给其他类
~~~

* 开闭原则：对修改关，对扩展开；
~~~
    尽量不修改原有代码；但要方便扩展功能
~~~

#### 注解学习：额外增加的，下面有些模式要用到
* 元注解：用来注解 注解 的注解；
    1. @Target：表示注解的目标是类、方法还是哪里
        * ElementType.xx
        * CONSTRUCTOR：构造器；FIELD：作用域；LOCAL_VARIABLE:局部变量；METHOD：方法；
        * PACKAGE:包；PARAMETER:参数；TYPE:类、接口、枚举等
        * TYPE_PARAMETER:类型注解,<T>、<? extends A>等；TYPE_USE：可以任何用到类型的位置使用，例如用new关键字创建对象、类型转换、使用implements实现接口、throws等
    2. @Retention:表示该注解类的作用域/生命周期/保留周期
        * RetentionPolicy.xx
        * SOURCE:源文件；CLASS：class/字节码文件；RUNTIME:运行时
    3. @Documented：表示该注解会被javadoc文档化
    4. @Inherited：表示该注解是否有继承性，也就是父类被注解，子类继承父类后，是否也相当于被注解
    5. @Repeatable：表示是否可以重复使用该注解在同一语句

####单例模式-Singleton
使用单例模式的类的显著特点：
    一个类没有自己的状态，就是一个类创建一个和创建多个是一样的。
单例与静态类的区别：
    1.可以多态（继承类、实现接口、被继承后方法可重写等）(B类如果继承A类，B.a()可以调用A类的静态方法a，但是该调用等价A.a()，假使在a()中输出一个属性的值，输出的会是A类的该属性值);
    2.可以在特定时间加载（这是错的，静态类也可以延迟加载，是在静态方法第一次被调用的时候加载的）;
1. 懒汉模式-lazy(实例会在第一次使用的时候加载)
~~~
    public class LazyClass {
        //私有化构造方法
        private LazyClass(){}
        //声明静态对象，不创建对应实例
        private static LazyClass lazyClass;
        //获取单例实例的方法
        public static LazyClass getInstance(){
            if(lazyClass == null)
                lazyClass = new LazyClass();
            return lazyClass;
        }
    }
~~~
2. 饱汉模式-hungry(实例直接创建)
~~~
    public class HungryClass {
        //私有化构造方法
        private HungryClass(){}
        //直接创建对应的实例
        private static HungryClass hungryClass = new HungryClass();
        //获取实例的方法
        public static HungryClass getInstance() {
            return hungryClass;
        }
    }
~~~
3. 双重检查锁模式-doubleCheckedLocking(改造饿汉模式，使其线程安全)（此处没有使用synchronized,而是使用了重入锁，该锁比较灵活，详见多线程项目）
~~~
    public class DoubleCheckedLocking {
        //私有化构造方法
        private DoubleCheckedLocking(){}
        //声明静态实例
        private static DoubleCheckedLocking doubleCheckedLocking;
        //重入锁
        private static ReentrantLock lock = new ReentrantLock();
        //获取实例
        public static DoubleCheckedLocking getInstance(){
            if(doubleCheckedLocking != null){
                lock.lock();
                if(doubleCheckedLocking != null){
                    doubleCheckedLocking = new DoubleCheckedLocking();
                }
                lock.unlock();
            }
            return doubleCheckedLocking;
        }
    }
    
    暂不确定是否要给静态实例添加volatile关键字，私以为，如果该类占用的过小，是有可能被分配到JVM的TLAB（也就是线程私有的缓冲区，详见JVM项目）的，也就有可能发生不可见性；
    ！！！看到一个关键的地方，volatile关键字可以禁止指令重排序；synchronized也可以禁止指令重排序
    
    此外可能产生bug:
        因为虚拟机在执行创建实例的这一步操作的时候，其实是分了好几步去进行的，也就是说创建一个新的对象并非是原子性操作。在有些JVM中上述做法是没有问题的，但是有些情况下是会造成莫名的错误。
        首先要明白在JVM创建新的对象时，主要要经过三步。
            1.分配内存
            2.初始化构造器
            3.将对象指向分配的内存的地址
        这种顺序在上述双重加锁的方式是没有问题的，因为这种情况下JVM是完成了整个对象的构造才将内存的地址交给了对象。
    但是如果2和3步骤是相反的（【指令重排序，happen—before原则（多线程项目中有相关解释）】），就会出现问题了。
        因为这时将会先将内存地址赋给对象，针对上述的双重加锁，就是说先将分配好的内存地址指给synchronizedSingleton，然后再进行初始化构造器，这时候后面的线程去请求getInstance方法时，
    会认为synchronizedSingleton对象已经实例化了，直接返回一个引用。如果在初始化构造器之前，这个线程使用了synchronizedSingleton，就会产生莫名的错误。
    
        上述这个bug是http://blog.csdn.net/zuoxiaolong8810/article/details/9005611这个博客中提出的；但我认为可能不太会，因为，加了锁的代码块应该是原子性的，那么即使指令重排序，
    在进行完原子操作后，也不会发生上述情况；即使不是这样，加上volatile关键词后，防止指令重排序后，应该也不会产生bug。(synchronized可以禁止指令重排序)
    (...原来博客主已经在文章末尾说了可以加volatile，不过他没有写synchronized可以禁止指令重排序)
~~~
4. 静态内部类模式-staticClass(据说最佳；使用时时初始化静态内部类，初始化静态内部类时加载对象，同时线程安全；相比双重检查锁模式，少了每次判断是否为空的步骤)
~~~
    public class StaticClassClass {
        //私有化构造方法
        private StaticClassClass(){}
        //静态内部类
        private static class StaticClassClassInternal{
            private static StaticClassClass instance = new StaticClassClass();
        }
        //获取实例
        public static StaticClassClass getInstance(){
            return StaticClassClassInternal.instance;
        }
    }
~~~
5. 枚举模式-同上的优点，但是不能延迟加载；从效率上来说，该模式最快
~~~
public class A {
    //私有化构造方法
    private A(){}
    //内部静态枚举
    private static enum Singleton{
        INSTANCE;
        private A instance;
        Singleton(){
            instance = new A();
        }
        private A getInstance(){
            return instance;
        }
    }
    //获取实例方法
    public A getInstance(){
        return Singleton.INSTANCE.getInstance();
    }
}
~~~

####代理模式-proxy：在原有类的行为上增加一些行为
代理模式需要的类：
    * 被代理接口，定义了一些方法;
    * 被代理接口实现类，即被代理类，实现了接口的方法
静态代理需要的类:
    * 静态代理类：有代理接口属性，通过该属性调用原方法，并在此基础上扩展(类似装饰者类)；
动态代理需要的类：
    * 实现jdk的InvocationHandler接口的代理类，通过该代理类返回实现被代理接口(Hero)的普通实现类(HeroImpl)的代理实现类(也是Hero,但是是运行时动态生成的会经过invoke()方法的代理实现类)

1. 静态代理:static效率高，耗空间
   1. 代理类一般要持有一个被代理的对象的引用。
   2. 对于我们不关心的方法，全部委托给被代理的对象处理。
   3. 自己处理我们关心的方法。
2. 动态代理：dynamic
    与静态代理的区别在于，编译时代理类并没有生成。在运行是，代理类动态生成。编译时只有被代理类和一个委托类（用来生成代理类）。
    而且动态代理如果需要对代理类的所有方法进行相同操作的代理，也无需把所有方法重写一遍。
    从重构的角度来说，其实更简单点，那就是在你发现你使用静态代理的时候，需要写一大堆重复代码的时候，就请改用动态代理试试吧
    动态代理的被代理类必须实现一个接口，或者本身就是一个接口；
    
    在proxy/com.zx.dynamic.sourcecode包中，我写了对动态代理源码的简析；
    此外，在测试中，将动态生成的代理类字节码文件保存到本地；
    该代理类除了实现自身接口，还继承了Proxy类；然后有所有被代理类的方法(Method类型)的静态属性；
    每次执行任何方法时，都会先调用Proxy类中的h(动态代理类(实现InvocationHandler接口的类))的invoke方法，并将必要的实参传入

#### 工厂模式-factory:创建不同的类
* 简单工厂模式
    * 产品接口
    * 具体产品：实现产品接口
    * 工厂类
    



