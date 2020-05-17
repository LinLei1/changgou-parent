package entity.demo;

import java.util.concurrent.*;

/**
 * 第四种获取java多线程的方式: 线程池  池化技术关闭比开启更重要!!
 *
 * 为什么使用线程池?
 *      1.降低系统资源消耗,通过反复利用已创建的线程,降低线程创建和销毁造成的消耗
 *      2.提高响应速度,当任务到达时,不需要等到线程创建就能立即执行
 *      3.提高线程的可管理性,不会无限创建,使用线程池可以对线程进行统一的分配,调优和监控
 *
 * 线程池底层类: ThreadPoolExecutor
 *
 * java线程池主要有3种(现在有5种左右)
 *      1.Executors.newFixedThreadPool(int)  //带固定线程数  适合执行长期任务,性能好很多
 *      2.Executors.newSingleThreadExecutor()  //一池一线程  适合一个任务一个任务执行的场景
 *      3.Executors.newCachedThreadPool()  //一池多线程带缓冲可扩容  适合短期异步小程序,或负载较轻的服务器
 *
 * 线程池7大参数:
 *      1.corePoolSize: 线程池中常驻核心线程数,当池中线程数达到corePoolSize,就会把到达的任务放到缓存队列中;
 *      2.maximumPoolSize: 池中能够容纳同时执行的最大线程数(最大物理上限),此值必须大于等于1
 *      3.keepAliveTime: 多余空闲线程的存活时间,把闲着的加班的赶走,剩下当值的corePoolSize
 *      4.TimeUnit: keepAliveTime时间单位(只对加班线程有效)
 *      5.workQueue: 阻塞任务队列,被提交但尚未被执行的任务,银行候客区
 *      6.threadFactory: 生成线程池中线程的工厂,一般使用默认即可
 *      7.拒绝策略,表示阻塞队列满了,并且工作线程大于等于线程池中最大线程数(maximumPoolSize)
 *
 * 流程: 主线程execute()->CorePool->阻塞队列->扩充到maximumPoolSize->拒绝策略
 *
 * 四种拒绝策略:
 *      1.AbortPolicy(默认): 直接抛出异常,阻止系统正常运行
 *      2.CallerRunsPolicy: 既不抛异常,也不抛弃任务,处理不了回退调用者去处理(main线程)
 *      3.DiscardOldestPolicy: 抛弃等待最久的任务
 *      4.DiscardPolicy: 直接抛弃任务,不予处理也不抛异常
 *
 * 线程池不允许使用Executors去创建,而是通过ThreadPoolExecutor的方式,避免资源耗尽的风险:
 *      Executors返回的线程池弊端如下:
 *              1.FixedThreadPool和singleThreadPool: 允许队列长度为Integer.MAX_VALUE,可能堆积大量请求,从而OOM
 *              2.CachedThreadPool和ScheduledThreadPool: 允许创建的线程数量为Integer.MAX_VALUE,可能创建大量线程,从而OOM
 *线程池参数配置:
 *      不是瞎配, cpu密集型: 任务需要大量运算,而没有阻塞,cpu一直全速运行,那么线程数尽量少,一般是:cpu核数+1
 *                io密集型:数据库开销大,大部分线程会阻塞,故需要尽量多
 *                      参考公式: CPU核数/1-阻塞系数   阻塞系数在0.8~0.9之间
 *                      如8核cpu: 8/1-0.9 = 80个线程数
 *
 */
public class MyThreadPoolDemo {
    public static void main(String[] args) {
        ExecutorService threadPool = new ThreadPoolExecutor(2,
                5,  //设多少看cpu核数而定
                1L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(3),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());

        try{
            for (int i = 1; i <=9; i++) {
                threadPool.execute(()->{
                    System.out.println(Thread.currentThread().getName()+"\t 办理业务");
                });
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            threadPool.shutdown();
        }
    }

    public static void threadPoolInit(){
        System.out.println(Runtime.getRuntime().availableProcessors());  //查看cpu核数

        ExecutorService threadPool = Executors.newFixedThreadPool(5);  //一池5个处理线程
        //ExecutorService threadPool = Executors.newSingleThreadExecutor();  //一池一线程
        //ExecutorService threadPool = Executors.newCachedThreadPool();  //带缓冲可扩容

        try{
            for (int i = 1; i < 10; i++) {
                threadPool.execute(()->{
                    System.out.println(Thread.currentThread().getName()+"\t 办理业务");
                });
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            threadPool.shutdown();
        }
    }
}
