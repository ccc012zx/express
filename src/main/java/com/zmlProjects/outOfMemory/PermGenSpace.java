package com.zmlProjects.outOfMemory;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 *
 * 环境jdk1.7？
 * Method Area 内存溢出实验
 * 说明：Non-heap，是用来存储 Object Class Data、常量、静态变量、JIT 编译后的代码等。如果该区域溢出，则说明某种数据创建的实在是太多了。模拟的话，可以不断创建新的 class，直到溢出为止。
 * 以下代码使用到 cglib-2.2.2.jar 和 asm-all-3.0.jar。
 * 输出：Caused by: java.lang.OutOfMemoryError: PermGen space
 */
public class PermGenSpace {
    static class MethodAreaOomObject {
    }
    public static void main(String[] args) {
        while(true){
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(MethodAreaOomObject.class);
            enhancer.setUseCache(false);
            enhancer.setCallback(new MethodInterceptor() {
                public Object intercept(Object obj, Method method, Object[] args,
                                        MethodProxy proxy) throws Throwable {
                    return proxy.invoke(obj, args);
                }
            });
            enhancer.create();
        }
    }
}
