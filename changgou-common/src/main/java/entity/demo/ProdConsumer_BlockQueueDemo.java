package entity.demo;


import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 使用阻塞队列,完成生消模式
 */
class MyResource{
    private volatile boolean FLAG = true;  //默认开启 进行生产+消费
    private AtomicInteger atomicInteger = new AtomicInteger();  //默认0开始
    BlockingQueue<String> blockingQueue = null;

    public MyResource(BlockingQueue<String> blockingQueue){
        this.blockingQueue = blockingQueue;
        System.out.println(blockingQueue.getClass().getName());
    }

    public void myProd()throws Exception{
        String data = null;
        boolean retValue;
        while (FLAG){
            data = atomicInteger.incrementAndGet()+"";
            retValue = blockingQueue.offer(data,2L, TimeUnit.SECONDS);
            if (retValue){
                System.out.println(Thread.currentThread().getName()+"\t 插入队列"+data+"成功!");
            }else {
                System.out.println(Thread.currentThread().getName()+"\t 插入队列"+data+"失败!");
            }
            TimeUnit.SECONDS.sleep(1);
        }
        System.out.println(Thread.currentThread().getName()+"\t 大老板叫停,FLAG = false了!生产动作结束!");
    }

    public void myConsumer()throws Exception{
        String result = null;
        while (FLAG){
            result = blockingQueue.poll(2L,TimeUnit.SECONDS);
            if (null == result || result.equals("")){
                FLAG = false;
                System.out.println(Thread.currentThread().getName()+"\t 超过两秒没有取到蛋糕,消费退出");
                System.out.println();
                System.out.println();
                return;
            }
            System.out.println(Thread.currentThread().getName()+"\t 消费蛋糕"+result+"成功!");
        }
    }

    public void stop() throws Exception{
        this.FLAG = false;
    }

}
public class ProdConsumer_BlockQueueDemo {
    public static void main(String[] args) throws Exception {
        MyResource resource = new MyResource(new ArrayBlockingQueue<>(10));
        new Thread(()->{
            System.out.println("生产线程已启动");
            try {
                resource.myProd();
            } catch (Exception e) {
                e.printStackTrace();
            }
        },"Prod").start();

        new Thread(()->{
            System.out.println("消费线程已启动");
            try {
                resource.myConsumer();
            } catch (Exception e) {
                e.printStackTrace();
            }
        },"Consumer").start();

        //暂停一会儿线程
        try{ TimeUnit.SECONDS.sleep(5);} catch (InterruptedException e) { e.printStackTrace();}
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println("5秒时间到,大老板main线程叫停,活动结束!");

        resource.stop();
    }
}
