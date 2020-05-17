package entity.demo;

import java.util.concurrent.TimeUnit;

/**
 * 高并发请求服务器时,经常出现如下异常:java.lang.OutOfMemoryError: unable to create new native thread
 * 准确的讲,该异常与对应的平台有关
 *
 * 导致原因:
 *      1.你的应用创建了太多线程,超过了系统承载极限
 *      2.你的服务器并不允许你的应用创建这么多线程,linux系统默认单个进程可以创建线程数为1024个,超过这个数量,就报上述异常
 *
 * 解决办法:
 *      1.想办法降低程序创建线程数量,分析应用是否真的需要这么多线程
 *      2.实在需要这么多线程,可修改linux服务器配置,扩大linux默认限制
 *
 *
 */
public class UnableCreateNewThreadDemo {
    public static void main(String[] args) {

        //把这个程序扔到linux中就会出现上述错误,但root用户线程无上限
        //一旦出现,需查看进程并强制杀死进程
        for (int i = 0; ; i++) {
            System.out.println("********i="+i);

            new Thread(()->{
                try{ TimeUnit.SECONDS.sleep(5);} catch (InterruptedException e) { e.printStackTrace();}
            },""+i).start();
        }
    }
}
