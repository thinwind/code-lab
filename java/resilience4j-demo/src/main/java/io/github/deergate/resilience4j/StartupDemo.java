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
package io.github.deergate.resilience4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;
import java.time.Duration;
import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.bulkhead.BulkheadFullException;
import io.github.resilience4j.bulkhead.ThreadPoolBulkhead;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.decorators.Decorators;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.timelimiter.TimeLimiter;
import io.vavr.control.Try;
import static java.util.Arrays.*;

/**
 *
 * TODO StartupDemo说明
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2021-08-11  13:38
 *
 */
public class StartupDemo {

    public void base() {
        // Create a CircuitBreaker with default configuration
        CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults("backendService");

        // Create a Retry with default configuration
        // 3 retry attempts and a fixed time interval between retries of 500ms
        Retry retry = Retry.ofDefaults("backendService");

        // Create a Bulkhead with default configuration
        Bulkhead bulkhead = Bulkhead.ofDefaults("backendService");

        // Supplier<String> supplier = () -> backendService
        // .doSomething(param1, param2)
        Supplier<String> supplier = () -> "Hello";

        // Decorate your call to backendService.doSomething() 
        // with a Bulkhead, CircuitBreaker and Retry
        // **note: you will need the resilience4j-all dependency for this
        Supplier<String> decoratedSupplier =
                Decorators.ofSupplier(supplier).withCircuitBreaker(circuitBreaker)
                        .withBulkhead(bulkhead).withRetry(retry).decorate();

        // Execute the decorated supplier and recover from any exception
        String result =
                Try.ofSupplier(decoratedSupplier).recover(throwable -> "Hello from Recovery").get();

        // When you don't want to decorate your lambda expression,
        // but just execute it and protect the call by a CircuitBreaker.
        // String result = circuitBreaker.executeSupplier(backendService::doSomething);

        // You can also run the supplier asynchronously in a ThreadPoolBulkhead
        ThreadPoolBulkhead threadPoolBulkhead = ThreadPoolBulkhead.ofDefaults("backendService");

        // The Scheduler is needed to schedule a timeout 
        // on a non-blocking CompletableFuture
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(3);
        TimeLimiter timeLimiter = TimeLimiter.of(Duration.ofSeconds(1));

        CompletableFuture<String> future = Decorators.ofSupplier(supplier)
                .withThreadPoolBulkhead(threadPoolBulkhead)
                .withTimeLimiter(timeLimiter, scheduledExecutorService)
                .withCircuitBreaker(circuitBreaker)
                .withFallback(asList(TimeoutException.class, CallNotPermittedException.class,
                        BulkheadFullException.class), throwable -> "Hello from Recovery")
                .get().toCompletableFuture();
    }
}
