package entity.demo;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class SemaphoreDemo {
    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(3);  //模拟三个停车位
        for (int i = 1; i < 6; i++) {  //模拟六辆汽车
            new Thread(()->{
                try{
                    semaphore.acquire();  //抢占停车位
                    System.out.println(Thread.currentThread().getName()+"\t 抢到车位");
                    try {
                        TimeUnit.SECONDS.sleep(3);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName()+"\t 停车三秒后离开车位");
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally {
                    semaphore.release();  //释放停车位
                }
            },String.valueOf(i)).start();
        }
    }
}
