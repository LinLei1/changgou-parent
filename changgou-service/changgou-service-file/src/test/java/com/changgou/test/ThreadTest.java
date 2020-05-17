package com.changgou.test;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Classname ThreadTest
 * @Description TODO
 * @Date 2020/4/25 16:55
 * @Created by _Del
 */

class SaleTickets{
    private int number = 100;
    private Lock lock = new ReentrantLock();

    public void sale(){
        lock.lock();
        try {
            if(number>0){
                System.out.println(Thread.currentThread().getName()+"\t卖出第:"+(number--)+"\t还剩下:"+number);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
public class ThreadTest {
    public static void main(String[] args) {
        SaleTickets tickets = new SaleTickets();
        //使用lambda表达式
        new Thread(()-> { for (int i = 0; i < 110; i++) tickets.sale(); },"AAA").start();
        new Thread(()-> { for (int i = 0; i < 110; i++) tickets.sale(); },"BBB").start();
        new Thread(()-> { for (int i = 0; i < 110; i++) tickets.sale(); },"CCC").start();
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 110; i++) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    tickets.sale();
                }
            }
        },"AAA").start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 110; i++) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    tickets.sale();
                }
            }
        },"BBB").start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 110; i++) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    tickets.sale();
                }
            }
        },"CCC").start();*/
    }
}
