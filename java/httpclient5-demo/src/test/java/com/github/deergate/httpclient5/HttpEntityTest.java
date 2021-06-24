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

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import org.apache.hc.client5.http.ClientProtocolException;
import org.apache.hc.client5.http.ConnectionKeepAliveStrategy;
import org.apache.hc.client5.http.HttpResponseException;
import org.apache.hc.client5.http.RouteInfo;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.DefaultConnectionKeepAliveStrategy;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.FileEntity;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.message.BasicClassicHttpResponse;
import org.apache.hc.core5.http.message.BasicHeader;
import org.apache.hc.core5.http.protocol.BasicHttpContext;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.apache.hc.core5.util.TimeValue;
import org.junit.Test;

/**
 *
 * TODO HttpEntityTest说明
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2021-06-23  19:39
 *
 */
public class HttpEntityTest {

    @Test
    public void test1() {
        File file = new File("somefile.txt");
        FileEntity entity = new FileEntity(file, ContentType.create("text/plain", "UTF-8"));

        HttpPost httppost = new HttpPost("http://localhost/action.do");
        httppost.setEntity(entity);
    }

    public void test2() throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet("http://localhost/json");

        HttpClientResponseHandler<String> rh = new HttpClientResponseHandler<>() {

            @Override
            public String handleResponse(final ClassicHttpResponse response) throws IOException {
                int statusCode = response.getCode();
                HttpEntity entity = response.getEntity();
                if (statusCode >= 300) {
                    throw new HttpResponseException(statusCode, response.getReasonPhrase());
                }
                if (entity == null) {
                    throw new ClientProtocolException("Response contains no content");
                }
                Header contentTypeHeader = response.getFirstHeader("Content-Type");
                ContentType contentType = ContentType.parse(contentTypeHeader.getValue());
                Charset charset = contentType.getCharset();
                try {
                    return EntityUtils.toString(entity, charset);
                } catch (ParseException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
        httpclient.execute(httpget, rh);
        // MyJsonObject myjson = client.execute(httpget, rh);
    }

    @Test
    public void testContentType() {
        BasicClassicHttpResponse response = new BasicClassicHttpResponse(HttpStatus.SC_OK, "OK");
        response.addHeader("Set-Cookie", "c1=a; path=/; domain=localhost");
        response.addHeader("Set-Cookie", "c2=b; path=\"/\", c3=c; domain=\"localhost\"");
        StringEntity myEntity = new StringEntity("important message 加上中文",
                ContentType.create("text/plain", "UTF-8"));
        response.setEntity(myEntity);
        response.setHeader(new BasicHeader("Content-Type", "text/plain;charset=UTF-8"));

        Header contentTypeHeader = response.getFirstHeader("Content-Type");
        ContentType contentType = ContentType.parse(contentTypeHeader.getValue());
        System.out.println(contentTypeHeader.getValue() + "\t" + contentTypeHeader.toString());
        System.out.println(contentType);
        System.out.println(contentType.getMimeType());
        System.out.println(contentType.getCharset());
    }

    public void test4() {
        ConnectionKeepAliveStrategy keepAliveStrat = new DefaultConnectionKeepAliveStrategy() {

            @Override
            public TimeValue getKeepAliveDuration(HttpResponse response, HttpContext context) {
                TimeValue keepAlive = super.getKeepAliveDuration(response, context);

                return keepAlive;
            }

        };
        //TODO RequestConfig
        CloseableHttpClient httpclient =
                HttpClients.custom().setKeepAliveStrategy(keepAliveStrat).build();
    }

    public void testContext() throws IOException {
        HttpContext context = new BasicHttpContext();
        HttpClientContext clientContext = HttpClientContext.adapt(context);
        // HttpHost target = clientContext.getTargetHost();
        RouteInfo httpRoute = clientContext.getHttpRoute();
        HttpHost target = httpRoute.getTargetHost();
        HttpRequest request = clientContext.getRequest();
        HttpResponse response = clientContext.getResponse();
        RequestConfig config = clientContext.getRequestConfig();
        CloseableHttpClient httpclient = HttpClients.createDefault();
        RequestConfig requestConfig =
                RequestConfig.custom().setConnectTimeout(1000, TimeUnit.MICROSECONDS)
                .setDefaultKeepAlive(10, TimeUnit.SECONDS)
                .build();
                
                
        httpclient = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();

        HttpGet httpget1 = new HttpGet("http://localhost/1");
        httpget1.setConfig(requestConfig);
        CloseableHttpResponse response1 = httpclient.execute(httpget1, context);
        try {
            HttpEntity entity1 = response1.getEntity();
        } finally {
            response1.close();
        }
        HttpGet httpget2 = new HttpGet("http://localhost/2");
        CloseableHttpResponse response2 = httpclient.execute(httpget2, context);
        try {
            HttpEntity entity2 = response2.getEntity();
        } finally {
            response2.close();
        }
    }

    public void testInterceptor() throws IOException {
        CloseableHttpClient httpclient =
                HttpClients.custom().addRequestInterceptorLast(new HttpRequestInterceptor() {

                    @Override
                    public void process(HttpRequest request, EntityDetails entity,
                            HttpContext context) throws HttpException, IOException {
                        AtomicInteger count = (AtomicInteger) context.getAttribute("count");
                        request.addHeader("Count", Integer.toString(count.getAndIncrement()));
                    }

                }).build();

        AtomicInteger count = new AtomicInteger(1);
        HttpClientContext localContext = HttpClientContext.create();
        localContext.setAttribute("count", count);

        HttpGet httpget = new HttpGet("http://localhost/");
        for (int i = 0; i < 10; i++) {
            CloseableHttpResponse response = httpclient.execute(httpget, localContext);
            try {
                HttpEntity entity = response.getEntity();
            } finally {
                response.close();
            }
        }
    }
}
