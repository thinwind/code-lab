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
package com.github.deergate.httpclient5;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;

import org.apache.hc.client5.http.async.methods.AbstractBinResponseConsumer;
import org.apache.hc.client5.http.async.methods.AbstractCharResponseConsumer;
import org.apache.hc.client5.http.async.methods.SimpleHttpRequest;
import org.apache.hc.client5.http.async.methods.SimpleRequestProducer;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.DefaultHttpRequestRetryStrategy;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManager;
import org.apache.hc.client5.http.ssl.DefaultClientTlsStrategy;
import org.apache.hc.core5.concurrent.DefaultThreadFactory;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.config.CharCodingConfig;
import org.apache.hc.core5.http.config.Http1Config;
import org.apache.hc.core5.http.config.Registry;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.http.nio.AsyncResponseConsumer;
import org.apache.hc.core5.http.nio.ssl.TlsStrategy;
import org.apache.hc.core5.pool.PoolConcurrencyPolicy;
import org.apache.hc.core5.pool.PoolReusePolicy;
import org.apache.hc.core5.reactor.IOReactorConfig;
import org.apache.hc.core5.util.ByteArrayBuffer;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;

/**
 *
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2021-06-24  15:24
 *
 */
public class ProdPostUtil {

    public static final CloseableHttpAsyncClient httpClient;

    private final static String path = "/echo";

    public static final int RESPONSE_INIT_CAP = 1024;

    static {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(Timeout.ofMilliseconds(100))
                .setConnectTimeout(60, TimeUnit.MILLISECONDS)
                .setResponseTimeout(200, TimeUnit.MILLISECONDS)
                .setRedirectsEnabled(false)
                .setAuthenticationEnabled(false)
                .build();

        final IOReactorConfig ioReactorConfig = IOReactorConfig.custom()
                .setSoTimeout(Timeout.ofMilliseconds(200))
                .setSoKeepAlive(true)
                .setTcpNoDelay(false)
                .setIoThreadCount(16)
                .build();

        Registry<TlsStrategy> registry = RegistryBuilder.<TlsStrategy>create()
                .register("https", DefaultClientTlsStrategy.getDefault())
                .build();

        PoolingAsyncClientConnectionManager cm = new PoolingAsyncClientConnectionManager(registry,
                PoolConcurrencyPolicy.LAX,
                PoolReusePolicy.FIFO, TimeValue.NEG_ONE_SECOND);
        cm.setMaxTotal(200);
        cm.setDefaultMaxPerRoute(200);

        final Http1Config h1Config = Http1Config.custom()
                .setMaxHeaderCount(199)
                .setBufferSize(2048)
                .setInitialWindowSize(4096)
                .build();

        // Create connection configuration
        final CharCodingConfig charCodingConfig = CharCodingConfig.custom()
                .setMalformedInputAction(CodingErrorAction.IGNORE)
                .setUnmappableInputAction(CodingErrorAction.IGNORE)
                .setCharset(StandardCharsets.UTF_8).build();

        httpClient = HttpAsyncClients.custom()
                .disableConnectionState()
                .disableCookieManagement()
                .disableAuthCaching()
                .disableRedirectHandling()
                .setDefaultRequestConfig(requestConfig)
                .setIOReactorConfig(ioReactorConfig)
                .setRetryStrategy(
                        new DefaultHttpRequestRetryStrategy(0, TimeValue.ofMilliseconds(500)))
                .setConnectionManager(cm)
                .setHttp1Config(h1Config)
                .setThreadFactory(new DefaultThreadFactory("HttpClient5-Async-Thread", false))
                .setCharCodingConfig(charCodingConfig)
                .setConnectionManagerShared(false)
                .build();
        httpClient.start();
    }

    public static byte[] post(byte[] input, String url, int port)
            throws InterruptedException, ExecutionException {
        HttpHost host = new HttpHost(url, port);
        SimpleHttpRequest simpleHttpRequest = new SimpleHttpRequest(HttpPost.METHOD_NAME, host, path);
        simpleHttpRequest.setBody(input, ContentType.APPLICATION_OCTET_STREAM);
        Future<byte[]> future = httpClient.execute(SimpleRequestProducer.create(simpleHttpRequest),
                responseConsumer(), null);
        return future.get();
    }

    public static String post(String body, String url, int port)
            throws InterruptedException, ExecutionException {
        HttpHost host = new HttpHost(url, port);
        SimpleHttpRequest simpleHttpRequest = new SimpleHttpRequest(HttpPost.METHOD_NAME, host, path);
        simpleHttpRequest.setBody(body, ContentType.create("text/plain", "UTF-8"));
        Future<String> future = httpClient.execute(SimpleRequestProducer.create(simpleHttpRequest),
        ConsumerManager.getConsumer(), null);
        return future.get();
    }

    static AsyncResponseConsumer<String> responseConsumer2() {
        return new AbstractCharResponseConsumer<String>() {

            private final StringBuilder buffer = new StringBuilder();

            @Override
            public void releaseResources() {
                buffer.setLength(0);
            }

            @Override
            protected void start(HttpResponse response, ContentType contentType)
                    throws HttpException, IOException {

            }

            @Override
            protected String buildResult() throws IOException {
                return buffer.toString();
            }

            @Override
            protected int capacityIncrement() {
                return Integer.MAX_VALUE;
            }

            @Override
            protected void data(CharBuffer src, boolean endOfStream) throws IOException {
                buffer.append(src);
            }

        };
    }

    static AsyncResponseConsumer<byte[]> responseConsumer() {
        return new AbstractBinResponseConsumer<byte[]>() {
            private final ByteArrayBuffer buffer = new ByteArrayBuffer(RESPONSE_INIT_CAP);

            @Override
            public void releaseResources() {
                buffer.clear();
            }

            @Override
            protected void start(HttpResponse response, ContentType contentType)
                    throws HttpException, IOException {
                //just do nothing
            }

            @Override
            protected byte[] buildResult() {
                return buffer.toByteArray();
            }

            @Override
            protected int capacityIncrement() {
                return Integer.MAX_VALUE;
            }

            @Override
            protected void data(ByteBuffer src, boolean endOfStream) throws IOException {
                if (src == null) {
                    return;
                }
                if (src.hasArray()) {
                    buffer.append(src.array(), src.arrayOffset() + src.position(), src.remaining());
                } else {
                    while (src.hasRemaining()) {
                        buffer.append(src.get());
                    }
                }
            }

        };
    }

}
