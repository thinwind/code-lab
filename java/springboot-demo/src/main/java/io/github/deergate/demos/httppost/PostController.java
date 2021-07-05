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

import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

/**
 *
 * TODO PostController说明
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2021-06-24  16:54
 *
 */
@RestController
public class PostController {

    @PostMapping("/post-mock")
    public byte[] binPostMock(HttpServletRequest request) throws IOException {
        InputStream inputStream = request.getInputStream();
        byte[] data = inputStream.readAllBytes();
        int sum = 0;
        for (byte b : data) {
            sum += b;
        }
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
    public DeferredResult<Object> asynchPostTest() {
        DeferredResult<Object> result = new DeferredResult<>(2000L);
        cache.put("DeferredResult", result);
        return result;
    }

    @PostConstruct
    public void init() {
        new Thread(() -> {
            while (true) {
                cache.forEach((key,val)->{
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
