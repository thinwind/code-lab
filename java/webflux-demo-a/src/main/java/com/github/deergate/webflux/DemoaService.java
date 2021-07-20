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

    public Mono<User> getUser(ServerRequest request) {
        Random random = new Random();
        return webClient.get().uri("/user/" + random.nextInt(9999) + 1).retrieve()
                .bodyToMono(User.class).elapsed().map(t -> {
                    System.out.println(t.getT1());
                    return t.getT2();
                }).map(u -> {
                    u.setTag("Demo A");
                    return u;
                }).flatMap(u->{
                    u.setId(-999);
                    return Mono.just(u);
                })
                // .doOnError(e->{
                //     System.out.println("Do 1");
                //     e.printStackTrace();
                // })               
                .onErrorResume(e -> {
                    System.out.println("ERROR1!!!");
                    User u=new User();
                    u.setId(-1);
                    return Mono.just(u);
                })
                .onErrorResume(e -> {
                    System.out.println("ERROR2!!!");
                    User u=new User();
                    u.setId(-2);
                    return Mono.just(u);
                })
                .doOnError(e->{
                    System.out.println("Do 2");
                    e.printStackTrace();
                }) 
                .flatMap(u->{
                    u.setName("final");
                    return Mono.just(u);
                })
                ;
    }
}
