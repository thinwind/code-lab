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

import java.util.Iterator;
import java.io.IOException;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.message.BasicHttpResponse;

/**
 *
 * TODO ResponseTest说明
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2021-06-23  18:03
 *
 */
public class ResponseTest {
    public static void main(String[] args) throws Exception{
        // demo2();
        demo3();
    }

    static void demo1() {
        HttpResponse response = new BasicHttpResponse(HttpStatus.SC_OK, "OK");
        response.addHeader("Set-Cookie", "c1=a; path=/; domain=localhost");
        response.addHeader("Set-Cookie", "c2=b; path=\"/\", c3=c; domain=\"localhost\"");
        Header h1 = response.getFirstHeader("Set-Cookie");
        System.out.println(h1);
        Header h2 = response.getLastHeader("Set-Cookie");
        System.out.println(h2);
        Header[] hs = response.getHeaders("Set-Cookie");
        System.out.println(hs.length);
    }

    static void demo2() {
        HttpResponse response = new BasicHttpResponse(HttpStatus.SC_OK, "OK");
        response.addHeader("Set-Cookie", "c1=a; path=/; domain=localhost");
        response.addHeader("Set-Cookie", "c2=b; path=\"/\", c3=c; domain=\"localhost\"");

        Iterator<Header> it = response.headerIterator("Set-Cookie");

        while (it.hasNext()) {
            Header header = it.next();
            System.out.println(header.getName() + " ^=>^ " + header.getValue());
            // System.out.println(header);

        }
    }

    static void demo3() throws ParseException, IOException {
        StringEntity myEntity =
                new StringEntity("important message 加上中文", ContentType.create("text/plain", "UTF-8"));

        System.out.println(myEntity.getContentType());
        System.out.println(myEntity.getContentLength());
        System.out.println(EntityUtils.toString(myEntity));
        System.out.println(EntityUtils.toString(myEntity).length());
        System.out.println(EntityUtils.toByteArray(myEntity).length);
    }
}
