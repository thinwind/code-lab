package com.study.batch_demo.listener;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

/**
 * @author zhudan <zhudan@ebchinatech.com>
 * @since 2021/1/22 13:59
 */
public class TestStepExecutionListener implements StepExecutionListener {

    @Override
    public void beforeStep(StepExecution stepExecution) {
        System.out.println("Thread name -> (" + Thread.currentThread().getName() + ") - Step Name -> ["
        + stepExecution.getStepName() + "] - Execute Context -> {" + stepExecution.getExecutionContext() + "}");
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return null;
    }
}
