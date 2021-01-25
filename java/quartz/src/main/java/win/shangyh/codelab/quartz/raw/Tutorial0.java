/*
 * Copyright 2021 Shang Yehua
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
package win.shangyh.codelab.quartz.raw;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;

/**
 *
 * TODO Tutorial1说明
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2021-00-05  19:08
 *
 */
public class Tutorial0 {
  public static void main(String[] args) throws SchedulerException {

    SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();

    Scheduler sched = schedFact.getScheduler();

    sched.start();

    // define the job and tie it to our HelloJob class
    JobDetail job = newJob(HelloJob2.class).withIdentity("myJob", "group1").build();

    // Trigger the job to run now, and then every 40 seconds
    Trigger trigger = newTrigger().withIdentity("myTrigger", "group1").startNow()
        .withSchedule(simpleSchedule().withIntervalInSeconds(40).repeatForever()).build();

    // Tell quartz to schedule the job using our trigger
    sched.scheduleJob(job, trigger);
  }
}
