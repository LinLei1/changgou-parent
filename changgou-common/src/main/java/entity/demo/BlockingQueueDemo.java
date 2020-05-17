package entity.demo;

import java.sql.SQLOutput;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 阻塞队列
 * 1. 当阻塞队列是空的,往队列中 获取 元素的操作(线程)将会被阻塞
 * 2. 当阻塞队列是满的,往队列中 添加 元素的操作(线程)将会被阻塞
 *
 * 为什么使用阻塞队列?
 *      好处是我们不需要关心什么时候需要阻塞线程,什么时候需要唤醒线程,由BlockingQueue一手包办
 *Collection(接口)->Queue(接口)->BlockingQueue(接口)->ArrayBlockingQueue: 由数组结构组成的有界阻塞队列
 *                                                   LinkedBlockingQueue: 由链表结构组成的有界阻塞队列(默认大小为Integer.MAX_VALUE,太大可当成无界使用)
 *                                                   SynchronousQueue: 不存储元素的阻塞队列,也即单个元素的队列
 *
 *
 */
public class BlockingQueueDemo {
    public static void main(String[] args) throws Exception {
        BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(3);
        /*System.out.println(blockingQueue.add("a"));  //此方法越界将抛异常
        System.out.println(blockingQueue.add("b"));
        System.out.println(blockingQueue.add("c"));

        System.out.println(blockingQueue.element());  //检查队列是否为空,并返回第一个元素

        System.out.println(blockingQueue.remove());  //表示按顺序移除 先进先出  越界将抛异常
        System.out.println(blockingQueue.remove());
        System.out.println(blockingQueue.remove());*/


        /*System.out.println(blockingQueue.offer("a"));  //此方法越界返回false
        System.out.println(blockingQueue.offer("b"));
        System.out.println(blockingQueue.offer("c"));

        System.out.println(blockingQueue.peek());  //检查队列是否为空,并返回第一个元素

        System.out.println(blockingQueue.poll());  //表示按顺序移除 先进先出  越界将返回空
        System.out.println(blockingQueue.poll());
        System.out.println(blockingQueue.poll());*/


        /*blockingQueue.put("a");  //此方法越界将阻塞,等待队列元素被消费腾空间
        blockingQueue.put("b");
        blockingQueue.put("c");

        blockingQueue.take();  //表示按顺序移除 先进先出  越界将阻塞,等待队列元素被添加进来
        blockingQueue.take();
        blockingQueue.take();*/


        System.out.println(blockingQueue.offer("a",2L, TimeUnit.SECONDS));  //此方法越界将阻塞2秒,两秒后仍未消费腾出空间返回false
        System.out.println(blockingQueue.offer("b",2L, TimeUnit.SECONDS));
        System.out.println(blockingQueue.offer("c",2L, TimeUnit.SECONDS));

        System.out.println(blockingQueue.poll(2L, TimeUnit.SECONDS));  //越界将阻塞2秒,仍未添加返回null
        System.out.println(blockingQueue.poll(2L, TimeUnit.SECONDS));
        System.out.println(blockingQueue.poll(2L, TimeUnit.SECONDS));
    }
}
