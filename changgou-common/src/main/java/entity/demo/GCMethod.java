package entity.demo;

/**
 * 常见的垃圾收集器:
 *      1.Serial(串行垃圾回收器):为单线程环境设计,且只使用一个线程进行垃圾回收,会暂停所有用户线程,不适合服务器环境
 *                              最古老,最稳定,效率最高的收集器
 *
 *      2.Parallel(并行垃圾回收器): 多个垃圾收集线程并行工作,此时用户线程也是暂停的,适合科学计算/大数据首台处理等弱交互场景
 *
 *      3.CMS(并发垃圾回收器): 用户线程和垃圾线程同时执行,不需要停顿用户线程,适合互联网公司
 *
 *      4.G1
 *
 * 怎么查看系统默认垃圾收集器?
 *          java -XX:+PrintCommandLineFlags -version
 *
 * java的GC回收的类型主要有几种?
 *
 *      新生代(Young gen):
 *          UseSerialGC(复制):  对应的JVM参数: -XX:+UseSerialGC  开启后使用组合: Serial(young区)+SerialOld(Old区)的收集器组合
 *          UseParNewGC(复制):  对应的JVM参数: -XX:+UseParNewGC 开启后组合(已不推荐使用): ParNew(Young区)+SerialOld(Old区)的收集器组合
 *          UseParallelGC(复制 java默认组合,吞吐量优先收集器):  对应的JVM参数: -XX:+UseParallelGC 或-XX:+UseParallelOldGC(可互相激活)
 *
 *      老年代(Old gen):
 *          UseConcMarkSweepGC(CMS并发标记清除收集器): 对应的JVM参数: -XX:+UseConcMarkSweepGC 开启后会自动将新生代: -XX:+UseParNewGC打开
 *                                                    会有一个GC Roots跟踪过程和修正确认过程,稍有停顿.新生代使用UseParNewGC CMS出问题备胎UseSerialOldGC(java8没了)上
 *                                                    分为四步:初始标记->并发标记(用户线程一起)->重新标记->并发清除(用户线程一起)
 *                                                    优点: 并发收集停顿低
 *                                                    缺点: 并发执行,对CPU消耗大,会产生内存碎片
 *          UseParallelOldGC(标整)
 *
 *          UseSerialOldGC(标整 没了)
 *
 *      两个代都行:
 *          UseG1GC(整体标整,局部复制): 对应的JVM参数: -XX:+UseG1GC java7产生,java9默认,仍然属于分代收集器,可横跨新生老年代,面向服务器端,更短的停顿,更高的吞吐量,也是与应用程序并发执行
 *                                              不会产生很多的内存碎片,增加了停顿时间预测机制,用户可指定停顿时间
 *                                              G1在宏观上把新生代老年代混在一起,将内存划分为一个个子区域(Region),这些子区域可在新生老年代切换
 *                                              通过参数-XX:G1HeapRegionSize=n 指定分区大小(1MB~32MB,必须是2的幂) 默认将堆划分为2048个分区
 *                                              指定停顿时间: -XX:MaxGCPauseMillis=n(毫秒)  jvm将会尽量满足小于这个时间
 *                                              所以最大支持内存为: 32*2048=64GB
 *          G1也是分为四步: 1.初始标记(停顿): 只标记GC Roots能直接关联到的对象
 *                         2.并发标记: 进行GC Roots Tracing的过程
 *                         3.最终标记:修正标记期间,因程序运行导致标记发生变化的一部分对象
 *                         4.筛选回收: 根据时间来进行价值最大化的回收
 *           经典配置: -XX:+UseG1GC -Xmx32g -XX:MaxGCPauseMillis=100  其它用默认就差不多了
 *
 *           G1与CMS相比较: 1.不会产生内存碎片
 *                         2.可以指定精确的停顿时间,或者说更小的停顿
 *Undertow取代tomcat:
 *      实际生产部署微服务及参数配置: java -server -Xms1024m -Xmx1024m -XX:+UseG1GC -jar springboot2019-1.0-SNAPSHOT.jar
 *      然后可以通过java命令查看: jps -l   jinfo -flags java进程号查看
 *
 * 操作系统: 32位的Windows操作系统,不论硬件如何都是使用Client的JVM模式
 *          64位的系统只使用Server模式   开发一定用64位
 *
 */
public class GCMethod {
}
