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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.PersistJobDataAfterExecution;

/**
 *
 * TODO DumbJob说明
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2021-00-01  17:37
 *
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class DumpJobWithSetter implements Job {

  private String jobSays;

  private float myFloatValue;

  public void execute(JobExecutionContext context) throws JobExecutionException {
    JobKey key = context.getJobDetail().getKey();

    JobDataMap dataMap = context.getJobDetail().getJobDataMap();
    List<Date> state = (List<Date>) dataMap.get("state");
    if (state == null) {
      System.out.println("||----------->> state is null");
      state = new ArrayList<>();
      dataMap.put("state", state);
    }

    System.out.println("||----------->> state's hashcode:" + state.hashCode());
    state.add(new Date());

    for (int i = 0; i < state.size(); i++) {
      System.out.println("||===========>> " + i + "\t"
          + new SimpleDateFormat("YYYY-MM-DD HH:mm:ss").format(state.get(i)));
    }

    System.out.println("||===========>> Instance " + key + " of DumbJob says: " + jobSays
        + ", and val is: " + myFloatValue);
    // datamap并不会每次持久化，只有判断isDirty的情况下才会持久化
    // 因此必须要直接调用put方法才可以保证被持久化
    System.out.println("datamap is dirty:"+dataMap.isDirty());
    dataMap.put("flag", false);
    dataMap.clearDirtyFlag();
    System.out.println("datamap is dirty:"+dataMap.isDirty());
  }

  public String getJobSays() {
    return jobSays;
  }

  public void setJobSays(String jobSays) {
    this.jobSays = jobSays;
  }

  public float getMyFloatValue() {
    return myFloatValue;
  }

  public void setMyFloatValue(float myFloatValue) {
    this.myFloatValue = myFloatValue;
  }


}
