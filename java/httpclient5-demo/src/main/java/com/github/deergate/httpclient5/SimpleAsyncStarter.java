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
package com.github.deergate.httpclient5;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.io.IOException;
import java.nio.CharBuffer;
import org.apache.hc.client5.http.async.methods.AbstractCharResponseConsumer;
import org.apache.hc.client5.http.async.methods.SimpleHttpRequest;
import org.apache.hc.client5.http.async.methods.SimpleHttpRequests;
import org.apache.hc.client5.http.async.methods.SimpleHttpResponse;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.nio.AsyncRequestProducer;
import org.apache.hc.core5.http.nio.support.AsyncRequestBuilder;

/**
 *
 * TODO SimpleAsyncStarter说明
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2021-06-23  17:08
 *
 */
public class SimpleAsyncStarter {
    public static void main(String[] args) throws InterruptedException, ExecutionException,IOException {
        try (CloseableHttpAsyncClient httpclient = HttpAsyncClients.createDefault()) {
            // Start the client
            httpclient.start();
        
            // Execute request
            SimpleHttpRequest request1 = SimpleHttpRequests.get("http://httpbin.org/get");
            Future<SimpleHttpResponse> future = httpclient.execute(request1, null);
            // and wait until response is received
            SimpleHttpResponse response1 = future.get();
            System.out.println(request1.getRequestUri() + "->" + response1.getCode());
        
            // One most likely would want to use a callback for operation result
            CountDownLatch latch1 = new CountDownLatch(1);
            SimpleHttpRequest request2 = SimpleHttpRequests.get("http://httpbin.org/get");
            httpclient.execute(request2, new FutureCallback<SimpleHttpResponse>() {
        
                @Override
                public void completed(SimpleHttpResponse response2) {
                    latch1.countDown();
                    System.out.println(request2.getRequestUri() + "->" + response2.getCode());
                }
        
                @Override
                public void failed(Exception ex) {
                    latch1.countDown();
                    System.out.println(request2.getRequestUri() + "->" + ex);
                }
        
                @Override
                public void cancelled() {
                    latch1.countDown();
                    System.out.println(request2.getRequestUri() + " cancelled");
                }
        
            });
            latch1.await();
        
            // In real world one most likely would want also want to stream
            // request and response body content
            CountDownLatch latch2 = new CountDownLatch(1);
            AsyncRequestProducer producer3 = AsyncRequestBuilder.get("http://httpbin.org/get").build();
            AbstractCharResponseConsumer<HttpResponse> consumer3 = new AbstractCharResponseConsumer<HttpResponse>() {
        
                HttpResponse response;
        
                @Override
                protected void start(HttpResponse response, ContentType contentType) throws HttpException, IOException {
                    this.response = response;
                }
        
                @Override
                protected int capacityIncrement() {
                    return Integer.MAX_VALUE;
                }
        
                @Override
                protected void data(CharBuffer data, boolean endOfStream) throws IOException {
                    // Do something useful
                }
        
                @Override
                protected HttpResponse buildResult() throws IOException {
                    return response;
                }
        
                @Override
                public void releaseResources() {
                }
        
            };
            httpclient.execute(producer3, consumer3, new FutureCallback<HttpResponse>() {
        
                @Override
                public void completed(HttpResponse response3) {
                    latch2.countDown();
                    System.out.println(request2.getRequestUri() + "->" + response3.getCode());
                }
        
                @Override
                public void failed(Exception ex) {
                    latch2.countDown();
                    System.out.println(request2.getRequestUri() + "->" + ex);
                }
        
                @Override
                public void cancelled() {
                    latch2.countDown();
                    System.out.println(request2.getRequestUri() + " cancelled");
                }
        
            });
            latch2.await();
        
        }
    }
}
