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

import java.util.Locale;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Component;

import io.github.deergate.BullShit;
import io.github.deergate.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * TODO 说明
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2022-10-20  14:52
 *
 */
@Slf4j
@Component
public class Runner {

    public void run(ObjectMapper mapper) throws IOException {
        Faker chineseFaker = Faker.instance(Locale.SIMPLIFIED_CHINESE);
        BullShit bullShit = new BullShit(mapper::readValue);
        Random random = new Random();
        int cnt=0;
        int total=3000;
        for (int i = 0; i < total; i++) {
            try {
                // long start = System.currentTimeMillis();
                ProdPostUtil.post(bullShit.generator(chineseFaker.address().streetName(), 
                    random.nextInt(300) + 200), 
                    "172.27.128.21", 8089);
                // long end = System.currentTimeMillis();
                // System.out.println(end-start);
            } catch (Exception e) {
                // e.printStackTrace();
                // log.error("请求失败", e);
                cnt++;
            }
        }
        System.out.println(Thread.currentThread().getName()+": Total : "+total+", failed:"+cnt+".");
    }
}
