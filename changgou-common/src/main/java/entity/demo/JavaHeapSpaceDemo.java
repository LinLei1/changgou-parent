package entity.demo;

import java.util.Random;

//java.lang.OutOfMemoryError: Java heap space
public class JavaHeapSpaceDemo {

    public static void main(String[] args) {
        String str = "hello";

        /*while (true){
            str+= str+new Random().nextInt(111111)+new Random().nextInt(2222222);
            str.intern();
        }*/

        byte[] bytes = new byte[1024*1024*1024]; //1G
    }
}
