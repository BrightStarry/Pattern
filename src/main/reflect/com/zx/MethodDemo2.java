package com.zx;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MethodDemo2 {
	public static void main(String[] args) throws Exception {
		List list1 = new ArrayList();
		List<String> list2 = new ArrayList<String>();
		
		Class c1 = list1.getClass();
		Class c2 = list2.getClass();
		System.out.println(c1==c2);
		
		/**
		 * c1 == c2 说明编译之后集合的泛型是去泛型化的，
		 * java中集合的泛型是防止错误输入 的，只在编译阶段有效，绕过编译就无效了
		 * 验证：可以通过方法的反射，绕过编译
		 */
		Method m = c2.getMethod("add", Object.class);
		m.invoke(list1,3);//绕过编译也就是绕过了泛型
		System.out.println(list1.size());
		System.out.println(list1);
		
	}
}
