/* 
 * Copyright 2021 Shang Yehua
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
package win.shangyh.codelab.quartz.raw;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

/**
 *
 * TODO QuickStart说明
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2021-00-05  18:50
 *
 */
public class QuickStart {
  public static void main(String[] args) {
    try {
      // Grab the Scheduler instance from the Factory
      Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

      // and start it off
      scheduler.start();

      scheduler.shutdown();

  } catch (SchedulerException se) {
      se.printStackTrace();
  }
  }
}
