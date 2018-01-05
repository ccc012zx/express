package com.zmlProjects.outOfMemory;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 环境jdk1.7？
 * Runtime Constant Pool in Method Area 内存溢出实验
 * 说明：在运行时产生大量常量就可以实现让 Method Area 溢出的目的。运行是常量可以用 String 类的 intern 方法，不断地产生新的常量。
 * 输出：Caused by: java.lang.OutOfMemoryError: PermGen space
 * 監控方法：命令行jps查看pid，命令行"jstat -gcutil 7002 250 20"
 */
public class RuntimeConstantPoolInMethodArea {
    public static void main(String[] args) {
        List<String> list = new ArrayList<String>();
        int i = 0;
        while (true) {
            list.add(String.valueOf(i++).intern());
        }
    }
}
