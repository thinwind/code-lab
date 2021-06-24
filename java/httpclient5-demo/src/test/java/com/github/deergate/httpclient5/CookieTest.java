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
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.cookie.Cookie;
import org.apache.hc.client5.http.cookie.CookieOrigin;
import org.apache.hc.client5.http.cookie.CookieSpec;
import org.apache.hc.client5.http.cookie.CookieSpecFactory;
import org.apache.hc.client5.http.cookie.CookieStore;
import org.apache.hc.client5.http.cookie.StandardCookieSpec;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.cookie.BasicClientCookie;
import org.apache.hc.client5.http.impl.cookie.IgnoreCookieSpecFactory;
import org.apache.hc.client5.http.impl.cookie.RFC6265CookieSpecFactory;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.client5.http.psl.PublicSuffixMatcher;
import org.apache.hc.client5.http.psl.PublicSuffixMatcherLoader;
import org.apache.hc.core5.http.config.Lookup;
import org.apache.hc.core5.http.config.Registry;
import org.apache.hc.core5.http.config.RegistryBuilder;

/**
 *
 * TODO CookieTest说明
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2021-06-24  14:43
 *
 */
public class CookieTest {

    public void test1() {
        BasicClientCookie cookie = new BasicClientCookie("name", "value");
        // Set effective domain and path attributes
        cookie.setDomain(".mycompany.com");
        cookie.setPath("/");
        // Set attributes exactly as sent by the server
        cookie.setAttribute(Cookie.PATH_ATTR, "/");
        cookie.setAttribute(Cookie.DOMAIN_ATTR, ".mycompany.com");
    }

    public void test2() {
        RequestConfig globalConfig =
                RequestConfig.custom().setCookieSpec(StandardCookieSpec.RELAXED).build();
        CloseableHttpClient httpclient =
                HttpClients.custom().setDefaultRequestConfig(globalConfig).build();
        RequestConfig localConfig =
                RequestConfig.copy(globalConfig).setCookieSpec(StandardCookieSpec.IGNORE).build();
        HttpGet httpGet = new HttpGet("/");
        httpGet.setConfig(localConfig);
    }

    public void test3() {
        PublicSuffixMatcher publicSuffixMatcher = PublicSuffixMatcherLoader.getDefault();

        Registry<CookieSpecFactory> r = RegistryBuilder.<CookieSpecFactory>create()
                .register(StandardCookieSpec.RELAXED,
                        new RFC6265CookieSpecFactory(publicSuffixMatcher))
                .register(StandardCookieSpec.IGNORE, new IgnoreCookieSpecFactory()).build();

        RequestConfig requestConfig =
                RequestConfig.custom().setCookieSpec(StandardCookieSpec.IGNORE).build();

        CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieSpecRegistry(r)
                .setDefaultRequestConfig(requestConfig).build();
    }

    public void test4() {
        // Create a local instance of cookie store
        CookieStore cookieStore = new BasicCookieStore();
        // Populate cookies if needed
        BasicClientCookie cookie = new BasicClientCookie("name", "value");
        cookie.setDomain(".mycompany.com");
        cookie.setPath("/");
        cookieStore.addCookie(cookie);
        // Set the store
        CloseableHttpClient httpclient =
                HttpClients.custom().setDefaultCookieStore(cookieStore).build();
    }

    public void test5() throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();

        Lookup<CookieSpecFactory> cookieSpecReg = RegistryBuilder.<CookieSpecFactory>create()
                .register(StandardCookieSpec.RELAXED, new RFC6265CookieSpecFactory())
                .register(StandardCookieSpec.IGNORE, new IgnoreCookieSpecFactory()).build();
        CookieStore cookieStore = new BasicCookieStore();

        HttpClientContext context = HttpClientContext.create();
        context.setCookieSpecRegistry(cookieSpecReg);
        context.setCookieStore(cookieStore);
        HttpGet httpget = new HttpGet("http://somehost/");
        CloseableHttpResponse response1 = httpclient.execute(httpget, context);
        // Cookie origin details
        CookieOrigin cookieOrigin = context.getCookieOrigin();
        // Cookie spec used
        CookieSpec cookieSpec = context.getCookieSpec();
    }
}
