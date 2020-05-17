package entity.demo;

import com.sun.org.apache.bcel.internal.generic.NEW;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * 多线程中第三种获取多线程的方式
 */
class MyThread implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
        return null;
    }
}
public class CallableDemo {


    public static void main(String[] args) throws ExecutionException, InterruptedException {

        /**
         * 多个线程使用同一个FutureTask,只会计算一次,要算多次就要起多个FutureTask
         */
        FutureTask<Integer> futureTask = new FutureTask<>(new MyThread());
        //FutureTask<Integer> futureTask2 = new FutureTask<>(new MyThread());
        new Thread(futureTask,"AAA").start();
        //new Thread(futureTask,"BBB").start();

        System.out.println(Thread.currentThread().getName()+"********************");
        int result01 = 100;

        //while (futureTask.isDone()){  //类似于自旋锁
//
       // }

        int result02 = futureTask.get();  //要求获得Callable线程的计算结果,如果没有计算结果,会去强求导致主线程阻塞,直到计算完成,所以建议获取结果的操作放到程序最后获取
        System.out.println("********result: "+(result01 + result02));
    }
}
