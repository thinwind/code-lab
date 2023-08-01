/* 
 * Copyright 2023 Shang Yehua <niceshang@outlook.com>
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
package io.github.deergate.demos.badprac;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * TODO 说明
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2023-08-01  15:05
 *
 */
@RestController
public class InjectedRequestController {

    @Autowired
    private HttpServletRequest request;

    private long stop = 10000;

    private AtomicBoolean switcher = new AtomicBoolean(true);

    @GetMapping("/echo-param")
    public Object echo() {
        new Thread(() -> {
            System.out.println(request.getParameter("param"));
        }).start();

        return request.getParameter("param");
    }

    @GetMapping("/echo-param2")
    public Object echo2(HttpServletRequest request2) {
        new Thread(() -> {
            System.out.println(request2.getParameter("param"));
        }).start();

        return request2.getParameter("param");
    }
}
