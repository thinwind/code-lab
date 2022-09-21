/*
 * Copyright 2022 Shang Yehua <niceshang@outlook.com>
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
package io.github.deergate.demos.web;

import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * TODO TracableService说明
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2022-07-12  22:21
 *
 */
@Service
public class TracableService {

    Random random = new Random();
    
    @Autowired
    SomeWorker someWorker;

    public void doSomething() {
        System.out.println("do something...");
        try {
            Thread.sleep(random.nextInt(2));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        doSomeOtherThing();
    }

    public void doSomeOtherThing() {
        System.out.println("do some other thing...");
        try {
            Thread.sleep(random.nextInt(10));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        someWorker.makeLeftworkDone();
    }

}
