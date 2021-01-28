package com.study.batch_demo.listener;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;

/**
 * @author zhudan <zhudan@ebchinatech.com>
 * @since 2021/1/21 14:57
 */

public class File2FileListener extends JobExecutionListenerSupport {
    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED){
            System.out.println("finish");
        }
    }
}
