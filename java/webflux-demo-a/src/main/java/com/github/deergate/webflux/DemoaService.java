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
package com.github.deergate.webflux;

import java.util.Random;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

/**
 *
 * TODO DemoaService说明
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2021-07-01  23:15
 *
 */
@Service
public class DemoaService {
    
    final WebClient webClient;

    public DemoaService(WebClient webClient) {
        this.webClient = webClient;
    }
    
    public Mono<User> getUser(ServerRequest request){
        Random random=new Random();
        return webClient.get().uri("/user/"+random.nextInt(9999)+1)
        .retrieve().bodyToMono(User.class).map(u->{
            u.setTag("Demo A");
            return u;
        });
    }
}
