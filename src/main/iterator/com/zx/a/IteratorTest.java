package com.zx.a;


/**
 * 迭代器模式测试类
 */
public class IteratorTest {
    public static void main(String[] args) {
        //使用迭代器进行遍历
        ArrayList<String> customList = new ArrayList<>();
        customList.add("a");
        customList.add("b");
        customList.add("c");
        Iterator<String> iterator = customList.iterator();
        while (iterator.hasNext()) {
            String next = iterator.next();
            System.out.println(next);
        }
    }
}

/**
 * 迭代器接口
 */
interface Iterator<E> {
    boolean hasNext();
    E next();
    void remove();
}

/**
 * 迭代器可用接口 所有要使用迭代器模式的集合类必须实现该接口
 */
interface Iterable<T> {
    //创建迭代器并返回
    Iterator<T> iterator();
}

/**
 * 集合类
 */
class ArrayList<E> implements Iterable<E>{
    //每次扩容递增的大小
    private static final int INCREMENT = 10;
    //存储元素的数组
    private E[] array = (E[])new Object[10];
    //数组大小
    private int size;
    //增加方法
    public void add(E e) {
        if (size < array.length) {
            array[size++] = e;
        }
    }
    //返回长度
    public int size(){
        return size;
    }


    //返回迭代器类
    @Override
    public Iterator<E> iterator() {
        return new Itr();
    }

    //迭代器-内部类
    private class Itr implements Iterator<E> {
        //游标
        int cursor = 0;
        //判断是否有下一个元素
        @Override
        public boolean hasNext() {
            return cursor != size();
        }
        //返回下一个元素
        @Override
        public E next() {
            return array[cursor++];
        }
        //删除该元素
        @Override
        public void remove() {
            //doSomething
        }
    }

}


