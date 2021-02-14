package win.shangyh.codelab.quartz.springboot;

import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.JobKey.jobKey;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

@Component
public class JobDefine {

    @Autowired
    Scheduler scheduler;

    public void defineNewJob() throws SchedulerException {
        JobDetail job=newJob(DataSynchJob.class)
                .withIdentity("data_syn_job","data_syn")
                .storeDurably()
                .build();
        scheduler.addJob(job,false);
    }

    public void startJob() throws SchedulerException {
        stopJob();
        Trigger trigger = newTrigger().withIdentity("data_syn_trigger","data_syn")
                .startAt(new Date(System.currentTimeMillis()+10*1000))
                .forJob("data_syn_job","data_syn")
                .withSchedule(simpleSchedule().withIntervalInSeconds(10).repeatForever()).build();
        scheduler.scheduleJob(trigger);
    }

    public void scheduleForStop() throws SchedulerException {
        JobDetail job=newJob(InnerJob.class)
                .withIdentity("stopper")
                .build();
        Trigger trigger = newTrigger().withIdentity("stopper_trigger")
                .startAt(new Date(System.currentTimeMillis()+30*1000))
                .build();
        scheduler.scheduleJob(job, trigger);
        System.out.println("scheduleForStop");
    }

    private void stopJob() throws SchedulerException {
        List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey("data_syn_job", "data_syn"));
        if (triggers == null || triggers.isEmpty()) {
            return;
        }
        scheduler.unscheduleJobs(triggers.stream().map(Trigger::getKey).collect(toList()));
        System.out.println("stopped");
    }

    public class InnerJob implements Job{

        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            try {
                stopJob();
                System.out.println("stopJob");
            } catch (SchedulerException e) {
                e.printStackTrace();
            }
        }
    }
}
