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

import java.util.concurrent.TimeoutException;
import java.io.IOException;
import java.time.Duration;
import io.github.resilience4j.core.IntervalFunction;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import io.vavr.CheckedFunction0;
import io.vavr.control.Try;

/**
 *
 * TODO RetryDemo说明
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2021-08-12  14:06
 *
 */
public class RetryDemo {

    Object failed = new Object();

    public void createAndConfig() {
        RetryConfig config = RetryConfig.custom().maxAttempts(3)
                .waitDuration(Duration.ofMillis(1000)).retryOnResult(response -> response == failed)
                .retryOnException(e -> e instanceof RuntimeException)
                .retryExceptions(IOException.class, TimeoutException.class)
                // .ignoreExceptions(BusinessException.class, OtherBusinessException.class)
                .failAfterMaxAttempts(true).build();

        // Create a RetryRegistry with a custom global configuration
        RetryRegistry registry = RetryRegistry.of(config);

        // Get or create a Retry from the registry - 
        // Retry will be backed by the default config
        Retry retry = registry.retry("name1");

        // Get or create a Retry from the registry, 
        // use a custom configuration when creating the retry
        // RetryConfig custom = RetryConfig.custom().waitDuration(Duration.ofMillis(100)).build();

        // Retry retryWithCustomConfig = registry.retry("name2", custom);


        // Create a Retry with default configuration
        // Retry retry = Retry.ofDefaults("id");
        // Decorate the invocation of the HelloWorldService
        CheckedFunction0<Object> retryableSupplier =
                Retry.decorateCheckedSupplier(retry, this::someMethod);

        // When I invoke the function
        Try<Object> result = Try.of(retryableSupplier)
                .recover((throwable) -> "Hello world from recovery function");
        System.out.println(result.get() == failed);
        System.out.println(retry.getMetrics());
    }

    public void customIntervalFun() {
        IntervalFunction defaultWaitInterval = IntervalFunction.ofDefaults();

        // This interval function is used internally 
        // when you only configure waitDuration
        IntervalFunction fixedWaitInterval = IntervalFunction.of(Duration.ofSeconds(5));

        IntervalFunction intervalWithExponentialBackoff = IntervalFunction.ofExponentialBackoff();

        IntervalFunction intervalWithCustomExponentialBackoff = IntervalFunction
                .ofExponentialBackoff(IntervalFunction.DEFAULT_INITIAL_INTERVAL, 2d);

        IntervalFunction randomWaitInterval = IntervalFunction.ofRandomized();

        // Overwrite the default intervalFunction with your custom one
        RetryConfig retryConfig =
                RetryConfig.custom().intervalFunction(intervalWithExponentialBackoff).build();
    }

    public static void main(String[] args) {
        new RetryDemo().createAndConfig();
    }

    Object someMethod() {
        if (Math.random() > 0.3) {
            return failed;
        }
        return new Object();
    }
}
