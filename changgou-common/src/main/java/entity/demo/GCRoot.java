package entity.demo;

import java.lang.ref.SoftReference;

/**
 * 1.什么是垃圾?
 *      内存中已经不再被使用到的空间就是垃圾
 *
 * 2.如何判定是否可以被回收?
 *      1.引用计数法: 给对象添加一个引用计数器,每当有地方引用它,计数器值加1,每有一个地方引用失效,计数器值减1,当减到0则判定为不再使用,可被回收,无法解决循环引用问题
 *      2.枚举根节点做可达性分析(根搜索路径算法):
 *              基本思路: 通过一系列名为"GC Roots" 的对象(Set集合)作为起始点,向下搜索,如果一个对象到GC Roots没有任何引用链相连时,说明对象不可以
 *
 *3.哪些对象可作为GC Root?
 *      1.虚拟机栈(栈帧中的局部变量区)中引用的对象
 *      2.方法区中类静态属性引用的对象
 *      3.方法区中常量引用的对象
 *      4.本地方法栈中引用的对象
 *
 *4. JVM的参数类型:
 *      1.标配参数: 如java -version java -help
 *      2.x参数:  如-xint 解释执行(了解)
 *    * 3.xx参数:主要分为
 *                  (1)Boolean类型: -XX: +或-某个属性,+表示启用,-表示关闭
 *                  (2)KV设值类型: -XX: key=value
 *                          例:-XX:MetaspaceSize=128m  -XX:MaxTenuringThreshold=15
 *5.如何查看一个正在运行的java程序,它的某个jvm参数是否开启?具体值是多少?
 *          1.现在控制台查看java进程编号: jps -l
 *          2.查看相应进程的参数信息: jinfo -flag PrintGCDetails(参数名称,KV类型只需K) 1356(进程号)
 *          3.模糊查询所有参数:jinfo -flag 进程编号
 *                  Non-default VM flags: 表示jvm默认的
 *                  Command line: 表示自定义修改过的
 *          4.坑: -Xmx 等价于-XX:InitialHeapSize  相当于取别名,本质还是-XX参数
 *                -Xms 等价于-XX:MaxHeapSize
 *6.查看jvm参数盘点家底:
 *          java -XX:+PrintFlagsInitial
 *          java -XX:+PrintFlagsFinal  主要查看修改后的参数 :=
 *          直接运行时修改: java -XX:+PrintFlagsFinal -XX:MetaspaceSize=512m
 *          java -XX:+PrintCommandLineFlags -version  主要最后一个参数,查看垃圾回收器
 *
 *
 *7.掌握常用参数:
 *      1.-Xms: 初始大小内存,默认物理内存1/64
 *      2.-Xmx: 最大分配内存,默认物理内存1/4
 *      3.-Xss: 设置单个线程栈的大小,一般默认为512~1024k 等价-XX:ThreadStackSize  如果为0,代表使用出厂默认值 Linux64位默认1024k
 *      4.-Xmn: 设置新生代大小
 *      5.-XX:MetaspaceSize: 设置元空间大小,仅受本地内存限制,不管多大内存,就用20多m
 *
 *8.经典配置: -Xms128m -Xmx4096 -Xss1024k -XX:MetaspaceSize=512m -XX:+PrintCommandLineFlags -XX:+PrintGCDetails -XX:+UseSerialGC
 *
 *9.配置参数:-XX:+PrintGCDetails  打印GC详细日志信息
 *
 *10.eden区内存比例: -XX:SurvivorRatio=8,  代表Eden:S0:S1 = 8:1:1(默认)
 *                  -XX:SurvivorRatio=4,  代表Eden:S0:S1 = 4:1:1
 *
 *11.新生老年比例(一般默认最好): -XX:NewRatio=2,代表新:老 = 1:2(默认)
 *                             -XX:NewRatio=4,代表新:老 = 1:4
 *
 *12.设置垃圾的最大年龄: -XX:MaxTenuringThreshold=15(默认) java8默认最大15
 *
 *13.强.软.弱.虚 引用
 *      1.强引用(Reference): 当内存不足,JVM开始垃圾回收,对于强引用对象,就算OOM也不会对该对象回收,死都不收,除非显式置为null,java内存泄漏主因之一
 *              例:Object obj = new Object(); 这样定义默认为强引用
 *      2.软引用(SoftReference): 相对强引用弱化,内存足够不收,内存不够收
 *
 *      3.弱引用(WeakReference):有GC 一律回收
 *              缓存清空,保证性能: WeakHashMap 主要用于做高速缓存
 *      4.虚引用(PhantomReference):形同虚设,如果一个对象持有虚引用,就跟没引用一样,随时会被GC回收,不能单独使用,
 *              主要用于跟踪对象被垃圾收集的状态,必须和引用队列(ReferenceQueue)联合使用,
 *              且它的get()方法总是返回null,后三种GC后都可以被放到引用队列,但虚引用一定要放到引用队列
 *              有点类似于Spring aop的后置通知,留点遗言什么的
 *
 */
public class GCRoot {
    public static void main(String[] args) {

        //程序级别查看内存
        long totalMemory = Runtime.getRuntime().totalMemory();//返回java虚拟机中内存总量
        long maxMemory = Runtime.getRuntime().maxMemory();//java虚拟机试图使用的最大内存
        System.out.println("TOTAL_MEMORY(-Xms)=" +totalMemory+"(字节,)"+(totalMemory/(double)1024/1024)+"MB");
        System.out.println("MAX_MEMORY(-Xms)=" +maxMemory+"(字节,)"+(maxMemory/(double)1024/1024)+"MB");

        //创建软引用
        //应用场景: 每次读取图片都从硬盘读取,会严重影响性能,一次性加载又会大量占用内存,适合软引用
        //思路:用一个HashMap保存图片路径和软引用的映射关系,内存不足时JVM自动回收这些空间,避免OOM
        Object o1 = new Object();
        SoftReference<Object> softReference = new SoftReference<>(o1);
        //获取软引用对象
        Object o = softReference.get();


        //创建弱引用
        Object o2 = new Object();
        SoftReference<Object> softReference1 = new SoftReference<>(o2);
        //获取软引用对象
        Object obj = softReference.get();
    }
}
