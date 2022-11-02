package com.github.deergate;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutionException;
import com.github.deergate.httpclient5.ProdPostUtil;

/**
 * Hello world!
 *
 */
public class App {
    
    public static void main(String[] args) throws InterruptedException {
        concurrentPost(160);
    }
    
    public static void test() throws InterruptedException, ExecutionException {
        Random random = new Random();
        byte[] tests = new byte[1024];
        for (int i = 0; i < tests.length; i++) {
            tests[i] = (byte) random.nextInt(Byte.MAX_VALUE * 2);
        }
        
        int sum=0;
        for (byte b : tests) {
            sum +=b;
        }
        System.out.println("data:"+sum);
        
        byte[] result=ProdPostUtil.post(tests, "127.0.0.1", 8089);
        sum=0;
        for (byte b : result) {
            sum +=b;
        }
        System.out.println("result:"+sum);
    }
    
    static void concurrentPost(int cnt) throws InterruptedException{
        Random random = new Random();
        byte[] tests = new byte[1024];
        for (int i = 0; i < tests.length; i++) {
            tests[i] = (byte) random.nextInt(Byte.MAX_VALUE * 2);
        }
        int[] timeSpans=new int[cnt];
        CyclicBarrier barrier=new CyclicBarrier(cnt);
        CountDownLatch latch = new CountDownLatch(cnt);
        for (int i = 0; i < cnt; i++) {
            int j=i;
            new Thread(()->{
                try {
                    barrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
                long start = System.currentTimeMillis();
                try {
                    byte[] result = ProdPostUtil.post(tests, "127.0.0.1", 8089);
                    long end = System.currentTimeMillis();
                    latch.countDown();
                    timeSpans[j]=(int) (end-start);
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                
            }).start();
        }
        
        latch.await();
        Arrays.sort(timeSpans);
        System.out.println(Arrays.toString(timeSpans));
    }
}
