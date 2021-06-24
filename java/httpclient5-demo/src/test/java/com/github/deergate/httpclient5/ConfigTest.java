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

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.DefaultHttpRequestRetryStrategy;
import org.apache.hc.client5.http.impl.DefaultRedirectStrategy;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.client5.http.protocol.RedirectLocations;
import org.apache.hc.client5.http.utils.URIUtils;
import org.apache.hc.core5.http.HttpHost;
import org.junit.Test;

/**
 *
 * TODO ConfigTest说明
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2021-06-24  11:53
 *
 */
public class ConfigTest {

    @Test
    public void testRetry() {
        // HttpRequestRetryHandler myRetryHandler = new HttpRequestRetryHandler() {

        //     public boolean retryRequest(
        //             IOException exception,
        //             int executionCount,
        //             HttpContext context) {
        //         if (executionCount >= 5) {
        //             // Do not retry if over max retry count
        //             return false;
        //         }
        //         if (exception instanceof InterruptedIOException) {
        //             // Timeout
        //             return false;
        //         }
        //         if (exception instanceof UnknownHostException) {
        //             // Unknown host
        //             return false;
        //         }
        //         if (exception instanceof ConnectTimeoutException) {
        //             // Connection refused
        //             return false;
        //         }
        //         if (exception instanceof SSLException) {
        //             // SSL handshake exception
        //             return false;
        //         }
        //         HttpClientContext clientContext = HttpClientContext.adapt(context);
        //         HttpRequest request = clientContext.getRequest();
        //         boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
        //         if (idempotent) {
        //             // Retry if the request is considered idempotent
        //             return true;
        //         }
        //         return false;
        //     }

        // };
        CloseableHttpClient httpclient = HttpClients.custom()
                .setRetryStrategy(new DefaultHttpRequestRetryStrategy()).build();
    }

    public void testRedirect() throws URISyntaxException, IOException {
        DefaultRedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
        CloseableHttpClient httpclient =
                HttpClients.custom().setRedirectStrategy(redirectStrategy).build();
        // CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpClientContext context = HttpClientContext.create();
        HttpGet httpget = new HttpGet("http://localhost:8080/");
        CloseableHttpResponse response = httpclient.execute(httpget, context);
        try {
            HttpHost target = context.getHttpRoute().getTargetHost();
            RedirectLocations redirectLocations = context.getRedirectLocations();
            URI location = URIUtils.resolve(httpget.getUri(), target, redirectLocations.getAll());
            System.out.println("Final HTTP location: " + location.toASCIIString());
            // Expected to be an absolute URI
        } finally {
            response.close();
        }
    }
}
