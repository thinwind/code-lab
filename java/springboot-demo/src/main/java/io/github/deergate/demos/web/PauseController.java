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
package io.github.deergate.demos.web;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

/**
 *
 * TODO 说明
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2022-11-03  16:28
 *
 */
@RestController
public class PauseController {
    
    @Value("${demo.pool.size:4}")
    int poolSize;
    
    ExecutorService service;
    
    @PostConstruct
    public void init(){
        service = Executors.newFixedThreadPool(poolSize);
    }
    
    @GetMapping("/post-async")
    public  DeferredResult<Object> asynchPostTest(@RequestParam(required = false,defaultValue = "20") int pause) {
        DeferredResult<Object> dr = new DeferredResult<>(4000L);
        service.execute(()->{
            try {
                Thread.sleep(pause);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Map<String,Object> result = new HashMap<>();
            result.put("result", "ok");
            dr.setResult(result);
        });
        return dr;
    }
}
