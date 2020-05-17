package entity.demo;

import java.util.HashMap;
import java.util.WeakHashMap;

public class WeakHashMapDemo {
    public static void main(String[] args) {
        myHashMap();
        myWeakHashMap();
    }

    private static void myHashMap() {
        HashMap<Integer,String> map = new HashMap<>();
        Integer key = new Integer(1);
        String value = "HashMap";

        map.put(key,value);
        System.out.println(map);

        System.gc();
        System.out.println(map);
    }

    private static void myWeakHashMap() {
        WeakHashMap<Integer,String> weakMap = new WeakHashMap<>();
        Integer key = new Integer(2);
        String value = "HashMap";

        weakMap.put(key,value);
        System.out.println(weakMap);

        //GC后Map将被清空
        System.gc();
        System.out.println(weakMap.size());
    }
}
