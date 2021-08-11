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

import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.time.Duration;
import static io.vavr.API.println;
import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.bulkhead.BulkheadConfig;
import io.github.resilience4j.bulkhead.BulkheadRegistry;
import io.github.resilience4j.bulkhead.ThreadPoolBulkhead;
import io.github.resilience4j.bulkhead.ThreadPoolBulkheadConfig;
import io.github.resilience4j.bulkhead.ThreadPoolBulkheadRegistry;
import io.vavr.CheckedFunction0;
import io.vavr.control.Try;

/**
 *
 * TODO BulkheadDemo说明
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2021-08-11  16:46
 *
 */
public class BulkheadDemo {

    public void registry() {
        BulkheadRegistry bulkheadRegistry = BulkheadRegistry.ofDefaults();

        ThreadPoolBulkheadRegistry threadPoolBulkheadRegistry =
                ThreadPoolBulkheadRegistry.ofDefaults();
    }

    public void bulkheadCustom() {
        // Create a custom configuration for a Bulkhead
        BulkheadConfig config = BulkheadConfig.custom().maxConcurrentCalls(150)
                .maxWaitDuration(Duration.ofMillis(500)).build();

        // Create a BulkheadRegistry with a custom global configuration
        BulkheadRegistry registry = BulkheadRegistry.of(config);

        // Get or create a Bulkhead from the registry - 
        // bulkhead will be backed by the default config
        Bulkhead bulkheadWithDefaultConfig = registry.bulkhead("name1");

        // Get or create a Bulkhead from the registry, 
        // use a custom configuration when creating the bulkhead
        BulkheadConfig custom = null;
        Bulkhead bulkheadWithCustomConfig = registry.bulkhead("name2", custom);
    }

    public void threadPoolBulkheadRegistry() {
        ThreadPoolBulkheadConfig config = ThreadPoolBulkheadConfig.custom().maxThreadPoolSize(10)
                .coreThreadPoolSize(2).queueCapacity(20).build();

        // Create a BulkheadRegistry with a custom global configuration
        ThreadPoolBulkheadRegistry registry = ThreadPoolBulkheadRegistry.of(config);

        // Get or create a ThreadPoolBulkhead from the registry - 
        // bulkhead will be backed by the default config
        ThreadPoolBulkhead bulkheadWithDefaultConfig = registry.bulkhead("name1");

        // Get or create a Bulkhead from the registry, 
        // use a custom configuration when creating the bulkhead
        ThreadPoolBulkheadConfig custom =
                ThreadPoolBulkheadConfig.custom().maxThreadPoolSize(5).build();

        ThreadPoolBulkhead bulkheadWithCustomConfig = registry.bulkhead("name2", custom);
    }

    public void decorate() {
        BulkheadConfig config = BulkheadConfig.custom().maxConcurrentCalls(150)
                .maxWaitDuration(Duration.ofMillis(500)).build();
        // Given
        Bulkhead bulkhead = Bulkhead.of("name", config);

        // When I decorate my function
        CheckedFunction0<String> decoratedSupplier = Bulkhead.decorateCheckedSupplier(bulkhead,
                () -> "This can be any method which returns: 'Hello");

        // and chain an other function with map
        Try<String> result = Try.of(decoratedSupplier).map(value -> value + " world'");

        System.out.println(result.get());

    }

    public void decorateThreadPool() throws InterruptedException, ExecutionException {
        ThreadPoolBulkheadConfig config = ThreadPoolBulkheadConfig.custom().maxThreadPoolSize(10)
                .coreThreadPoolSize(2).queueCapacity(20).build();

        ThreadPoolBulkhead bulkhead = ThreadPoolBulkhead.of("name", config);

        CompletionStage<String> supplier = bulkhead.executeSupplier(() -> "Hold on,Man!");
        System.out.println(supplier.toCompletableFuture().get());
    }

    public void registryEvents() {
        BulkheadRegistry registry = BulkheadRegistry.ofDefaults();
        registry.getEventPublisher().onEntryAdded(entryAddedEvent -> {
            Bulkhead addedBulkhead = entryAddedEvent.getAddedEntry();
            println("Bulkhead added:" + addedBulkhead.getName());
        }).onEntryRemoved(entryRemovedEvent -> {
            Bulkhead removedBulkhead = entryRemovedEvent.getRemovedEntry();
            println("Bulkhead removed" + removedBulkhead.getName());
        });

        //bulkhead.getEventPublisher()
        // .onCallPermitted(event -> logger.info(...))
        // .onCallRejected(event -> logger.info(...))
        // .onCallFinished(event -> logger.info(...));
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        new BulkheadDemo().decorateThreadPool();
    }
}
