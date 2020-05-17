package com.changgou.test;

import java.util.concurrent.TimeUnit;

/**
 * @Classname Lock8
 * @Description TODO
 * @Date 2020/4/25 21:19
 * @Created by _Del
 */
class Phone{
    public static synchronized void sendEmail()throws Exception{
        try { TimeUnit.SECONDS.sleep(4); } catch (InterruptedException e) { e.printStackTrace(); }
        System.out.println("----------sendEmail");
    }
    public synchronized void sendSMS()throws Exception{
        System.out.println("-----------sendSMS");
    }
    public void hello(){
        System.out.println("-------hello");
    }
}

/**
 * 题目:多线程8锁
 * 1.标准访问: 先打印发邮件还是发短信?
 * 2.邮件方法暂停4秒,先打印哪个?
 * 3.新增一个普通方法,先打印哪个?
 * 4.两部手机,先打印哪一个?
 * 5.两个静态同步方法,1部手机?
 * 6.两个静态同步方法,2部手机?
 * 7.一个静态,一个普通,1部手机
 * 7.一个静态,一个普通,2部手机
 */
public class Lock8 {
    public static void main(String[] args) throws Exception {
        Phone phone = new Phone();
        Phone phone1 = new Phone();
        new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            phone.sendEmail();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },"A").start();
        Thread.sleep(100);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //phone.sendSMS();
                    //phone.hello();
                    phone1.sendSMS();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        },"B").start();
    }
}
