package entity.demo;

import java.util.ArrayList;
import java.util.List;

/**
 * GC回收时间过长,98%的时间用于GC并且回收了不到2%的堆内存,并且很快又会被填满,再次GC的恶行循环,
 * cpu使用率一直是100%,而GC没有任何成果
 * java.lang.OutOfMemoryError: GC overhead limit exceeded
 */
public class GCOverheadDemo {
    public static void main(String[] args) {
        int i = 0;
        List<String> list = new ArrayList<>();

        try{
            while (true){
                list.add(String.valueOf(++i).intern());
            }
        }catch (Throwable e){
            System.out.println("**********i:"+i);
            e.printStackTrace();
            throw e;
        }
    }
}
