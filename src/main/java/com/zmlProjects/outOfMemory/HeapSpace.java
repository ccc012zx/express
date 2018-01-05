package com.zmlProjects.outOfMemory;

import java.util.ArrayList;
import java.util.List;

/**
 * 环境jdk1.7？
 * Heap内存溢出实验
 * 说明：堆是用来存储对象的，当然对象不一定都存在堆里（由于逃逸技术的发展）。那么堆如果溢出了，一定是不能被杀掉的对象太多了。模拟 Heap 内存溢出，只要不断创建对象并保持有引用存在即可。
 * 输出：Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
 */
public class HeapSpace {
    private static class HeapOomObject {
    }

    public static void main(String[] args) {
        List<HeapOomObject> list = new ArrayList<HeapOomObject>();
        while (true) {
            list.add(new HeapOomObject());
        }
    }
}
