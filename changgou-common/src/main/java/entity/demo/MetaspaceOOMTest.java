package entity.demo;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * JVM参数: -XX:MetaspaceSize=8m -XX:MaxMetaspaceSize=8m
 *
 * 元空间存放了以下信息:
 *      1.虚拟机加载的类信息
 *      2.常量池
 *      3.静态变量
 *      4.即时编译后的代码
 * 模拟Metaspace空间溢出: 不断生成类,往元空间里灌,类占据的空间总是会超过Metaspace指定的空间大小
 *
 * java.lang.OutOfMemoryError: Metaspace
 *
 *
 */
public class MetaspaceOOMTest {
    static class OOMTest{}
    public static void main(String[] args) {
        int i = 0;  //模拟计数多少次后发生异常

        try{
            while (true){
                i++;
                Enhancer enhancer = new Enhancer();
                enhancer.setSuperclass(OOMTest.class);
                enhancer.setUseCache(false);
                enhancer.setCallback(new MethodInterceptor() {
                    @Override
                    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {

                        return methodProxy.invokeSuper(o,args);
                    }
                });
                enhancer.create();
            }
        }catch (Throwable e){
            System.out.println("*********多少次后发生异常:"+i);
            e.printStackTrace();
        }
    }
}
