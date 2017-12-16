package com.zx;

public class ClassDemo1 {
	public static void main(String[] args) throws Exception {
		//Foo的实例对象如何表示
		Foo foo1 = new Foo();
		
		//任何一个类都是java.lang.Class的实例对象，
		//这个对象称为Class类的类类型
		//这个实例对象有三种表示方式
		
		//1. -->实际再告诉我们，任何一个类都有隐含的静态成员变量class
		Class c1= Foo.class;
		
		//2. 已知该类对象，通过getClass方法
		Class c2 =foo1.getClass();
		
		//c1.c2表示了Foo类的类类型
		
		//3.
		Class c3 = Class.forName("com.zx.reflect.Foo");
		
		//可以通过类的类类型创建该类的对象--》通过c1.c2.c3创建Foo的实例对象
		Foo foo = (Foo) c1.newInstance();//要有无参构造方法
		foo.print();
		
		//new对象是静态加载类，在编译时刻就加载所有可能用到的类
		Foo foo2= new Foo();
		
		//动态加载类
		Class c4 = Class.forName("com.zx.reflect.Foo");
		Foo foo3 = (Foo) c1.newInstance();
		
	}
}

class Foo{
	
	void print(){
		System.out.println("foo");
	}
}