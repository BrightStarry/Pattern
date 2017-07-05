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

#### 注解-Annotation :额外增加的，下面有些模式要用到
* 元注解：用来注解 注解 的注解；
    1. @Target：表示注解的目标是类、方法还是哪里
        * ElementType.xx或{ElementType.xx,ElementType.xxx}
        * CONSTRUCTOR：构造器；FIELD：属性；LOCAL_VARIABLE:局部变量；METHOD：方法；
        * PACKAGE:包；PARAMETER:参数；TYPE:类、接口、枚举等
        * TYPE_PARAMETER:类型注解,<T>、<? extends A>等；TYPE_USE：可以任何用到类型的位置使用，例如用new关键字创建对象、类型转换、使用implements实现接口、throws等
    2. @Retention:表示该注解类的作用域/生命周期/保留周期，默认为CLASS
        * RetentionPolicy.xx
        * SOURCE:源文件；CLASS：class/字节码文件；RUNTIME:运行时
        * 一般只写RUNTIME，只有该注解才能动态获取参数
    3. @Documented：表示该注解会被javadoc文档化
    4. @Inherited：表示该注解是否有继承性，也就是父类被注解，子类继承父类后，是否也相当于被注解
    5. @Repeatable：表示是否可以重复使用该注解在同一语句
* 注解中于允许的参数：
        * 所有基本类型(byte,int,short,long,float,double,char,boolean)
        * String
        * Class
        * enum
        * Annotation
        * 以上所有类型的数组类型
* 当注解中的属性的名字为value时，注解时直接写xxx(a),a就会被赋值给属性名为value的属性
* Class<T>： 如果使用了@Repeatable注解，使A注解可以重复注解一个类(此时应该配置As类),且在B类上使用了多个A注解，但此时想要获取A注解获取不到的，只能获取到As注解（该注解包含A[]属性）
    getAnnotations()/Annotation[]:获取该类所有的注解；
    getAnnotation(Class<A>)/A:获取该类指定类型的注解；
    getDeclaredField()：该方法才能获取到private字段，Declared意思应该不是公开的，而是所有存在的的意思
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
    JDK实现的动态代理实际上就是那个接口的一个空壳，所有调用的类实际上还是通过method.invoke(hero, args)方法中的第一个参数实现的，这个参数传的就是该接口的实现类实例。（具体可参考策略模式中的代码理解）
    与静态代理的区别在于，编译时代理类并没有生成。在运行是，代理类动态生成。编译时只有被代理类和一个委托类（用来生成代理类）。
    而且动态代理如果需要对代理类的所有方法进行相同操作的代理，也无需把所有方法重写一遍。
    从重构的角度来说，其实更简单点，那就是在你发现你使用静态代理的时候，需要写一大堆重复代码的时候，就请改用动态代理试试吧
    动态代理的被代理类必须实现一个接口，或者本身就是一个接口；
    
    在proxy/com.zx.dynamic.sourcecode包中，我写了对动态代理源码的简析；
    此外，在测试中，将动态生成的代理类字节码文件保存到本地；
    该代理类除了实现自身接口，还继承了Proxy类；然后有所有被代理类的方法(Method类型)的静态属性；
    每次执行任何方法时，都会先调用Proxy类中的h(动态代理类(实现InvocationHandler接口的类))的invoke方法，并将必要的实参传入

#### 工厂模式-factory:
创建不同的类-如果需要修改产品类的构造方法，只需要修改工厂类中的一处就可以了；
此外，假设产品是某个功能需要的东西，如果通过工厂模式创建，隐藏内部细节，那么日后想要更换具体产品也很容易，只需自己在内部更换就可以了，否则如果是普通的new的方式，已经多出耦合了
* 简单工厂模式-一个工厂类创造所有的产品
    * 扩展时需要修改工厂类代码
    
![](img/1.png)
---
* 工厂模式-每个产品都有单独的用来创建它的工厂类，这些工厂类都继承自一个工厂超类
    * 扩展时只需增加一个工厂子类和一个产品子类，无需修改原代码
    
![](img/2.png)
---
* 抽象工厂模式-和工厂模式类似，不过每个具体工厂需要生产一整个产品系列(产品分为多个系列，每个系列都有单独的Factory;其中，如果有A、B两个抽象产品，则A1、B1两个具体产品为一个系列)
    
![](img/3.png)

1. 首先从简单工厂进化到工厂方法，是因为工厂方法弥补了简单工厂对修改开放的弊端，即简单工厂违背了开闭原则。
2. 从工厂方法进化到抽象工厂，是因为抽象工厂弥补了工厂方法只能创造一个系列的产品的弊端。

---
#### 观察者模式/发布订阅模式-Observer:一个类管理所有依赖于它的类，并在自身发生变化时主动给依赖它的类发出通知

---
#### 策略模式-strategy：在一个需要多种算法不同实现的地方，可以方便的替换
* 策略模式-com.zx.common
    * 抽象策略接口+A策略+B策略
    * 策略调用类，有抽象策略接口作为属性，直接调用该接口的策略方法。构造时需要传入该接口实现类，且可以使用set方法修改策略的具体实现
    
![](img/4.png)

---
* 普通策略模式实现根据历史消费总额和本次消费金额，进行优惠减免-com.zx.annotation.a
* 使用注解+策略模式+单例模式+工厂模式实现，消除if-elseif-else代码，可以对单一活动进行计算，并对单一活动扩展-com.zx.annotation.b
* 使用嵌套注解+策略模式+动态代理+单例模式+工厂模式+Reflections(反射框架-新建Reflections类的时候较慢)，可以扩展多个活动
    * 简述下这最后一个的思路
        ~~~
            1. 实现多个活动所需的各种算法；这些算法类都实现同一接口
            2. 定义出表示历史总额还是本次消费金额的注解，这两个注解都包含了一个嵌套注解：包含了金额的范围
            3. 一个创建策略实现类的简单工厂；在该工厂中，扫描到了指定包(策略实现类所在包)的所有class文件的File，并通过这些file获取了所有策略实现类的类对象；也可以直接使用Reflections框架，直接两行代码简化了原生代码
            4. 遍历这些类对象，获取每个类对象上的注解，并判断这个消费者(历史总额，本次金额)是否符合该注解中的范围，如果符合，则将这个类对象添加到一个有序集合中
            5. 然后将这些有序集合都传给策略接口的一个动态代理类，以获取动态代理实例。
            6. 在该动态代理实例中，调用invoke()方法时，只要是算法方法，就根据有序集合的顺序，一次执行每个策略实现类的算法，得到最终的优惠金额，返回()
        ~~~
---
#### 适配器模式-


