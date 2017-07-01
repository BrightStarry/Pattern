##### 动态代理源码理解

1. Proxy.newProxyInstance(getClass().getClassLoader(),hero.getClass().getInterfaces(),this);

>    //参数分别为 类加载器、被代理类实现的接口类类型、动态代理类自身

>    @CallerSensitive
>    public static Object newProxyInstance(ClassLoader loader,
>                                          Class<?>[] interfaces,
>                                          InvocationHandler h)throws IllegalArgumentException
>    {   
    
>        //该方法判断h是否为空，如果为空，抛出NullPointException，否则，直接返回；
>        Objects.requireNonNull(h);
        
>        //将 被代理类实现的接口数组 复制，
>        //此处调用Object的clone()，本来该方法需要对象实现Cloneable接口才行，但对于数组对象是没事的；该拷贝是浅拷贝
>        //此处的浅拷贝是指，intfs和interface的内存地址不同，但其中的元素还是原来的内存地址，也就是同一个元素
>        final Class<?>[] intfs = interfaces.clone();
        
>        //获取安全管理器类，该类用来检查是否拥有进行某些操作的权限
>        final SecurityManager sm = System.getSecurityManager();

>        //如果 该安全管理器不为空，执行 2：checkProxyAccess()方法，该方法是检查创建代理类的权限
>        //其中，Reflection.getCallerClass()方法可以返回调用该方法的类的类对象，或者更上层的类对象，也就是说，可以查出整个调用链
>        if (sm != null) {
>            checkProxyAccess(Reflection.getCallerClass(), loader, intfs);}

>        //该方法必须在上面checkProxyAccess()方法执行后执行，用来创建代理类(获取代理类的类类型)
>        //getProxyClass0()该方法之所以说查找或创建，是因为如果缓存中有的话，会直接返回
>        /*
>         * Look up or generate the designated proxy class(查找或生成指定的代理类).
>         */
>        Class<?> cl = getProxyClass0(loader, intfs);

>        //Reflection.getCallerClass()获取的应该是上一级调用该方法的类的类对象，也就是实现invocation handler的类的类对象；c1则是代理类的类对象
>        /*
>         * Invoke its constructor with the designated invocation handler(使用指定的动态代理类（实现invocation handler的类）调用它的构造方法).
>         */
>        try {
>            if (sm != null) {
>                checkNewProxyPermission(Reflection.getCallerClass(), cl);
>            }

>         //获取代理类类对象的构造方法，传入的参数constructorParams是Class<?>[]，里面只有一个Class<InvocationHandler>元素      
>         final Constructor<?> cons = cl.getConstructor(constructorParams);

>         //将该方法传入的第三个参数(动态代理类this)赋值给ih
>         final InvocationHandler ih = h;

>         //Modifier类提供了static方法和常量来解码类和成员访问修饰符。
>         //如果生成的代理类对象没有被public修饰的参数，则进入if
>         //此处应该是给其构造方法特殊的访问权限，以执行下面的newInstance方法
>         if (!Modifier.isPublic(cl.getModifiers())) {
>                AccessController.doPrivileged(new PrivilegedAction<Void>() {
>                    public Void run() {
>                        cons.setAccessible(true);
>                        return null;
>                    }
>                });
>            }

>            //此处可以看出，c1的Class不再是被代理类的Class了，需要传入h(动态代理类自身)作为参数了
>            return cons.newInstance(new Object[]{h});
        } catch (IllegalAccessException|InstantiationException e) {
            throw new InternalError(e.toString(), e);
        } catch (InvocationTargetException e) {
            Throwable t = e.getCause();
            if (t instanceof RuntimeException) {
                throw (RuntimeException) t;
            } else {
                throw new InternalError(t.toString(), t);
            }
        } catch (NoSuchMethodException e) {
            throw new InternalError(e.toString(), e);
        }
    }
    
---
