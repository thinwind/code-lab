/* 
 * Copyright 2021 Shang Yehua <niceshang@outlook.com>
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
package io.github.deergate.rocketmq;

import java.time.Duration;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;

/**
 *
 * TODO LimitedProducer说明
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2021-08-20  22:28
 *
 */
public class LimitedProducer {
    public static void main(String[] args) throws MQClientException {
        DefaultMQProducer producer = new DefaultMQProducer("BatchProducerGroupName");
        producer.setNamesrvAddr("192.168.31.149:9876");
        producer.start();
        
        
    }
    
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
}
