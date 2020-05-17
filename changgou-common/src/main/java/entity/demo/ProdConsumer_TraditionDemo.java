package entity.demo;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 生消模式
 *
 * synchronized与Lock的区别?
 *      1.synchronized是jvm系统级别的关键字,Lock是api层面,是java的类
 *      2.synchronized不需要手动释放锁,底层无论代码是否发生异常都会释放锁,Lock需要手动释放,如果没有释放,有可能导致死锁
 *      3.synchronized执行不可中断,而Lock可以调用interrupt()方法中断同步
 *      4.synchronized只能是非公平锁,而Lock可以根据构造方法指定
 *      5.Lock可以绑定多个Condition实现精准唤醒,synchronized只能随机唤醒一个或全部唤醒
 *
 */

class ShareData{
    private int number = 0;
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    /**
     * 生产者方法
     * @throws Exception
     */
    public void increment()throws Exception{
        lock.lock();
        try {
            //判断
            while (number != 0){
                //等待,不能生产
                condition.await();
            }
            //干活
            number++;
            System.out.println(Thread.currentThread().getName()+"\t"+number);
            //通知唤醒
            condition.signalAll();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }

    /**
     * 消费者方法
     * @throws Exception
     */
    public void decrement()throws Exception{
        lock.lock();
        try {
            //判断
            while (number == 0){
                //等待,不能生产
                condition.await();
            }
            //干活
            number--;
            System.out.println(Thread.currentThread().getName()+"\t"+number);
            //通知唤醒
            condition.signalAll();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
}


public class ProdConsumer_TraditionDemo {
    public static void main(String[] args) {
        ShareData shareData = new ShareData();
        new Thread(()->{
            for (int i = 1; i <=5; i++) {
                try {
                    shareData.increment();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        },"AAA").start();

        new Thread(()->{
            for (int i = 1; i <=5; i++) {
                try {
                    shareData.decrement();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        },"BBB").start();
    }
}
