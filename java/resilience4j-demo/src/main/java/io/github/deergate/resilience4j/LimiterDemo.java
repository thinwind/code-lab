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

import java.util.function.Consumer;
import java.time.Duration;
import static io.vavr.API.println;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.vavr.CheckedRunnable;
import io.vavr.control.Try;

/**
 *
 * TODO LimiterDemo说明
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2021-08-11  17:27
 *
 */
public class LimiterDemo {
    public void registry() {
        RateLimiterConfig config =
                RateLimiterConfig.custom().limitRefreshPeriod(Duration.ofMillis(1))
                        .limitForPeriod(10).timeoutDuration(Duration.ofMillis(25)).build();

        // Create registry
        RateLimiterRegistry rateLimiterRegistry = RateLimiterRegistry.of(config);

        // Use registry
        RateLimiter rateLimiterWithDefaultConfig = rateLimiterRegistry.rateLimiter("name1");

        RateLimiter rateLimiterWithCustomConfig = rateLimiterRegistry.rateLimiter("name2", config);
    }

    public void decorate() {
        RateLimiterConfig config =
                RateLimiterConfig.custom().limitRefreshPeriod(Duration.ofMillis(1))
                        .limitForPeriod(10).timeoutDuration(Duration.ofMillis(25)).build();

        // Create registry
        RateLimiterRegistry rateLimiterRegistry = RateLimiterRegistry.of(config);
        RateLimiter rateLimiter = rateLimiterRegistry.rateLimiter("name1");
        // Decorate your call to BackendService.doSomething()
        CheckedRunnable restrictedCall =
                RateLimiter.decorateCheckedRunnable(rateLimiter, () -> println("Hold on, Man!!"));

        Try.run(restrictedCall).andThenTry(restrictedCall)
                .onFailure((throwable) -> println("Wait before call it again :)"));
    }

    public void changeRuntime() {
        RateLimiterConfig config =
                RateLimiterConfig.custom().limitRefreshPeriod(Duration.ofMillis(1))
                        .limitForPeriod(10).timeoutDuration(Duration.ofMillis(25)).build();

        // Create registry
        RateLimiterRegistry rateLimiterRegistry = RateLimiterRegistry.of(config);
        RateLimiter rateLimiter = rateLimiterRegistry.rateLimiter("name1");
        // Decorate your call to BackendService.doSomething()
        CheckedRunnable restrictedCall =
                RateLimiter.decorateCheckedRunnable(rateLimiter, () -> println("Hold on, Man!!"));

        // during second refresh cycle limiter will get 100 permissions
        rateLimiter.changeLimitForPeriod(100);
    }

    public static void main(String[] args) {
        new LimiterDemo().decorate();
    }
}
