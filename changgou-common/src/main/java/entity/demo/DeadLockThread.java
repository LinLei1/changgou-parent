package entity.demo;
/*
 *死锁:两个或两个以上的线程在执行过程中,各自持有一把对方线程所需要的锁,而造成的一种相互等待的现象,若无外力干涉将会一直等待,如果系统资源充足,进程的资源请求
 *      都能得到满足,死锁出现的可能性很低
 *死锁产生的原因: 1.系统资源不足
 *               2.进程运行推进的顺序不合适
 *               3.资源分配不恰当
 *
 *定位死锁: 1. 在当前项目下使用控制台命令:jps -l 查看java进程编号
 *          2. 接着使用:jstack 线程编号 定位到故障,把日志信息截个图发给运维
 */

import java.util.concurrent.TimeUnit;

class HoldLockThread implements Runnable{

    private String lockA;
    private String lockB;

    public HoldLockThread(String lockA, String lockB) {
        this.lockA = lockA;
        this.lockB = lockB;
    }

    @Override
    public void run() {
        synchronized (lockA){
            System.out.println(Thread.currentThread().getName()+"\t 自己持有:"+lockA+"\t 尝试获得:"+lockB);
            try{ TimeUnit.SECONDS.sleep(2);} catch (InterruptedException e) { e.printStackTrace();}

            synchronized (lockB){
                System.out.println(Thread.currentThread().getName()+"\t 自己持有:"+lockB+"\t 尝试获得:"+lockA);
                try{ TimeUnit.SECONDS.sleep(2);} catch (InterruptedException e) { e.printStackTrace();}
            }
        }
    }
}
public class DeadLockThread {
    public static void main(String[] args) {
        String lockA = "lockA";
        String lockB = "lockB";

        new Thread(new HoldLockThread(lockA,lockB),"AAA").start();
        new Thread(new HoldLockThread(lockB,lockA),"BBB").start();
    }
}
