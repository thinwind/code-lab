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

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;
import java.util.function.Supplier;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.time.Duration;
import static io.vavr.API.println;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.SlidingWindowType;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.core.RegistryStore;
import io.github.resilience4j.core.SupplierUtils;
import io.github.resilience4j.core.registry.EntryAddedEvent;
import io.github.resilience4j.core.registry.EntryRemovedEvent;
import io.github.resilience4j.core.registry.EntryReplacedEvent;
import io.github.resilience4j.core.registry.RegistryEventConsumer;
import io.vavr.CheckedFunction0;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import io.vavr.control.Try;

/**
 *
 * TODO CircuitBreakerDemo说明
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2021-08-11  14:52
 *
 */
public class CircuitBreakerDemo {

    public void demo() {
        CircuitBreakerRegistry circuitBreakerRegistry = CircuitBreakerRegistry.ofDefaults();

        // Create a custom configuration for a CircuitBreaker
        CircuitBreakerConfig circuitBreakerConfig =
                CircuitBreakerConfig.custom().failureRateThreshold(50).slowCallRateThreshold(50)
                        .waitDurationInOpenState(Duration.ofMillis(1000))
                        .slowCallDurationThreshold(Duration.ofSeconds(2))
                        .permittedNumberOfCallsInHalfOpenState(3).minimumNumberOfCalls(10)
                        .slidingWindowType(SlidingWindowType.TIME_BASED).slidingWindowSize(5)
                        // .recordException(e -> INTERNAL_SERVER_ERROR.equals(getResponse().getStatus()))
                        .recordExceptions(IOException.class, TimeoutException.class)
                        // .ignoreExceptions(BusinessException.class, OtherBusinessException.class)
                        .build();

        // Create a CircuitBreakerRegistry with a custom global configuration
        CircuitBreakerRegistry circuitBreakerRegistry2 =
                CircuitBreakerRegistry.of(circuitBreakerConfig);

        // Get or create a CircuitBreaker from the CircuitBreakerRegistry 
        // with the global default configuration
        CircuitBreaker circuitBreakerWithDefaultConfig =
                circuitBreakerRegistry.circuitBreaker("name1");

        // Get or create a CircuitBreaker from the CircuitBreakerRegistry 
        // with a custom configuration
        CircuitBreaker circuitBreakerWithCustomConfig =
                circuitBreakerRegistry.circuitBreaker("name2", circuitBreakerConfig);
    }

    public void shareConfig() {
        CircuitBreakerRegistry circuitBreakerRegistry = CircuitBreakerRegistry.ofDefaults();
        CircuitBreakerConfig circuitBreakerConfig =
                CircuitBreakerConfig.custom().failureRateThreshold(70).build();

        circuitBreakerRegistry.addConfiguration("someSharedConfig", circuitBreakerConfig);

        CircuitBreaker circuitBreaker =
                circuitBreakerRegistry.circuitBreaker("name", "someSharedConfig");
    }

    public void overrideConfig() {
        CircuitBreakerRegistry circuitBreakerRegistry = CircuitBreakerRegistry.ofDefaults();
        CircuitBreakerConfig defaultConfig = circuitBreakerRegistry.getDefaultConfig();

        CircuitBreakerConfig overwrittenConfig = CircuitBreakerConfig.from(defaultConfig)
                .waitDurationInOpenState(Duration.ofSeconds(20)).build();
    }

    public void standaloneCB() {
        // Create a custom configuration for a CircuitBreaker
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                .recordExceptions(IOException.class, TimeoutException.class)
                // .ignoreExceptions(BusinessException.class, OtherBusinessException.class)
                .build();

        CircuitBreaker customCircuitBreaker = CircuitBreaker.of("testName", circuitBreakerConfig);
    }

    public void registryBuilder() {
        Map<String, String> circuitBreakerTags = HashMap.of("key1", "value1", "key2", "value2");

        CircuitBreakerRegistry circuitBreakerRegistry = CircuitBreakerRegistry.custom()
                .withCircuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
                .addRegistryEventConsumer(new RegistryEventConsumer() {
                    @Override
                    public void onEntryAddedEvent(EntryAddedEvent entryAddedEvent) {
                        // implementation
                    }

                    @Override
                    public void onEntryRemovedEvent(EntryRemovedEvent entryRemoveEvent) {
                        // implementation
                    }

                    @Override
                    public void onEntryReplacedEvent(EntryReplacedEvent entryReplacedEvent) {
                        // implementation
                    }
                }).withTags(circuitBreakerTags).build();

        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("testName");
    }

    public void invoke() {
        // Given
        CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults("testName");

        // When I decorate my function
        CheckedFunction0<String> decoratedSupplier = CircuitBreaker.decorateCheckedSupplier(
                circuitBreaker, () -> "This can be any method which returns: 'Hello");

        // and chain an other function with map
        Try<String> result = Try.of(decoratedSupplier).map(value -> value + " world'");

        // Then the Try Monad returns a Success<String>, if all functions ran successfully.
        System.out.println(result.isSuccess());
        System.out.println(result.get());
    }

    public void registryEvents() {
        CircuitBreakerRegistry circuitBreakerRegistry = CircuitBreakerRegistry.ofDefaults();
        circuitBreakerRegistry.getEventPublisher().onEntryAdded(entryAddedEvent -> {
            CircuitBreaker addedCircuitBreaker = entryAddedEvent.getAddedEntry();
            println("CircuitBreaker " + addedCircuitBreaker.getName() + " added");
        }).onEntryRemoved(entryRemovedEvent -> {
            CircuitBreaker removedCircuitBreaker = entryRemovedEvent.getRemovedEntry();
            println("CircuitBreaker " + removedCircuitBreaker.getName() + " removed");
        });
    }

    public void execute() {
        CircuitBreakerConfig circuitBreakerConfig =
                CircuitBreakerConfig.custom().failureRateThreshold(50).slowCallRateThreshold(50)
                        .waitDurationInOpenState(Duration.ofMillis(1000))
                        .slowCallDurationThreshold(Duration.ofSeconds(2))
                        .permittedNumberOfCallsInHalfOpenState(3).minimumNumberOfCalls(10)
                        .slidingWindowType(SlidingWindowType.TIME_BASED).slidingWindowSize(5)
                        // .recordException(e -> INTERNAL_SERVER_ERROR.equals(getResponse().getStatus()))
                        .recordExceptions(IOException.class, TimeoutException.class)
                        // .ignoreExceptions(BusinessException.class, OtherBusinessException.class)
                        .build();
        CircuitBreakerRegistry circuitBreakerRegistry =
                CircuitBreakerRegistry.of(circuitBreakerConfig);
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("name");

        String result = circuitBreaker.executeSupplier(() -> {
            if (Math.random() > 0.5) {
                throw new RuntimeException("Exception!");
            }
            return "Hold on, man";
        });
        System.out.println(result);
    }

    public void decorate() {
        CircuitBreakerConfig circuitBreakerConfig =
                CircuitBreakerConfig.custom().failureRateThreshold(50).slowCallRateThreshold(50)
                        .waitDurationInOpenState(Duration.ofMillis(1000))
                        .slowCallDurationThreshold(Duration.ofSeconds(2))
                        .permittedNumberOfCallsInHalfOpenState(3).minimumNumberOfCalls(10)
                        .slidingWindowType(SlidingWindowType.TIME_BASED).slidingWindowSize(5)
                        // .recordException(e -> INTERNAL_SERVER_ERROR.equals(getResponse().getStatus()))
                        .recordExceptions(IOException.class, TimeoutException.class)
                        // .ignoreExceptions(BusinessException.class, OtherBusinessException.class)
                        .build();
        CircuitBreakerRegistry circuitBreakerRegistry =
                CircuitBreakerRegistry.of(circuitBreakerConfig);
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("name");

        Supplier<String> decoratedSupplier = CircuitBreaker.decorateSupplier(circuitBreaker, () -> {
            if (Math.random() > 0.5) {
                throw new RuntimeException("Exception!");
            }
            return "Hold on, man";
        });

        String result =
                Try.ofSupplier(decoratedSupplier).recover(throwable -> "Hello from Recovery").get();
        System.out.println(result);
    }

    public void recovery() {
        // Given
        CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults("testName");

        // When I decorate my function and invoke the decorated function
        CheckedFunction0<String> checkedSupplier =
                CircuitBreaker.decorateCheckedSupplier(circuitBreaker, () -> {
                    throw new RuntimeException("BAM!");
                });
        Try<String> result = Try.of(checkedSupplier).recover(throwable -> "Hello Recovery");
        System.out.println(result);
    }

    public void recoveryBeforeRecord() {
        CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults("testName");
        Supplier<String> supplier = () -> {
            throw new RuntimeException("BAM!");
        };

        Supplier<String> supplierWithRecovery =
                SupplierUtils.recover(supplier, (exception) -> "Hello Recovery");

        String result = circuitBreaker.executeSupplier(supplierWithRecovery);
        System.out.println(result);
    }

    public void exceptHandling() {
        // Supplier<String> supplier = () -> {
        //     throw new RuntimeException("BAM!");
        // };
        // Supplier<String> supplierWithResultAndExceptionHandler =
        //         SupplierUtils.andThen(supplier, (result, exception) -> "Hello Recovery");
        CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults("testName");

        Supplier<HttpResponse> supplier = () -> null;
        Supplier<HttpResponse> supplierWithResultHandling =
                SupplierUtils.andThen(supplier, result -> {
                    if (result.statusCode() == 400) {
                        throw new RuntimeException("ClientException");
                    } else if (result.statusCode() == 500) {
                        throw new RuntimeException("ServerException");
                    }
                    return result;
                });
        HttpResponse httpResponse = circuitBreaker.executeSupplier(supplierWithResultHandling);
    }

    public void transition() {
        CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults("testName");
        circuitBreaker.transitionToDisabledState();
        // circuitBreaker.onFailure(...) won't trigger a state change
        circuitBreaker.transitionToClosedState(); // will transition to CLOSED state and re-enable normal behaviour, keeping metrics
        circuitBreaker.transitionToForcedOpenState();
        // circuitBreaker.onSuccess(...) won't trigger a state change
        circuitBreaker.reset(); //  will transition to CLOSED state and re-enable normal behaviour, losing metrics
    }

    public void registryStore() {
        CircuitBreakerRegistry circuitBreakerRegistry = CircuitBreakerRegistry.custom()
                .withRegistryStore(new CacheCircuitBreakerRegistryStore()).build();
    }

    public static void main(String[] args) {
        new CircuitBreakerDemo().recoveryBeforeRecord();
    }
}

class CacheCircuitBreakerRegistryStore implements RegistryStore<CircuitBreaker>{

    @Override
    public CircuitBreaker computeIfAbsent(String key,
            Function<? super String, ? extends CircuitBreaker> mappingFunction) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CircuitBreaker putIfAbsent(String key, CircuitBreaker value) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<CircuitBreaker> find(String key) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<CircuitBreaker> remove(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<CircuitBreaker> replace(String name, CircuitBreaker newEntry) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Collection<CircuitBreaker> values() {
        // TODO Auto-generated method stub
        return null;
    }
    
}