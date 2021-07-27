/**
 * Copyright 2019 the original author or authors.
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
package com.example.Chapter04.jobs;

import java.util.Arrays;
import com.example.Chapter04.batch.DailyJobTimestamper;
import com.example.Chapter04.batch.HelloWorldTasklet;
import com.example.Chapter04.batch.JobLoggerListener;
import com.example.Chapter04.batch.ParameterValidator;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.CompositeJobParametersValidator;
import org.springframework.batch.core.job.DefaultJobParametersValidator;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.listener.ExecutionContextPromotionListener;
import org.springframework.batch.core.listener.JobListenerFactoryBean;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author Michael Minella
 */
// @EnableBatchProcessing
// @SpringBootApplication
public class HelloWorldJob {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public CompositeJobParametersValidator validator() {
        CompositeJobParametersValidator validator = new CompositeJobParametersValidator();

        DefaultJobParametersValidator defaultJobParametersValidator =
                new DefaultJobParametersValidator(new String[] {"fileName"},
                        new String[] {"name","var", "currentDate","run.id"});

        defaultJobParametersValidator.afterPropertiesSet();

        validator.setValidators(
                Arrays.asList(new ParameterValidator(), defaultJobParametersValidator));

        return validator;
    }

    //	@Bean
    //	public Job job() {
    //
    //		return this.jobBuilderFactory.get("basicJob")
    //				.start(step1())
    //				.validator(validator())
    //				.incrementer(new DailyJobTimestamper())
    ////				.listener(new JobLoggerListener())
    //				.listener(JobListenerFactoryBean.getListener(new JobLoggerListener()))
    //				.build();
    //	}
    //
    
    // @Bean
    // public JobParametersValidator validator(){
    //     return new ParameterValidator();
    // }
    
    @Bean
    public Job job() {
        return this.jobBuilderFactory.get("basicJob2").start(step1())
                .validator(validator())
                .incrementer(new DailyJobTimestamper())
                .listener(new JobLoggerListener())
                // .listener(JobListenerFactoryBean.getListener(new JobLoggerListener()))
                .build();
    }
    
    public StepExecutionListener promotionListener(){
        ExecutionContextPromotionListener promotionListener=new ExecutionContextPromotionListener();
        promotionListener.setKeys(new String[]{"name"});
        return promotionListener;
    }

    @Bean
    public Step step1() {
        return this.stepBuilderFactory.get("step1")
                .tasklet(helloInstance())
                .listener(promotionListener())
                .build();
    }
    
    @Bean
    @StepScope
    public Tasklet helloInstance(){
        return new HelloWorldTasklet();
    }

    // @StepScope
    // @Bean
    // public Tasklet helloWorldTasklet(@Value("#{jobParameters['name']}") String name,
    //         @Value("#{jobParameters['fileName']}") String fileName) {

    //     return (contribution, chunkContext) -> {
    //         String nameInCtx = (String)chunkContext.getStepContext().getJobParameters().get("name");
    //         ExecutionContext executionContext = chunkContext.getStepContext().getStepExecution()
    //         .getJobExecution()
    //         .getExecutionContext();
    //         executionContext.put("name", nameInCtx);
    //         System.out.println(String.format("Hello, %s!", name));
    //         System.out.println(String.format("fileName = %s", fileName));

    //         return RepeatStatus.FINISHED;
    //     };
    // }

    	@Bean
        @StepScope
    	public Tasklet helloWorldTasklet(@Value("#{jobParameters['var']}") String var,
                @Value("#{jobParameters['fileName']}") String fileName) {
    
    		return (contribution, chunkContext) -> {
    				System.out.println(String.format("Hello, %s!",var));
                    System.out.println(String.format("fileName = %s", fileName));
    				return RepeatStatus.FINISHED;
    			};
    	}

    public static void main(String[] args) {
        args = new String[] {"name=bar","fileName=some-file.csv","-var=foo"};
        SpringApplication.run(HelloWorldJob.class, args);
    }
}
