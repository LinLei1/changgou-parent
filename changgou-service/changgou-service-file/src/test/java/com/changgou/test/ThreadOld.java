package com.changgou.test;

/**
 * @Classname AirConditioner
 * @Description 资源类  老版本的线程交互
 * @Date 2020/4/25 19:22
 * @Created by _Del
 */
class AirConditioner {
    private int number = 0;

    public synchronized void increment() throws InterruptedException {
        //判断 使用while取代if 防止虚假唤醒 当只有一个消费者线程,一个生产者线程的时候,if不会有问题
        while (number!=0){
            this.wait();
        }
        //干活
        number++;
        System.out.println(Thread.currentThread().getName()+"\t"+number);
        //通知
        this.notifyAll();
    }

    public synchronized void decrement() throws InterruptedException {
        while (number==0){
            this.wait();
        }
        number--;
        System.out.println(Thread.currentThread().getName()+"\t"+number);
        this.notifyAll();
    }
}
public class ThreadOld{
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
