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

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 *
 * TODO DemoaController说明
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2021-07-01  23:15
 *
 */
@Configuration
public class DemoIniter {
    
    @Bean
    public RouterFunction<ServerResponse> userRoute(DemoaService aService){
        return RouterFunctions.route(RequestPredicates.GET("/user"),
         request->ServerResponse.ok()
             .body(aService.getUser(request), User.class)
        );
    }
}
