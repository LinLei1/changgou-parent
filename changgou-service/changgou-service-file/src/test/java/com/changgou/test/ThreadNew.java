package com.changgou.test;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Classname ThreadNew
 * @Description TODO
 * @Date 2020/4/25 20:14
 * @Created by _Del
 */
class AirConditioner1 {
    private int number = 0;
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    public void increment() throws InterruptedException {
        //判断 使用while取代if 防止虚假唤醒
        lock.lock();
        try {
            while (number!=0){
                condition.await();
            }
            //干活
            number++;
            System.out.println(Thread.currentThread().getName()+"\t"+number);
            //通知
            condition.signalAll();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void decrement() throws InterruptedException {
        lock.lock();
        try {
            while (number==0){
                condition.signalAll();
            }
            number--;
            System.out.println(Thread.currentThread().getName()+"\t"+number);
            condition.signalAll();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
public class ThreadNew{
    public static void main(String[] args) {
        AirConditioner airConditioner = new AirConditioner();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 1; i <=30; i++) {
                    try {
                        airConditioner.increment();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        },"AAA").start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 1; i <=30; i++) {
                    try {
                        airConditioner.decrement();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        },"BBB").start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 1; i <=30; i++) {
                    try {
                        airConditioner.increment();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        },"CCC").start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 1; i <=30; i++) {
                    try {
                        airConditioner.decrement();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        },"DDD").start();
    }
}
