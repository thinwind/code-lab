/*
 * Copyright 2022 Shang Yehua <niceshang@outlook.com>
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
package com.github.deergate.syncclient;

import java.util.concurrent.TimeUnit;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import org.apache.hc.client5.http.DnsResolver;
import org.apache.hc.client5.http.SystemDefaultDnsResolver;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.cookie.StandardCookieSpec;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.DefaultHttpResponseParserFactory;
import org.apache.hc.client5.http.impl.io.ManagedHttpClientConnectionFactory;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.io.ManagedHttpClientConnection;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.config.CharCodingConfig;
import org.apache.hc.core5.http.config.Http1Config;
import org.apache.hc.core5.http.config.Registry;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.http.impl.io.DefaultClassicHttpResponseFactory;
import org.apache.hc.core5.http.impl.io.DefaultHttpRequestWriterFactory;
import org.apache.hc.core5.http.impl.io.DefaultHttpResponseParser;
import org.apache.hc.core5.http.io.HttpConnectionFactory;
import org.apache.hc.core5.http.io.HttpMessageParser;
import org.apache.hc.core5.http.io.HttpMessageParserFactory;
import org.apache.hc.core5.http.io.HttpMessageWriterFactory;
import org.apache.hc.core5.http.io.SocketConfig;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.message.LazyLaxLineParser;
import org.apache.hc.core5.pool.PoolConcurrencyPolicy;
import org.apache.hc.core5.pool.PoolReusePolicy;
import org.apache.hc.core5.util.TimeValue;

/**
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2022-10-19  14:37
 *
 */
public class HttpUtil {
    static final CloseableHttpClient HTTP_CLIENT;

    static {
        // Create global request configuration
        final RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setAuthenticationEnabled(false).setCookieSpec(StandardCookieSpec.IGNORE)
                .setRedirectsEnabled(false).setExpectContinueEnabled(false)
                .setConnectionKeepAlive(TimeValue.ofSeconds(10))
                .setConnectTimeout(60, TimeUnit.MILLISECONDS)
                .setConnectionRequestTimeout(100, TimeUnit.MILLISECONDS)
                //TODO 0 表示一直等
                .setResponseTimeout(200, TimeUnit.MILLISECONDS).build();

        // Create socket configuration
        final SocketConfig socketConfig = SocketConfig.custom().setTcpNoDelay(true)
                .setSoKeepAlive(true).setSoTimeout(300, TimeUnit.MILLISECONDS).build();

        // Create connection configuration
        final CharCodingConfig connectionConfig =
                CharCodingConfig.custom().setMalformedInputAction(CodingErrorAction.IGNORE)
                        .setUnmappableInputAction(CodingErrorAction.IGNORE)
                        .setCharset(StandardCharsets.UTF_8).build();

        // Create HTTP/1.1 protocol configuration
        final Http1Config h1Config =
                Http1Config.custom().setMaxHeaderCount(200).setMaxLineLength(4000).build();

        // Use custom message parser / writer to customize the way HTTP
        // messages are parsed from and written out to the data stream.
        final HttpMessageParserFactory<ClassicHttpResponse> responseParserFactory =
                new DefaultHttpResponseParserFactory() {

                    @Override
                    public HttpMessageParser<ClassicHttpResponse> create(
                            final Http1Config h1Config) {
                        return new DefaultHttpResponseParser(LazyLaxLineParser.INSTANCE,
                                DefaultClassicHttpResponseFactory.INSTANCE, h1Config);
                    }

                };
        final HttpMessageWriterFactory<ClassicHttpRequest> requestWriterFactory =
                new DefaultHttpRequestWriterFactory();

        // Use a custom connection factory to customize the process of
        // initialization of outgoing HTTP connections. Beside standard connection
        // configuration parameters HTTP connection factory can define message
        // parser / writer routines to be employed by individual connections.
        final HttpConnectionFactory<ManagedHttpClientConnection> connFactory =
                new ManagedHttpClientConnectionFactory(h1Config, connectionConfig,
                        requestWriterFactory, responseParserFactory);


        // Client HTTP connection objects when fully initialized can be bound to
        // an arbitrary network socket. The process of network socket initialization,
        // its connection to a remote address and binding to a local one is controlled
        // by a connection socket factory.

        // Create a registry of custom connection socket factories for supported
        // protocol schemes.
        final Registry<ConnectionSocketFactory> socketFactoryRegistry =
                RegistryBuilder.<ConnectionSocketFactory>create()
                        .register("http", PlainConnectionSocketFactory.INSTANCE).build();

        //TODO 如果后续CDCS DNS异常，考虑自己定制处理规则
        final DnsResolver dnsResolver = SystemDefaultDnsResolver.INSTANCE;

        // Create a connection manager with custom configuration.
        final PoolingHttpClientConnectionManager connManager =
                new PoolingHttpClientConnectionManager(socketFactoryRegistry,
                        PoolConcurrencyPolicy.LAX, PoolReusePolicy.LIFO, TimeValue.ofSeconds(60),
                        null, dnsResolver, connFactory);


        // Configure the connection manager to use socket configuration either
        // by default or for a specific host.
        connManager.setDefaultSocketConfig(socketConfig);
        // Validate connections after 1 sec of inactivity
        connManager.setValidateAfterInactivity(TimeValue.ofSeconds(1));
        // Configure total max or per route limits for persistent connections
        // that can be kept in the pool or leased by the connection manager.
        connManager.setMaxTotal(200);
        connManager.setDefaultMaxPerRoute(50);

        HTTP_CLIENT = HttpClients.custom().setConnectionManager(connManager).disableAuthCaching()
                .disableAutomaticRetries().disableCookieManagement().disableDefaultUserAgent()
                .disableRedirectHandling().setDefaultRequestConfig(defaultRequestConfig).build();

    }

    public static String post(String uri, String body, long traceId)
            throws ParseException, IOException {
        final HttpPost post = new HttpPost(uri);
        post.addHeader("X-CFPS2-TraceId", traceId);
        post.setEntity(new StringEntity(body, ContentType.APPLICATION_JSON, "UTF-8", false));

        try (final CloseableHttpResponse response = HTTP_CLIENT.execute(post)) {
            final HttpEntity entity = response.getEntity();
            if (entity != null) {
                try {
                    return EntityUtils.toString(entity, StandardCharsets.UTF_8);
                } finally {
                    EntityUtils.consume(entity);
                }
            } else {
                return null;
            }
        }

    }

    public static String get(String url) throws IOException, ParseException {
        final HttpGet httpget = new HttpGet(url);

        try (final CloseableHttpResponse response = HTTP_CLIENT.execute(httpget)) {
            // If the response does not enclose an entity, there is no need
            // to bother about connection release
            final HttpEntity entity = response.getEntity();
            if (entity != null) {
                try {
                    return EntityUtils.toString(entity, StandardCharsets.UTF_8);
                } finally {
                    EntityUtils.consume(entity);
                }
            } else {
                return null;
            }
        }
    }


    static final Object GUARDIAN_OBJECT = new Object() {

        @Override
        protected void finalize() throws Throwable {
            HTTP_CLIENT.close();
        }

    };
}
