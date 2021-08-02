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
package com.github.deergate.webfluxb;

import com.github.deergate.netty.RestartableEchoServer;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * TODO Controller说明
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2021-08-02  15:23
 *
 */
@RestController
public class Controller {
    
    RestartableEchoServer echoServer=new RestartableEchoServer(1234);

    
    @PostMapping("/start-netty")
    public Object startNetty(){
        echoServer.start();
        return "OK";
    }
    
    @PostMapping("/stop-netty")
    public Object stopNetty(){
        echoServer.stop();
        return "OK";
    }
    
    @PostMapping("/restart-netty")
    public Object restartNetty(){
        echoServer.reBind();
        return "OK";
    }
}
