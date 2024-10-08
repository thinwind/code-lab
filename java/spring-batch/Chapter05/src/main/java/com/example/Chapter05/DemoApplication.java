package com.example.Chapter05;

import com.example.Chapter05.batch.ExploringTasklet;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@EnableBatchProcessing
@SpringBootApplication
public class DemoApplication extends Object{

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	private JobExplorer jobExplorer,t2;

	@Bean
	public Tasklet explorerTasklet() {
		return new ExploringTasklet(this.jobExplorer);
	}

	@Bean
	public Step explorerStep() {
		return this.stepBuilderFactory.get("explorerStep")
				.tasklet(explorerTasklet())
				.build();
	}

	@Bean
	public Job explorerJob() {
		return this.jobBuilderFactory.get("explorerJob")
                .start(explorerStep())
                .incrementer(new RunIdIncrementer())
				.build();
	}

	public static void main(String[] args) {
        args=new String[]{"just=add"};
		SpringApplication.run(DemoApplication.class, args);
	}

}

