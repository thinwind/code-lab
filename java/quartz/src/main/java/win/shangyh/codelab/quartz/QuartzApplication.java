package win.shangyh.codelab.quartz;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import win.shangyh.codelab.quartz.springboot.JobDefine;

@SpringBootApplication
public class QuartzApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuartzApplication.class, args);
    }


    @Bean
    public CommandLineRunner testJob(JobDefine jobDefine) {
        return args -> {
//            jobDefine.defineNewJob();
            jobDefine.startJob();
            jobDefine.scheduleForStop();
        };
    }
}
