package entity.qingfeng;

public class BigNumberAdd {
    public static void main(String[] args) {
        String a = "100001";
        String b = "9999999";

        char[] large = null;  //大
        char[] small = null;  //小

        if (a.length()>=b.length()){
            large = a.toCharArray();
            small = b.toCharArray();
        }else {
            large = b.toCharArray();
            small = a.toCharArray();
        }
        int[] sums = new int[large.length+1];  //最终的结果位,最高位可能是0
        for (int i = 0; i < large.length; i++) {
            sums[i] = large[large.length-i-1]-'0';
        }
        for (int i = 0; i < small.length; i++) {
            sums[i] = small[small.length-i-1]-'0';
        }
        for (int i = 0; i < sums.length - 1; i++) {
            if (sums[i]>9){
                sums[i+1]+=sums[i]/10;  //进位
                sums[i]%=10;
            }
        }
        StringBuilder builder = new StringBuilder();
        for (int i =sums.length-1; i >=0 ; i--) {
            builder.append(sums[i]);
        }
        String result = builder.toString();
        if (result.startsWith("0")){
            result = result.substring(1);
        }
        System.out.println(result);
    }
}
