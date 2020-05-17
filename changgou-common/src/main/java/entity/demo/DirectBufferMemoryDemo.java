package entity.demo;

import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

/**
 * java.lang.OutOfMemoryError: Direct buffer memory  //直接内存溢出  出现在NIO程序中
 *
 * 导致原因: 写NIO程序经常使用ByteBuffer来读取或写入数据.这是一种基于通道(Channel)与缓冲区(Buffer)的I/O方式
 * 它可以使用Native本地方法直接分配堆外内存,然后通过一个存储在java堆里面的DirectByteBuffer对象作为这块内存的引用进行操作
 * 这样在一些场景中可以显著提升性能,因为避免了在java堆与Native堆中来回复制数据
 *
 * ByteBuffer.allocate(capability) 这种方式是分配JVM堆内存,属于GC管辖范围,由于需要拷贝速度相对较慢
 * ByteBuffer.allocateDirect(capability): 这种方式是分配OS本地内存,不属于GC管辖范围,由于不需要内存拷贝所以速度相对较快
 *
 * 但如果不断分配本地内存,堆内存很少使用,那么JVM就不需要执行GC,DirectByteBuffer对象们就不会被回收
 * 这时候堆内存充足,但本地内存可能已经使用光了,再次尝试分配本地内存就OOM了,程序直接崩溃
 */
public class DirectBufferMemoryDemo {
    public static void main(String[] args) {
        System.out.println("配置的maxDirectMemory:"+(sun.misc.VM.maxDirectMemory() / 1024 /1024)+"MB");
        try{ TimeUnit.SECONDS.sleep(5);} catch (InterruptedException e) { e.printStackTrace();}

        //最大分配本地内存: -XX:MaxDirectMemorySize=5m
        ByteBuffer bb = ByteBuffer.allocateDirect(6*1024*1024);
    }
}
