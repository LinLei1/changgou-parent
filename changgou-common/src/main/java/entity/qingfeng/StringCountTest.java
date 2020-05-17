package entity.qingfeng;

import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class StringCountTest {

    /**
     * 找出字符串中出现最多的字符
     */
    @Test
    public void test_count(){
        String str = "hgaklfhuihghfkshhgjgadshjfgkg";
        char res = str.charAt(0);
        int max = 0;  //最多出现次数
        Map<Character,Integer> map = new HashMap<>();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            Integer count = map.get(c);
            if (count == null){  //字符没有出现过
                count = 1;
            }else {
                count++;
            }
            map.put(c,count);
            if (count>max){
                res = c;
                max = count;
            }
        }
        System.out.println(res+"出现次数最多:"+max);
    }

    /**
     * 找出字符串中第一次重复出现的字符
     */
    @Test
    public void test_first(){
        String str = "fsakhgsfj";
        Set<Character> set = new HashSet<>();
        for (int i=0;i<str.length();i++){
            if (!set.add(str.charAt(i))){
                System.out.println(str.charAt(i));
                break;
            }
        }
    }

    /**
     * 找出字符串中第一个只出现一次的字符
     */
    @Test
    public void test_only_one(){
        String str = "shgjhgfjklg";
        Map<Character,Integer> map = new HashMap<>();
        for (int i=0;i<str.length();i++){
            Integer count = map.get(str.charAt(i));
            if (count == null){
                count = 1;
            }else {
                count++;
            }
            map.put(str.charAt(i),count);
        }
        for (int i = 0; i<str.length();i++){
            if (map.get(str.charAt(i))==1){
                System.out.println(str.charAt(i));
                break;
            }
        }
    }

    /**
     * 统计手机号各个数字个数,按升序输出 桶排序
     */
    @Test
    public void test_phone(){
        int[] count = new int[10];
        String mobile = "13587653456";
        for (int i = 0; i < mobile.length(); i++) {
            char c = mobile.charAt(i);
            count[c-'0']++;
        }
        for (int i = 0; i < count.length; i++) {
            if (count[i]>0){
                System.out.println(i+"出现了"+count[i]);
            }
        }

    }

    /**
     * 按字节数截取字符串
     */
    @Test
    public void test_str_substring(){
        //获取当前字符所占字节数
        //int len = String.valueOf('人').getBytes().length;
        //System.out.println(len);
        String str = "人ABC们DEF";
        int count =4;
        int sum = 0; //已经截取了多少
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            int len = String.valueOf(str.charAt(i)).getBytes().length;  //每个字符的字节数
            if (sum+len<=count){
                sum+=len;
                builder.append(str.charAt(i));
            }else {
                break;
            }
        }
        System.out.println(builder.toString());
    }

    /**
     * 从一个字符串中截取一段指定字符串
     */
    @Test
    public void test_str_sub(){
        String str = "<p id=\"text\">abcdefg</p>";
        String reg = "efg";
        int index = str.indexOf(reg);
        String target = str.substring(index,index+reg.length());
        System.out.println(target);
    }

    /**
     * 字符串反转
     */
    @Test
    public void test_str_reverse(){
        String str = "abcdefg";
        //StringBuilder builder = new StringBuilder();
        //System.out.println(builder.reverse());
        String des="";
        for (int i = str.length()-1; i >=0 ; i--) {
            des+=str.charAt(i);
        }
        System.out.println(des);
    }
}
