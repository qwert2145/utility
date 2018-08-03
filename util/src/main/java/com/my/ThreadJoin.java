package com.my;

import java.util.concurrent.TimeUnit;

/**
 * Created by dell on 2018/7/31.
 */
public class ThreadJoin implements Runnable {
    int time;

    public ThreadJoin(int time){
        this.time = time;
    }

    public void run() {
        long startTime = System.currentTimeMillis();
//        System.out.println(Thread.currentThread().getName());
        try {
            TimeUnit.SECONDS.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " cost " + (System.currentTimeMillis() - startTime)/1000);
    }

    public static void main(String[] args) throws InterruptedException {
        //子线程执行完成后,主线程结束
        for(int i = 1;i<10;i++){
            Thread thread = new Thread(new ThreadJoin(i),"ThreadJoin" + i);
            thread.start();
            thread.join();
        }
        System.out.println("main");
    }
}
