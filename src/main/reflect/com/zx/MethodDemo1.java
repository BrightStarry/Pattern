package com.zx;

import java.lang.reflect.Method;

public class MethodDemo1 {
	public static void main(String[] args) throws Exception {
		//获取print(int,int)方法
		//1.获取类的类类型
		AAAA a1 = new AAAA();
		Class c = a1.getClass();
		/**
		 * 2.获取方法 由名称和参数列表来决定
		 *	getMethod()获取的事public 方法 
		 *getDeclaredMethod()获取所有的这个类自己定义的方法
		 */
		
		//c.getMethod("print",new Class[]{int.class,int.class});
		//或者
		Method m = c.getMethod("print", int.class,int.class);
		
		//方法的反射操作,指用m对象来进行方法调用
		//a1.print(10, 20);
		//方法如果没有返回值，返回null
		//Object o = m.invoke(a1, new Object[]{10,20});
		//或者
		Object o = m.invoke(a1,10,20);
	}
	
}
class AAAA{
	public void  print(int a,int b){
		System.out.println(a + b);
	}
	
	public void print(String a,String b){
		//转换成大写/小写
		System.out.println(a.toUpperCase() + "," +b.toLowerCase());
	}
}