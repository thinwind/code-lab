package com.github.deergate;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import com.github.deergate.httpclient5.AsyncPostUtil;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
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
        
        byte[] result=AsyncPostUtil.post(tests, "127.0.0.1", 8089);
        sum=0;
        for (byte b : result) {
            sum +=b;
        }
        System.out.println("result:"+sum);
    }
}
