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

import java.util.Random;
import org.springframework.stereotype.Service;

/**
 *
 * TODO SomeWorker说明
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2022-07-12  22:21
 *
 */
@Service
public class SomeWorker {

    Random random = new Random();
    
    public void makeLeftworkDone() {
        System.out.println("Start working...");
        readNews();
        drinkTea();
        goToWc();
        finish();
    }

    private void finish() {
        try {
            Thread.sleep(random.nextInt(3));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("1+1="+(1+1));
    }

    private void goToWc() {
        System.out.println("TIMI~~~");
        try {
            Thread.sleep(random.nextInt(30)+10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void drinkTea() {
        System.out.println("雨前龙井，清香扑鼻");
        try {
            Thread.sleep(random.nextInt(3));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void readNews() {
        System.out.println("震惊了!某国原首相被枪击，凶手居然是...");
        try {
            Thread.sleep(random.nextInt(10)+10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
}
