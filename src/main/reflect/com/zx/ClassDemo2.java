package com.zx;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;


public class ClassDemo2 {
	public static void main(String[] args) throws Exception {
		Class c1 = int.class;//int的类类型
		Class c2 = String.class;//String类的类类型
		Class c3 = double.class;
		Class c4 = Double.class;
		Class c5 = void.class;
		
		//类名
		System.out.println(c2.getName());
		//简单的类名--》不带包名的类名
		System.out.println(c2.getSimpleName());
		
		//创建A的实例
		AAA a = new AAA();
		printClassMessage(a);
	}
	
	/**
	 * 获取类的全部信息
	 */
	public static void printClassMessage(Object obj){
		//要获取类的信息，首先要获取类的类类型
		Class c = obj.getClass();//传递的是哪个类的对象，c就是该类的类类型
		
		//获取类的名称
		System.out.println("类名：" + c.getName());
		//不带包名的类名
		System.out.println("简单类名:" + c.getSimpleName());
		/**
		 * Method类，方法对象
		 * 一个方法就是一个Method对象
		 * getMethods()获取所有public的方法，包括从父类继承的
		 * getDeclaredMethods()获取的是该类自己声明的方法，不问访问权限
		 * getMethod(name, parameterTypes)根据方法名和方法参数获取单个方法
		 */
		Method[] ms =c.getMethods();
		for(int i=0; i< ms.length;i++){
			//得到方法的返回值类型的类类型
			Class returnType= ms[i].getReturnType();
			//获取返回值类型的名字
			System.out.println(returnType.getName());
			//得到方法名
			System.out.println(ms[i].getName());
			//获取参数类型--》得到的是参数列表类型的类类型
			Class[] paramTypes = ms[i].getParameterTypes();
			for (Class class1 : paramTypes) {
				//获取所有参数的名字
				System.out.println(class1.getName());
			}
		}
		
		
		/**
		 * java.lang.reflect.Filed
		 * 这个类封装了关于成员变量的操作
		 * getField()方法获取的是所有的public的成员变量的信息
		 * getDeclaredFields获取所有该类自己声明的成员变量的信息
		 */
		Field[] fs = c.getDeclaredFields();
		for (Field field : fs) {
			//得到成员变量的类型的类类型
			Class fieldType = field.getType();
			//得到成员变量的类型的类类型的名字
			System.out.println(fieldType.getName());
			//得到成员变量的名字
			System.out.println(field.getName());
		}
		
		/**
		 * 对象的构造函数的信息
		 * 构造函数也是对象
		 * java.lang.Constructor封装了构造函数的信息
		 * getConstructors()获取所有的public的构造方法
		 */
		//Constructor[] cs = c.getConstructors();
		//得到所有的构造方法
		Constructor[] cs = c.getDeclaredConstructors();
		for (Constructor constructor : cs) {
			//获取构造函数的参数列表--》得到的是参数列表的类类型
			Class[] paramTypes = constructor.getParameterTypes();
			for (Class class1 : paramTypes) {
				System.out.println(class1.getName());
			}
		}
	}
	
	
}
