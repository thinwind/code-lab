package com.study.batch_demo;

import com.study.batch_demo.config.File2FileBatchConfig;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collection;

@SpringBootTest
class BatchDemoApplicationTests {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job file2FileJob;


    @Test
    void testFile2FileJob() {
        JobParameters jobParameter = new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters();


        try {

            JobExecution run = jobLauncher.run(file2FileJob, jobParameter);

            StringBuffer stringBuffer = new StringBuffer();
            Collection<StepExecution> stepExecutions = run.getStepExecutions();
            stepExecutions.forEach(stepExecution -> {
                stringBuffer.append("readCount:" + stepExecution.getReadCount() + ",");
                stringBuffer.append("filterCount:" + stepExecution.getFilterCount() + ",");
                stringBuffer.append("commitCount:" + stepExecution.getCommitCount() + ",");
                stringBuffer.append("writeCount:" + stepExecution.getWriteCount());
            });

            System.out.println(System.lineSeparator() +"resultCount: "+ stringBuffer.toString());

            ExitStatus exitStatus = run.getExitStatus();
            Assert.assertEquals(ExitStatus.COMPLETED, exitStatus);


        } catch (JobExecutionAlreadyRunningException e) {
            e.printStackTrace();
        } catch (JobRestartException e) {
            e.printStackTrace();
        } catch (JobInstanceAlreadyCompleteException e) {
            e.printStackTrace();
        } catch (JobParametersInvalidException e) {
            e.printStackTrace();
        }
    }

    @Autowired
    private Job file2DbJob;

    @Test
    void testFile2DbJob() {
        JobParameters jobParameter = new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters();
        try {
            JobExecution run = jobLauncher.run(file2DbJob, jobParameter);

            StringBuffer stringBuffer = new StringBuffer();
            Collection<StepExecution> stepExecutions = run.getStepExecutions();
            stepExecutions.forEach(stepExecution -> {
                stringBuffer.append("readCount:" + stepExecution.getReadCount() + ",");
                stringBuffer.append("filterCount:" + stepExecution.getFilterCount() + ",");
                stringBuffer.append("commitCount:" + stepExecution.getCommitCount() + ",");
                stringBuffer.append("writeCount:" + stepExecution.getWriteCount());
            });

            System.out.println(System.lineSeparator() +"resultCount: "+ stringBuffer.toString());

            ExitStatus exitStatus = run.getExitStatus();
            Assert.assertEquals(ExitStatus.COMPLETED, exitStatus);

        } catch (JobExecutionAlreadyRunningException e) {
            e.printStackTrace();
        } catch (JobRestartException e) {
            e.printStackTrace();
        } catch (JobInstanceAlreadyCompleteException e) {
            e.printStackTrace();
        } catch (JobParametersInvalidException e) {
            e.printStackTrace();
        }
    }

}
