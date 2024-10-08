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
package io.github.deergate.demos.comp;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 *
 * TODO SchedulerTask说明
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2021-09-24  14:34
 *
 */
// @Component
public class SchedulerTask {
    
    @Scheduled(initialDelay = 3000,fixedDelay = 3000)
    public void scheduleTask(){
        System.out.println("Hello Schedule");
    }
}
