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

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;

/**
 *
 * TODO DumbJob说明
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2021-00-01  17:37
 *
 */
public class DumbJob implements Job {

  public DumbJob() {
  }

  public void execute(JobExecutionContext context) throws JobExecutionException {
    JobKey key = context.getJobDetail().getKey();

    JobDataMap dataMap = context.getJobDetail().getJobDataMap();

    String jobSays = dataMap.getString("jobSays");
    float myFloatValue = dataMap.getFloat("myFloatValue");

    System.err.println(
        "Instance " + key + " of DumbJob says: " + jobSays + ", and val is: " + myFloatValue);
  }
}
