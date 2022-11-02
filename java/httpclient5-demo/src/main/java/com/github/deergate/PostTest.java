/* 
 * Copyright 2022 Shang Yehua <niceshang@outlook.com>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.deergate;

import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.deergate.httpclient5.ConsumerManager;
import com.github.deergate.httpclient5.Runner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 *
 * TODO 说明
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2022-10-20  15:15
 *
 */
@SpringBootApplication
public class PostTest {
    
    public static void main(String[] args) {
        SpringApplication.run(PostTest.class, args);
    }
    
    @Bean
    CommandLineRunner post(ObjectMapper mapper,Runner runner) {
        System.out.println("-------------> post");
        return args->{
            System.out.println("-------------> posting...");
            // runner.run(mapper);
            // System.out.println("-------------> posted.");
            CountDownLatch latch = new CountDownLatch(20);
            
            for (int i = 0; i < 20; i++) {
                new Thread(()->{
                    try {
                        runner.run(mapper);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }finally{
                        latch.countDown();
                    }
                }).start();;
            }
            latch.await();
            ConsumerManager.printAllConsumers();
            System.out.println("-------------> end.");
        };
    }
}
