/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for additional information regarding
 * copyright ownership. The ASF licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package io.github.deergate.rocketmq;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.io.IOException;
import java.time.Duration;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;

public class TagFilterConsumer {


    static RateLimiter rateLimiter() {
        int tps = 500;
        int cycle = 100;
        RateLimiterConfig config =
                RateLimiterConfig.custom().limitRefreshPeriod(Duration.ofMillis(1000 / cycle))
                        .limitForPeriod(tps / cycle)
                        .timeoutDuration(Duration.ofMillis(1000))
                        .writableStackTraceEnabled(false)
                        .build();

        // Create registry
        RateLimiterRegistry rateLimiterRegistry = RateLimiterRegistry.of(config);
        RateLimiter rateLimiter = rateLimiterRegistry.rateLimiter("name1");
        
        return rateLimiter;
    }

    public static void main(String[] args)
            throws InterruptedException, MQClientException, IOException {

        DefaultMQPushConsumer consumer =
                new DefaultMQPushConsumer("please_rename_unique_group_name");
        consumer.setNamesrvAddr("127.0.0.1:9876");
        consumer.setConsumeMessageBatchMaxSize(1);
        consumer.subscribe("BatchTest", "Tag");
        // consumer.setConsumeThreadMin(2);
        // consumer.setConsumeThreadMax(4);
        // int tps = 4000;
        // int cycle = 100;
        // consumer.setPullBatchSize(tps / cycle);
        // consumer.setPullInterval(1000 / cycle);
        // consumer.setPullThresholdForTopic(10);
        AtomicInteger idx = new AtomicInteger(0);

        RateLimiter rateLimiter = rateLimiter();
        Consumer<List<MessageExt>> msgConsumer =
                RateLimiter.decorateConsumer(rateLimiter, msgs -> msgs.size(), msgs -> {
                    idx.incrementAndGet();
                    if (msgs.size() > 1) {
                        System.out.printf("%s Receive New Messages: %d %n",
                                Thread.currentThread().getName(), msgs.size());
                    }
                });

        // Consumer<List<MessageExt>> msgConsumer = msgs -> {
        //     idx.incrementAndGet();
        //     if (msgs.size() > 1) {
        //         System.out.printf("%s Receive New Messages: %d %n",
        //                 Thread.currentThread().getName(), msgs.size());
        //     }
        // };

        consumer.registerMessageListener(new MessageListenerConcurrently() {

            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,
                    ConsumeConcurrentlyContext context) {
                try {
                    msgConsumer.accept(msgs);
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                } catch (Exception e) {
                    System.out.println("Exception:"+e.toString());
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }

            }
        });
        consumer.start();
        System.out.printf("Consumer Started.%n");
        long start = System.currentTimeMillis();
        while (idx.get() < 20000) {
            Thread.sleep(3);
        }
        long end = System.currentTimeMillis();
        System.out.println("Rate: " + idx.get() / ((end - start) / 1000));
    }
}
