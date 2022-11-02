/* 
 * Copyright 2022 Shang Yehua <niceshang@outlook.com>
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

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;

import org.apache.hc.client5.http.async.methods.AbstractCharResponseConsumer;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.EntityDetails;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.nio.AsyncResponseConsumer;
import org.apache.hc.core5.http.nio.entity.AbstractCharDataConsumer;
import org.apache.hc.core5.http.protocol.HttpContext;

/**
 *
 * TODO 说明
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2022-10-21  16:15
 *
 */
public class ConsumerManager {

    final static int POOL_SIZE = 256;

    final static StringConsumer[] CONSUMERS = new StringConsumer[POOL_SIZE];

    final static AtomicLong CURSOR = new AtomicLong(0);

    static {
        for (int i = 0; i < CONSUMERS.length; i++) {
            CONSUMERS[i] = new StringConsumer();
        }
    }

    public static StringConsumer getConsumer() {
        // System.out.println("finding consumer...");
        for (int i = 0; i < POOL_SIZE; i++) {
            StringConsumer consumer = CONSUMERS[(int) (CURSOR.getAndIncrement() % POOL_SIZE)];
            if (consumer.tryUse()) {
                // System.out.println("found one: " + consumer);
                return consumer;
            }
        }

        StringConsumer consumer = new StringConsumer();
        // System.out.println("create new one: " + consumer);
        return consumer;
    }

    static class StringConsumer extends AbstractCharDataConsumer implements AsyncResponseConsumer<String> {

        final static AtomicLong SEQ = new AtomicLong(0);

        private volatile FutureCallback<String> resultCallback;

        private final AtomicBoolean using = new AtomicBoolean(false);

        private final StringBuilder builder = new StringBuilder(2048);

        private final long id = SEQ.getAndIncrement();

        public boolean tryUse() {
            return using.compareAndSet(false, true);
        }

        @Override
        public void releaseResources() {
            resultCallback = null;
            builder.setLength(0);
            using.set(false);
            // System.out.println("[==Consumer Release Resources==] " + this);
        }

        @Override
        public void consumeResponse(HttpResponse response, EntityDetails entityDetails, HttpContext context,
                FutureCallback<String> resultCallback) throws HttpException, IOException {
            this.resultCallback = resultCallback;
            if (entityDetails != null) {
                final ContentType contentType;
                try {
                    contentType = ContentType.parse(entityDetails.getContentType());
                } catch (final UnsupportedCharsetException ex) {
                    throw new UnsupportedEncodingException(ex.getMessage());
                }
                Charset charset = contentType != null ? contentType.getCharset() : null;
                if (charset == null) {
                    charset = StandardCharsets.UTF_8;
                }
                setCharset(charset);
            } else {
                completed();
            }
        }

        @Override
        public void informationResponse(HttpResponse response, HttpContext context) throws HttpException, IOException {
            //just ignore
        }

        @Override
        public void failed(Exception cause) {
            //ignore for now
        }

        @Override
        protected int capacityIncrement() {
            return Integer.MAX_VALUE;
        }

        @Override
        protected void data(CharBuffer src, boolean endOfStream) throws IOException {
            if (src.hasArray()) {
                builder.append(src.array(), src.arrayOffset() + src.position(), src.remaining());
            } else {
                while (src.hasRemaining()) {
                    builder.append(src.get());
                }
            }
        }

        @Override
        protected void completed() throws IOException {
            this.resultCallback.completed(builder.toString());
        }

        @Override
        public String toString() {
            return String.format("StringConsumer [using=%s, id=%s]", using, id);
        }

    }

    public static void printAllConsumers() {
        int usingCnt = 0;
        for (StringConsumer stringConsumer : CONSUMERS) {
            if (stringConsumer.using.get()) {
                usingCnt++;
            }
            System.out.println(stringConsumer);
        }
        System.out.println(usingCnt + " consumers are still using.");

    }
}
