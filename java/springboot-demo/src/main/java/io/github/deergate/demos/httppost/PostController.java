/*
 * Copyright 2021 Shang Yehua <niceshang@outlook.com>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package io.github.deergate.demos.httppost;

import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.io.IOException;
import java.io.InputStream;
import javax.annotation.PostConstruct;
import javax.servlet.AsyncContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * TODO PostController说明
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2021-06-24  16:54
 *
 */
@RestController
@Slf4j
public class PostController {

    @PostMapping("/post-mock")
    public byte[] binPostMock(HttpServletRequest request) throws IOException {
        InputStream inputStream = request.getInputStream();
        byte[] data = inputStream.readAllBytes();
        int sum = 0;
        for (byte b : data) {
            sum += b;
        }

        try {
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        } ;

        System.out.println("data:" + sum);
        Random random = new Random();
        byte[] result = new byte[100 + random.nextInt(1024)];
        for (int i = 0; i < result.length; i++) {
            result[i] = (byte) random.nextInt(Byte.MAX_VALUE * 2);
        }

        sum = 0;
        for (byte b : result) {
            sum += b;
        }
        System.out.println("result:" + sum);

        return result;
    }

    Map<String, DeferredResult<Object>> cache = new ConcurrentHashMap<>();

    @PostMapping("/post-async")
    public DeferredResult<Object> asynchPostTest(@RequestBody byte[] data) {
        log.info("request in");
        DeferredResult<Object> dr = new DeferredResult<>(4000L);

        try {
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        } ;
        Random random = new Random();
        byte[] result = new byte[100 + random.nextInt(1024)];
        int sum = 0;
        for (byte b : data) {
            sum += b;
        }
        log.info("data:" + sum);
        for (int i = 0; i < result.length; i++) {
            result[i] = (byte) random.nextInt(Byte.MAX_VALUE * 2);
        }

        sum = 0;
        for (byte b : result) {
            sum += b;
        }
        log.info("result:" + sum);
        dr.setResult(result);
        return dr;
    }

    @PostMapping("/post-async2")
    public void asynchBodyTest(HttpServletRequest request) throws InterruptedException {
        log.info("request in");
        AsyncContext context = request.startAsync();
        context.start(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            } ;
            InputStream inputStream;
            Random random = new Random();
            byte[] result = new byte[100 + random.nextInt(1024)];
            try {
                inputStream = request.getInputStream();
                byte[] data = inputStream.readAllBytes();
                int sum = 0;
                for (byte b : data) {
                    sum += b;
                }
                log.info("data:" + sum);
                for (int i = 0; i < result.length; i++) {
                    result[i] = (byte) random.nextInt(Byte.MAX_VALUE * 2);
                }

                sum = 0;
                for (byte b : result) {
                    sum += b;
                }
                log.info("result:" + sum);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            ServletResponse response = context.getResponse();
            try {
                ServletOutputStream outputStream = response.getOutputStream();
                outputStream.write(result);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            context.complete();
        });
    }

    ExecutorService executorService = new ThreadPoolExecutor(10, 200, 60L, TimeUnit.SECONDS,
            new SynchronousQueue<Runnable>(), new ThreadFactory() {
                private final ThreadGroup group;
                private final AtomicInteger threadNumber = new AtomicInteger(1);
                private final String namePrefix;

                {
                    SecurityManager s = System.getSecurityManager();
                    group = (s != null) ? s.getThreadGroup()
                            : Thread.currentThread().getThreadGroup();
                    namePrefix = "Aync" + "-task-";
                }

                public Thread newThread(Runnable r) {
                    Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(),
                            256);
                    if (t.isDaemon()) {
                        t.setDaemon(false);
                    }
                    if (t.getPriority() != Thread.NORM_PRIORITY)
                        t.setPriority(Thread.NORM_PRIORITY);
                    return t;
                }

            }, new ThreadPoolExecutor.CallerRunsPolicy());

    @PostMapping("/post-async3")
    public DeferredResult<Object> asynchBodyTest3(@RequestBody byte[] data)
            throws InterruptedException {
        log.info("request in");
        DeferredResult<Object> dr = new DeferredResult<>(4000L);
        executorService.submit(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            } ;
            Random random = new Random();
            byte[] result = new byte[100 + random.nextInt(1024)];
            int sum = 0;
            for (byte b : data) {
                sum += b;
            }
            log.info("data:" + sum);
            for (int i = 0; i < result.length; i++) {
                result[i] = (byte) random.nextInt(Byte.MAX_VALUE * 2);
            }

            sum = 0;
            for (byte b : result) {
                sum += b;
            }
            log.info("result:" + sum);
            dr.setResult(result);
        });
        return dr;
    }

    @PostConstruct
    public void init() {
        new Thread(() -> {
            while (true) {
                cache.forEach((key, val) -> {
                    val.setResult(UUID.randomUUID().toString());
                });
                cache.clear();
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
