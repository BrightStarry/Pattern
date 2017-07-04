package com.zx;

import org.reflections.ReflectionUtils;
import org.reflections.Reflections;

import java.lang.annotation.*;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.util.Set;

/**
 * 测试
 */
class Test{
    public static void main(String[] args) throws NoSuchFieldException {
//        a();
        b();
    }

    /**
     * 使用Class<T>类获取注解
     */
    public static void a() throws NoSuchFieldException {
        //首先获取类对象
        Class<BaoNv> baoNvClass = BaoNv.class;
        //获取Intro注解
        Intro annotation = baoNvClass.getAnnotation(Intro.class);
        //获取Intros注解，当注解多个Intro注解时，获取到的就是Intros
        Intros annotation2 = baoNvClass.getAnnotation(Intros.class);
        //获取A注解
        A annotation1 = baoNvClass.getAnnotation(A.class);
        //获取所有注解
        Annotation[] annotations = baoNvClass.getAnnotations();

        /**
         * 获取属性中的注解
         */
        //该方法才能获取private属性
        Field hp = baoNvClass.getDeclaredField("HP");
        Annotation[] annotations1 = hp.getAnnotations();
    }

    /**
     * 使用 Reflections jar获取注解
     */
    public static void b() {
        //需要反射的包名
        Reflections reflections = new Reflections("com.zx");
        //获取有该注解的类
        Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(Intros.class);
        //获取类中，被该注解注释的属性，也可以不传第二个条件参数，也可以传多个条件参数
        Set<Field> allFields = ReflectionUtils.getAllFields(BaoNv.class, ReflectionUtils.withAnnotation(IsInfinite.class));
    }
}





/**
 * 英雄简介 注解，用来标注一个英雄的基本状态
 */
//注解目标
@Target(ElementType.TYPE)
//作用域
@Retention(RetentionPolicy.RUNTIME)
//会被javadoc文档化
@Documented
//可以继承
@Inherited
//可以重复
@Repeatable(Intros.class)
public @interface Intro {
    //名字 String型 默认值为""
    String name() default "";
    //血量 int型 默认值为-1
    int HP() default -1;
    //移速 int型 默认值为-1
    int speed() default -1;
}
/**
 * 用来让注解可以重复，搭配@Repeatable使用
 */
//注解目标
@Target(ElementType.TYPE)
//作用域
@Retention(RetentionPolicy.RUNTIME)
//会被javadoc文档化
@Documented
//可以继承
@Inherited
@interface Intros{
    Intro[] value();
}

/**
 * 属性级别的注解，标注血量是否为无限
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@interface IsInfinite{
    //是或否
    boolean value() default false;
    //测试 属性名为value时，是否能直接写@IsInfinite(a),将a赋值给value
    int a() default -1;
}

/**
 * 测试注解，用来测试是否能只获取一个类对象上某个类型的注解
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@interface A{
    int value() default -1;
}

/**
 * 豹女
 */
@A(100)
@Intro(name = "人形态",HP = 500,speed = 300)
@Intro(name = "豹形态",HP = 600,speed = 400)
class BaoNv{
    @IsInfinite(false)
    private Integer HP;
    
    private Integer speed;
}
